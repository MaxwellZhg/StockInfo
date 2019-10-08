package com.zhuorui.commonwidget.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import com.zhuorui.securities.base2app.util.NetUtil;
import org.jetbrains.annotations.NotNull;

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/10/8 14:54
 * desc   : 监听网络变化
 */
public class NetworkReceiver extends BroadcastReceiver {

    private NetworkCallback networkCallback;
    private ConnectivityManager.NetworkCallback callback;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (NetUtil.isNetworkAvailable(context)) {
                this.networkCallback.onAvailable();
            } else {
                this.networkCallback.onLost();
            }
        }
    }

    /**
     * 注册监听
     * @param context
     * @param networkCallback
     */
    public void register(Context context, final NetworkCallback networkCallback){
        this.networkCallback = networkCallback;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            callback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NotNull Network network) {
                    super.onAvailable(network);
                    networkCallback.onAvailable();
                }

                @Override
                public void onLost(@NotNull Network network) {
                    super.onLost(network);
                    networkCallback.onLost();
                }
            };
            connManager.registerDefaultNetworkCallback(callback);
        } else {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(this, intentFilter);
        }
    }

    /**
     * 取消网络监听
     */
    public void unRegister(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            connManager.unregisterNetworkCallback(callback);
        } else {
            context.unregisterReceiver(this);
        }
    }

    /**
     * 网络状态回调
     */
    public interface NetworkCallback {

        /**
         * 网络可用
         */
        void onAvailable();

        /**
         * 网络不可用，丢失连接
         */
        void onLost();
    }
}