package com.zhuorui.securities.push;

import android.content.Context;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.zhuorui.securities.base2app.infra.LogInfra;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 */
public class ZRIntentService extends GTIntentService {

    @Override
    public void onReceiveServicePid(Context context, int pid) {

    }

    // 处理透传消息
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        byte[] payload = msg.getPayload();
        if (payload == null) {
            LogInfra.Log.d(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            LogInfra.Log.d(TAG, "receiver payload = " + data);
            ZRPushManager.Companion.getInstance().onReceiveMessageData(data);
        }
    }

    // 接收 cid
    @Override
    public void onReceiveClientId(Context context, String clientid) {
        LogInfra.Log.d(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    // cid 离线上线通知
    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    // 各种事件处理回执
    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }

    // 通知到达，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
        LogInfra.Log.d(TAG, "onNotificationMessageArrived -> " + "msg = " + msg);
    }

    // 通知点击，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {
        LogInfra.Log.d(TAG, "onNotificationMessageClicked -> " + "msg = " + msg);
    }
}