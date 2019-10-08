package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.zhuorui.commonwidget.receiver.NetworkReceiver;
import com.zhuorui.securities.base2app.util.NetUtil;

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/10/8 14:54
 * desc   : 网络错误提示
 */
public class ZRNetworkUnavailableView extends LinearLayout implements View.OnClickListener, NetworkReceiver.NetworkCallback {

    private NetworkReceiver networkReceiver;

    public ZRNetworkUnavailableView(Context context) {
        super(context);
        init();
    }

    public ZRNetworkUnavailableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZRNetworkUnavailableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_network_unavailable_tips, this);
        findViewById(R.id.btn_check).setOnClickListener(this);
        checkNetworkState();

        networkReceiver = new NetworkReceiver();
        networkReceiver.register(getContext(), this);
    }

    private void checkNetworkState() {
        if (NetUtil.isNetworkAvailable(getContext())) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        getContext().startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
    }

    @Override
    public void onAvailable() {
        post(new Runnable() {
            @Override
            public void run() {
                setVisibility(GONE);
            }
        });
    }

    @Override
    public void onLost() {
        post(new Runnable() {
            @Override
            public void run() {
                setVisibility(VISIBLE);
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        networkReceiver.unRegister(getContext());
    }
}
