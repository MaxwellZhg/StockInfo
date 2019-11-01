package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.zhuorui.securities.market.R;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-11-01 10:52
 * desc   :
 */
public class StockInformationView extends FrameLayout {
    public StockInformationView(Context context) {
        this(context,null);
    }

    public StockInformationView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StockInformationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_stock_information,this);
    }
}
