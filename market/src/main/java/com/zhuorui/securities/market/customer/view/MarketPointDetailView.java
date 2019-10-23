package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.zhuorui.securities.market.R;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/23
 * Desc:
 */
public class MarketPointDetailView extends FrameLayout {
    public MarketPointDetailView(Context context) {
        this(context,null);
    }

    public MarketPointDetailView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MarketPointDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_market_point_detail_view,this);
    }
}
