package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.zhuorui.securities.market.R;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-11-01 10:18
 * desc   :
 */
public class StockHAView extends FrameLayout {
    public StockHAView(Context context) {
        this(context,null);
    }

    public StockHAView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StockHAView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_stock_h_a,this);
        initView();
    }

    private void initView() {

    }
}
