package com.dycm.applib1.socket;

import android.annotation.SuppressLint;
import com.dycm.applib1.event.SocketDisconnectEvent;
import com.dycm.applib1.model.StockTopic;
import com.dycm.applib1.model.StockTopicDataTypeEnum;
import com.dycm.applib1.util.ByteBufferUtil;
import com.dycm.applib1.util.GZipUtil;
import com.dycm.base2app.infra.LogInfra;
import com.dycm.base2app.rxbus.RxBus;
import com.dycm.base2app.util.DeviceUtil;
import com.dycm.base2app.util.JsonUtil;
import com.dycm.base2app.util.Md5Util;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SocketClient {

    private final String TAG = "SocketClient";

    private static SocketClient instance;
    private WebSocketClient client;
    private Map<String, SocketRequest> requestMap;

    private final boolean openGzip = true;

    public static SocketClient getInstance() {
        if (instance == null) {
            synchronized (SocketClient.class) {
                if (instance == null) {
                    instance = new SocketClient();
                }
            }
        }
        return instance;
    }

    public void destroy() {
        if (client != null) {
            // 解除所有的订阅
            unBindAllTopic();
            // 断开连接
            client.close();
            client = null;
        }
        if (requestMap != null) {
            requestMap.clear();
            requestMap = null;
        }
    }

    public void connect() {
        try {
            // 先断开上一次的连接
            destroy();
            // 重新创建连接
            requestMap = new HashMap<>();
            client = new WebSocketClient(new URI(SocketApi.SOCKET_URL), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    LogInfra.Log.d(TAG, "握手成功");
                    sendAuth();
                }

                @Override
                public void onMessage(String message) {

                }

                @Override
                public void onMessage(ByteBuffer byteBuffer) {
                    String message;
                    if (openGzip) {
                        byte[] bytes = ByteBufferUtil.byteBuffer2Byte(byteBuffer);
                        message = GZipUtil.uncompressToString(bytes);
                    } else {
                        message = ByteBufferUtil.byteBuffer2String(byteBuffer);
                    }
                    LogInfra.Log.d(TAG, "<-- onMessage " + message);
                    if (message.equals("over")) {
                        client.close();
                    }
                    try {
                        JSONObject jsonObject = JsonUtil.toJSONObject(message);
                        if (jsonObject != null && jsonObject.has("header")) {
                            // 传递股市行情信息
                            RxBus.getDefault().post(JsonUtil.fromJson(message, StocksTopicResponse.class));
                        } else {
                            SocketResponse response = JsonUtil.fromJson(message, SocketResponse.class);
                            if (response != null && response.isSuccessful()) {
                                switch (Objects.requireNonNull(response.getPath())) {
                                    case SocketApi.AUTH:
                                        // 自动订阅本地纪录中的自选股
//                                        LocalStocksConfig config = LocalStocksConfig.Companion.read();
//                                        StockTopic[] stockTopics = config.getStocks().toArray(new StockTopic[0]);
//                                        bindTopic(stockTopics);
                                        // TODO 暂时写死
                                        StockTopic stockTopic = new StockTopic(StockTopicDataTypeEnum.kminute, "SZ", "000001", 1);
                                        bindTopic(stockTopic);
                                        break;
                                    case SocketApi.TOPIC_UNBIND:
                                        // 传递上层，解绑订阅成功
                                        RxBus.getDefault().post(new StockUnBindTopicResponse(requestMap.remove(response.getResp_id())));
                                        break;
                                }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onWebsocketPing(WebSocket conn, Framedata f) {
                    LogInfra.Log.d(TAG, "ping>>" + f);
                    super.onWebsocketPing(conn, f);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    LogInfra.Log.d(TAG, "链接已关闭");
                    // 通知上层连接断开
                    RxBus.getDefault().post(new SocketDisconnectEvent());
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    LogInfra.Log.d(TAG, "发生错误已关闭");
                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client.setConnectionLostTimeout(30);
        client.connect();
        client.getDraft();
        LogInfra.Log.d(TAG, "正在连接...");
    }

    private void sendRequest(String json) {
        if (client == null || client.getReadyState() != WebSocket.READYSTATE.OPEN) {
            return;
        }
        LogInfra.Log.d(TAG, "--> sendRequest " + json);
        if (openGzip) {
            byte[] zipBytes = GZipUtil.compress(json);
            client.send(ByteBuffer.wrap(zipBytes));
        } else {
            client.send(ByteBufferUtil.string2ByteBuffer(json));
        }
    }

    public void bindTopic(StockTopic... topics) {
        if (topics == null || topics.length == 0) {
            return;
        }
        SocketRequest param = createTopicMessage(SocketApi.TOPIC_BIND, topics);
        sendRequest(JsonUtil.toJson(param));
    }

    public void unBindTopic(StockTopic... topics) {
        SocketRequest param = createTopicMessage(SocketApi.TOPIC_UNBIND, topics);
        sendRequest(JsonUtil.toJson(param));
        requestMap.put(Objects.requireNonNull(param.getHeader()).getReq_id(), param);
    }

    private void unBindAllTopic() {
        SocketRequest param = createTopicMessage(SocketApi.TOPIC_UNBIND_ALL);
        sendRequest(JsonUtil.toJson(param));
    }

    private void sendAuth() {
        String devId = DeviceUtil.getDeviceUuid();

        SocketRequest param = new SocketRequest();
        SocketHeader socketHeader = new SocketHeader();
        socketHeader.setDev_id(devId);
        socketHeader.setLanguage("ZN");
        socketHeader.setReq_id(UUID.randomUUID().toString());
        socketHeader.setVersion("1.0.0");
        socketHeader.setPath(SocketApi.AUTH);
        param.setHeader(socketHeader);

        Map<String, Object> body = new HashMap<>();
        body.put("dev_id", devId);
        long timestamp = System.currentTimeMillis();
        body.put("timestamp", System.currentTimeMillis());
        String str = devId + timestamp + SocketApi.SOCKET_AUTH_SIGNATURE;
        String token = Md5Util.getMd5Str(str);
        body.put("token", token);

        param.setBody(body);
        sendRequest(JsonUtil.toJson(param));
    }

    @SuppressLint("DefaultLocale")
    private SocketRequest createTopicMessage(String path, StockTopic... topics) {
        SocketRequest socketRequest = new SocketRequest();

        SocketHeader socketHeader = new SocketHeader();
        socketHeader.setLanguage("ZN");
        socketHeader.setReq_id(UUID.randomUUID().toString());
        socketHeader.setVersion("1.0.0");
        socketHeader.setPath(path);
        socketRequest.setHeader(socketHeader);

        if (topics != null) {
            StockSubTopic subTopic = new StockSubTopic();
            subTopic.setTopics(topics);
            socketRequest.setBody(subTopic);
        }

        return socketRequest;
    }
}