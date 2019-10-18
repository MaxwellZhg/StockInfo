package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.zhuorui.securities.market.R;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-18 17:45
 * desc   :
 */
public class OrderBrokerView extends FrameLayout {
    public OrderBrokerView(Context context) {
        this(context,null);
    }

    public OrderBrokerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public OrderBrokerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_order_broker,this);
    }
}
