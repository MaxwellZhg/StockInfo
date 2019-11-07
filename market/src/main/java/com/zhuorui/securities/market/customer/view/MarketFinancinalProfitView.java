package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.zhuorui.securities.market.R;

import java.util.ArrayList;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/7
 * Desc:
 */
public class MarketFinancinalProfitView  extends FrameLayout {
    private int mOut1Color;
    private int mOut2Color;
    private int mOut3Color;
    ArrayList<Integer> colors = new ArrayList<Integer>();

    public MarketFinancinalProfitView(Context context) {
        this(context,null);
    }

    public MarketFinancinalProfitView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MarketFinancinalProfitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initColor();
        inflate(context, R.layout.layout_market_profit_view,this);
    }

    private void initColor() {
        mOut1Color = Color.parseColor("#3A79C8");
        mOut2Color = Color.parseColor("#5DA6F2");
        mOut3Color = Color.parseColor("#FF8E1B");
        colors = new ArrayList();
        colors.add(mOut1Color);
        colors.add(mOut2Color);
        colors.add(mOut3Color);
    }
}
