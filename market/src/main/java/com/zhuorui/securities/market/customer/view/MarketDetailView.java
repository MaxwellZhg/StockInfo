package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.Group;
import com.zhuorui.securities.market.R;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-11 18:18
 * desc   : 股票详情价格详细数据View
 */
public class MarketDetailView extends FrameLayout {

    private TextView vMoreBtn;
    private Group vMoreGroup;

    public MarketDetailView(Context context) {
        this(context, null);
    }

    public MarketDetailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarketDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_market_detail, this);
        vMoreBtn = findViewById(R.id.more_btn);
        vMoreGroup = findViewById(R.id.more_group);
        vMoreBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMore();
            }
        });
    }

    private void changeMore() {
        if (vMoreGroup.getVisibility() == VISIBLE) {
            vMoreBtn.setText("查看更多");
            vMoreGroup.setVisibility(GONE);
            vMoreBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_arrow_down_53a0fd,0,0,0);
        } else {
            vMoreBtn.setText("收起");
            vMoreGroup.setVisibility(VISIBLE);
            vMoreBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_arrow_up_53a0fd,0,0,0);
        }
    }
}
