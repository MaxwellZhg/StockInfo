package com.dycm.applib1.socket;

import android.annotation.SuppressLint;
import com.dycm.applib1.Lib1Constants;
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
import java.util.*;

public class SocketClient {

    private final String TAG = "SocketClient";

    private static SocketClient instance;
    private WebSocketClient client;

    private final boolean openGzip = true;

    public static SocketClient getInstance() {
        if (instance == null) {
            synchronized (SocketClient.class) {
                if (instance == null) {
                    instance = new SocketClient();
                }
            }
        }
        instance.destroy();
        return instance;
    }

    public void destroy() {
        if (client != null) {
            client.close();
            client = null;
        }
    }

    public void newClient() {
        try {
            client = new WebSocketClient(new URI(Lib1Constants.SOCKET_URL), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    LogInfra.Log.d(TAG, "握手成功");
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
                    LogInfra.Log.d(TAG, "onMessage:" + message);
                    if (message.equals("over")) {
                        client.close();
                    }
                    try {
                        JSONObject jsonObject = JsonUtil.toJSONObject(message);
                        if (jsonObject != null && jsonObject.has("header")) {
                            // 传递股市行情信息
                            RxBus.getDefault().post(JsonUtil.fromJson(message, StocksResponse.class));
                        } else {
                            SocketResponse response = JsonUtil.fromJson(message, SocketResponse.class);
                            if (response != null && response.isSuccessful() && response.getPath().equals(Lib1Constants.AUTH)) {
                                bindTopic();
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
        while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            LogInfra.Log.d(TAG, "正在连接...");
        }

        sendAuth();
    }

    private void bindTopic() {
        SocketRequest param = createTopicMessage();
        String json = JsonUtil.toJson(param);
        LogInfra.Log.d(TAG, "压缩前：" + json);
        if (openGzip) {
            byte[] zipBytes = GZipUtil.compress(json);
            LogInfra.Log.d(TAG, "压缩后：" + zipBytes.length);
            client.send(ByteBuffer.wrap(zipBytes));
        } else {
            client.send(ByteBufferUtil.string2ByteBuffer(json));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                int index = 0;
                while (client!= null) {
                    index++;
                    if (index % 30 == 0) {
                        client.sendPing();
                    }
                    if (index % 10 == 0) {
                        SocketRequest param;
                        if (new Random().nextInt(2) == 0) {
                            param = createTopicMessage();
                        } else {
                            param = createTopicMessage();
                        }
                        String json = JsonUtil.toJson(param);
                        LogInfra.Log.d(TAG, "压缩前：" + json);
                        if (openGzip) {
                            byte[] zipBytes = GZipUtil.compress(json);
                            LogInfra.Log.d(TAG, "压缩后：" + zipBytes.length);
                            client.send(ByteBuffer.wrap(zipBytes));
                        } else {
                            client.send(ByteBufferUtil.string2ByteBuffer(json));
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void sendAuth() {

        String devId = DeviceUtil.getDeviceUuid();

        SocketRequest SocketRequest = new SocketRequest();
        SocketHeader SocketHeader = new SocketHeader();
        SocketHeader.setDevId(devId);
        SocketHeader.setLanguage("ZN");
        SocketHeader.setReqId(UUID.randomUUID().toString());
        SocketHeader.setVersion("1.0.0");
        SocketHeader.setPath(Lib1Constants.AUTH);


        SocketRequest.setHeader(SocketHeader);

        Map<String, Object> body = new HashMap<>();
        body.put("dev_id", devId);
        long timestamp = System.currentTimeMillis();
        body.put("timestamp", System.currentTimeMillis());
        String str = devId + timestamp + Lib1Constants.SOCKET_AUTH_SIGNATURE;
        String token = Md5Util.getMd5Str(str);
        body.put("token", token);

        SocketRequest.setBody(body);
        String json = JsonUtil.toJson(SocketRequest);
        LogInfra.Log.d(TAG, "压缩前：" + json);
        if (openGzip) {
            byte[] zipBytes = GZipUtil.compress(json);
            LogInfra.Log.d(TAG, "压缩后：" + zipBytes.length);
            client.send(ByteBuffer.wrap(zipBytes));
        } else {
            client.send(ByteBufferUtil.string2ByteBuffer(json));
        }
    }

    @SuppressLint("DefaultLocale")
    private SocketRequest createTopicMessage() {
        SocketRequest SocketRequest = new SocketRequest();

        SocketHeader SocketHeader = new SocketHeader();
        SocketHeader.setLanguage("ZN");
        SocketHeader.setReqId(UUID.randomUUID().toString());
        SocketHeader.setVersion("1.0.0");
        SocketHeader.setPath(Lib1Constants.BIND);

        SocketRequest.setHeader(SocketHeader);

        SubTopic subTopic = new SubTopic();
        List<String> topics = new ArrayList<>();
        for (int k = 0; k < 5; k++) {
            topics.add("/topic/" + String.format("%0" + 5 + "d", new Random().nextInt(2000)));
        }
        subTopic.setTopics(topics);

        SocketRequest.setBody(subTopic);
        LogInfra.Log.d(TAG, JsonUtil.toJson(SocketRequest));
        return SocketRequest;
    }
}