package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.zhuorui.securities.market.R;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-16 09:53
 * desc   :
 */
public class TodayCapitalFlowTrendView extends FrameLayout {

    private LineChart vLineChart;

    public TodayCapitalFlowTrendView(Context context) {
        this(context,null);
    }

    public TodayCapitalFlowTrendView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TodayCapitalFlowTrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_today_capital_flow_trend,this);
        initLineChart();
    }

    private void initLineChart() {
        vLineChart = findViewById(R.id.line_cahart);
    }
}
