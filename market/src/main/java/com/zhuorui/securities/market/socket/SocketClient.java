package com.zhuorui.securities.market.socket;

import android.annotation.SuppressLint;
import android.util.TypedValue;
import com.zhuorui.securities.base2app.infra.LogInfra;
import com.zhuorui.securities.base2app.rxbus.RxBus;
import com.zhuorui.securities.base2app.util.DeviceUtil;
import com.zhuorui.securities.base2app.util.JsonUtil;
import com.zhuorui.securities.base2app.util.Md5Util;
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent;
import com.zhuorui.securities.market.event.SocketConnectEvent;
import com.zhuorui.securities.market.model.StockTopic;
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum;
import com.zhuorui.securities.market.socket.push.*;
import com.zhuorui.securities.market.socket.request.GetStockKlineGetDailyRequestBody;
import com.zhuorui.securities.market.socket.request.GetStockMinuteKlineRequestBody;
import com.zhuorui.securities.market.socket.request.SocketHeader;
import com.zhuorui.securities.market.socket.request.SocketRequest;
import com.zhuorui.securities.market.socket.response.*;
import com.zhuorui.securities.market.util.ByteBufferUtil;
import com.zhuorui.securities.market.util.GZipUtil;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
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
    //    private Map<String, SocketRequest> requestMap;
    private boolean isConnected = false;

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
//        if (requestMap != null) {
//            requestMap.clear();
//            requestMap = null;
//        }
    }

    public void connect() {
        if (isConnected) return;
        try {
            // 创建连接
//            requestMap = new HashMap<>();
            client = new WebSocketClient(new URI(SocketApi.SOCKET_URL), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    LogInfra.Log.d(TAG, "握手成功");
                    RxBus.getDefault().post(new SocketConnectEvent(true));
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
                            JSONObject header = jsonObject.getJSONObject("header");
                            String path = header.getString("path");
                            JSONObject body = jsonObject.getJSONObject("body");
                            switch (path) {
                                case SocketApi.PUSH_STOCK_INFO:
                                    // 行情
                                    RxBus.getDefault().post(JsonUtil.fromJson(message, StocksTopicMarketResponse.class));
                                    break;
                                case SocketApi.PUSH_STOCK_KLINE:
                                    // K线
                                    String klineType = body.getString("klineType");

                                    // TODO 暂时只判断2-1
                                    if (klineType.equals(StockTopicDataTypeEnum.MINUTE.getValue())) {
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, StocksTopicMinuteKlineResponse.class));
                                    }
                                    break;
                        /*        case SocketApi.PUSH_STOCK_TRANS:
                                    RxBus.getDefault().post(JsonUtil.fromJson(message, StocksTopicTransResponse.class));
                                    break;*/
                                case SocketApi.PUSH_STOCK_PRICE:
                                    RxBus.getDefault().post(JsonUtil.fromJson(message, StocksTopicPriceResponse.class));
                                    break;
                                case SocketApi.PUSH_STOCK_TRADE:
                                    RxBus.getDefault().post(JsonUtil.fromJson(message, StocksTopicTradeResponse.class));
                                    break;
                                case SocketApi.PUSH_STOCK_TRADESTA:
                                    RxBus.getDefault().post(JsonUtil.fromJson(message, StocksTopicTradeStaResponse.class));
                                    break;
                                case SocketApi.PUSH_STOCK_ORDER:
                                    RxBus.getDefault().post(JsonUtil.fromJson(message, StocksTopicOrderResponse.class));
                                    break;
                                case SocketApi.PUSH_STOCK_ORDERBROKER:
                                    RxBus.getDefault().post(JsonUtil.fromJson(message, StocksTopicOrderBrokerResponse.class));
                                    break;
                                    //推送指数股票盘口
                                case SocketApi.PUSH_STOCK_HANDICAP:
                                    int type= body.getInt("type");
                                    if(type==2) {
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, StocksTopicHandicapResponse.class));
                                    }else{
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, StockTopicIndexHandicapResponse.class));
                                    }
                                    break;
                                case SocketApi.PUSH_STOCK_CAPITAL:
                                    RxBus.getDefault().post(JsonUtil.fromJson(message, StocksTopicCapitalResponse.class));
                                    break;
                            }
                        } else {
                            SocketResponse response = JsonUtil.fromJson(message, SocketResponse.class);
                            if (response != null && response.isSuccessful()) {
                                switch (Objects.requireNonNull(response.getPath())) {
                                    case SocketApi.AUTH:
                                        // 认证成功
                                        isConnected = true;
                                        RxBus.getDefault().post(new SocketAuthCompleteEvent());
                                        break;
                                    case SocketApi.TOPIC_UNBIND:
                                        // 传递上层，解绑订阅成功
//                                        RxBus.getDefault().post(new StockUnBindTopicResponse(requestMap.remove(response.getRespId())));
                                        break;
                                    case SocketApi.GET_KLINE_MINUTE:
                                        // 获取分时
//                                        requestMap.remove(response.getRespId());
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, GetStocksMinuteKlineResponse.class));
                                        break;
                                    case SocketApi.GET_KLINE_GET_DAILY:
                                        // 获取日K
//                                        requestMap.remove(response.getRespId());
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, StocksDayKlineResponse.class));
                                        break;
                                    case SocketApi.GET_KLINE_FIVE_DAY:
                                        // 获取五日
