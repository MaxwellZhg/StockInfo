package com.zhuorui.securities.market.customer.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.zhuorui.securities.market.R;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-11 18:18
 * desc   : 股票详情指数view
 */
public class MarketDetailIndexView extends LinearLayout {

    private boolean mSimple;// true 简单 false 详细
    private View vChangeBtn;
    private View vRooView;

    public MarketDetailIndexView(Context context) {
        this(context, null);
    }

    public MarketDetailIndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarketDetailIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
//        setLayoutTransition(new LayoutTransition());
        setSimpleView();
    }

    private void changeView() {
        if (!mSimple) {
            setSimpleView();
        } else {
            setDetailedView();
        }
    }

    private void setDetailedView() {
        mSimple = false;
        removeView(getChildAt(0));
        inflate(getContext(), R.layout.view_market_detail_index_detailed, this);
        vChangeBtn = findViewById(R.id.retract_btn);
        vChangeBtn.setOnClickListener(view -> changeView());
    }

    private void setSimpleView() {
        mSimple = true;
        removeView(getChildAt(0));
        inflate(getContext(), R.layout.view_market_detail_index_simple, this);
        vChangeBtn = findViewById(R.id.open_btn);
        vChangeBtn.setOnClickListener(view -> changeView());
    }
}
