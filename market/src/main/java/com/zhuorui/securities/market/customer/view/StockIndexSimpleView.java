package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.zhuorui.securities.market.R;
import org.jetbrains.annotations.NotNull;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-11-06 10:04
 * desc   :
 */
public class StockIndexSimpleView extends FrameLayout {

    private TextView vName;
    private TextView vPrcie;
    private TextView vUpsDownsPirce;
    private TextView vUpsDowns;
    private int color = Color.parseColor("#C3CDE3");
    private int upColor;
    private int downColor;

    public StockIndexSimpleView(Context context) {
        this(context, null);
    }

    public StockIndexSimpleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StockIndexSimpleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_stock_index_simple, this);
        vName = findViewById(R.id.tv_simple_title);
        vPrcie = findViewById(R.id.tv_simple_price);
        vUpsDownsPirce = findViewById(R.id.tv_simple_ups_downs_pirce);
        vUpsDowns = findViewById(R.id.tv_simple_ups_downs);
    }

    public void setColor(int up, int down) {
        upColor = up;
        downColor = down;
    }

    public void setData(@NotNull String name, float price, float pieClosePirce) {
        vName.setText(name);
        int c;
        if (price > pieClosePirce) {
            c = upColor;
        } else if (price < pieClosePirce) {
            c = downColor;
        } else {
            c = color;
        }
        vPrcie.setTextColor(c);
        vUpsDownsPirce.setTextColor(c);
        vUpsDowns.setTextColor(c);
        vPrcie.setText(String.format("%.2f", price));
        float d = price - pieClosePirce;
        vUpsDownsPirce.setText(String.format("%+.2f", d));
        vUpsDowns.setText(String.format("%+.2f%%", d / price * 100f));
    }
}