//                                        requestMap.remove(response.getRespId());
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, StocksFiveDayKlineResponse.class));
                                        break;
                                    case SocketApi.GET_STOCK_PRICE:
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, GetStockPriceResponse.class));
                                        break;
                                    case SocketApi.GET_STOCK_ORDER:
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, GetStocksOrderResponse.class));
                                        break;
                                    case SocketApi.GET_STOCK_ORDER_BROKER:
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, GetStocksOrderBrokerResponse.class));
                                        break;
                                    case SocketApi.GET_STOCK_TRADE:
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, GetStockTradeResponse.class));
                                        break;
                                    case SocketApi.GET_STOCK_TRADESTA:
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, GetStockTradeStaResponse.class));
                                        break;
                                    case SocketApi.GET_CAPITAL:
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, GetCapitalResponse.class));
                                        break;
                                    case SocketApi.GET_STOCK_HANDICAP:
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, GetStockHandicapResponse.class));
                                        break;
                                    case SocketApi.GET_INDEX_HANDICAP:
                                        RxBus.getDefault().post(JsonUtil.fromJson(message, GetIndexHandicapResponse.class));
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
                    LogInfra.Log.d(TAG, "链接已关闭：" + s);
                    // 通知上层连接断开
                    RxBus.getDefault().post(new SocketConnectEvent(false));
                    isConnected = false;
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    LogInfra.Log.d(TAG, "发生错误已关闭：" + e.getMessage());
                    isConnected = false;
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

    private void sendRequest(SocketRequest request) {
        if (request == null || client == null || client.getReadyState() != WebSocket.READYSTATE.OPEN) {
            return;
        }
        try {
            String json = JsonUtil.toJson(request);
            if (openGzip) {
                byte[] zipBytes = GZipUtil.compress(json);
                client.send(ByteBuffer.wrap(zipBytes));
            } else {
                client.send(ByteBufferUtil.string2ByteBuffer(json));
            }
            LogInfra.Log.d(TAG, "--> sendRequest " + json);
        } catch (WebsocketNotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void bindTopic(StockTopic... topics) {
        if (topics == null || topics.length == 0) {
            return;
        }
        sendRequest(createTopicMessage(SocketApi.TOPIC_BIND, topics));
    }

    public void unBindTopic(StockTopic... topics) {
        SocketRequest request = createTopicMessage(SocketApi.TOPIC_UNBIND, topics);
        sendRequest(request);
//        requestMap.put(Objects.requireNonNull(request.getHeader()).getReqId(), request);
    }

    public void unBindAllTopic() {
        SocketRequest request = createTopicMessage(SocketApi.TOPIC_UNBIND_ALL);
        sendRequest(request);
    }

    private void sendAuth() {
        String devId = DeviceUtil.getDeviceUuid();

        SocketRequest request = new SocketRequest();
        SocketHeader socketHeader = getRequestHeader(SocketApi.AUTH);
        socketHeader.setDeviceId(devId);
        request.setHeader(socketHeader);

        Map<String, Object> body = new HashMap<>();
        body.put("deviceId", devId);
        long timestamp = System.currentTimeMillis();
        body.put("timestamp", System.currentTimeMillis());
        String str = devId + timestamp + SocketApi.SOCKET_AUTH_SIGNATURE;
        String token = Md5Util.getMd5Str(str);
        body.put("token", token);

        request.setBody(body);
        sendRequest(request);
    }

    @SuppressLint("DefaultLocale")
    private SocketRequest createTopicMessage(String path, StockTopic... topics) {
        SocketRequest socketRequest = new SocketRequest();
        SocketHeader socketHeader = getRequestHeader(path);
        socketRequest.setHeader(socketHeader);

        if (topics != null) {
            StockSubTopic subTopic = new StockSubTopic();
            subTopic.setTopics(topics);
            socketRequest.setBody(subTopic);
        }

        return socketRequest;
    }

    @SuppressLint("DefaultLocale")
    public String requestGetDailyKline(GetStockKlineGetDailyRequestBody getStockKlineGetDailyRequestBody) {
        SocketRequest request = new SocketRequest();
        SocketHeader socketHeader = getRequestHeader(SocketApi.GET_KLINE_GET_DAILY);
        request.setHeader(socketHeader);
        request.setBody(getStockKlineGetDailyRequestBody);

        sendRequest(request);
//        requestMap.put(Objects.requireNonNull(request.getHeader()).getReqId(), request);
        return socketHeader.getReqId();
    }

    @SuppressLint("DefaultLocale")
    public String requestGetFiveDayKline(GetStockKlineGetDailyRequestBody getStockKlineGetDailyRequestBody) {
        SocketRequest request = new SocketRequest();
        SocketHeader socketHeader = getRequestHeader(SocketApi.GET_KLINE_FIVE_DAY);
        request.setHeader(socketHeader);
        request.setBody(getStockKlineGetDailyRequestBody);

        sendRequest(request);
//        requestMap.put(Objects.requireNonNull(request.getHeader()).getReqId(), request);
        return socketHeader.getReqId();
    }

    @SuppressLint("DefaultLocale")
    public String requestGetMinuteKline(GetStockMinuteKlineRequestBody getStockMinuteKlineRequestBody) {
        SocketRequest request = new SocketRequest();
        SocketHeader socketHeader = getRequestHeader(SocketApi.GET_KLINE_MINUTE);
        request.setHeader(socketHeader);
        request.setBody(getStockMinuteKlineRequestBody);

        sendRequest(request);
//        requestMap.put(Objects.requireNonNull(request.getHeader()).getReqId(), request);
        return socketHeader.getReqId();
    }

    /**
     * 发起一个自定义参数的请求
     *
     * @return
     */
    public String postRequest(Object requestBody, String path) {
        SocketRequest request = new SocketRequest();
        SocketHeader socketHeader = getRequestHeader(path);
        request.setHeader(socketHeader);
        request.setBody(requestBody);

        sendRequest(request);
        return socketHeader.getReqId();
    }

    private SocketHeader getRequestHeader(String path) {
        SocketHeader socketHeader = new SocketHeader();
        socketHeader.setLanguage("ZN");
        socketHeader.setReqId(UUID.randomUUID().toString());
        socketHeader.setVersion("1.0.0");
        socketHeader.setPath(path);
        return socketHeader;
    }
}