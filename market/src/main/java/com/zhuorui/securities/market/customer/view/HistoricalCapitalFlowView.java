package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zhuorui.securities.market.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-17 11:43
 * desc   : 历史资金流向
 */
public class HistoricalCapitalFlowView extends FrameLayout {

    private final int upColor = Color.parseColor("#D9001B");
    private final int downColor = Color.parseColor("#00AB3B");
    private final float testSizePx = Utils.convertDpToPixel(10f);
    private BarChart vChart;

    public HistoricalCapitalFlowView(Context context) {
        this(context, null);
    }

    public HistoricalCapitalFlowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoricalCapitalFlowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_historical_capital_flow, this);
        initBarChart();
        getTestData();
    }

    private void initBarChart() {
        vChart = findViewById(R.id.bar_cahart);
        vChart.setNoDataText("暂无数据");
        vChart.setNoDataTextColor(Color.parseColor("#C3CDE3"));
        //是否有触摸事件
        vChart.setTouchEnabled(false);
        //是否展示网格线
        vChart.setDrawGridBackground(false);
        //是否显示边界
        vChart.setDrawBorders(false);
        //是否可以拖动
        vChart.setDragEnabled(false);
        // 可缩放
        vChart.setScaleEnabled(false);
        vChart.getDescription().setEnabled(false);
        vChart.getAxisRight().setEnabled(false);
        vChart.getLegend().setEnabled(false);
        vChart.setMinOffset(0f);
        vChart.setExtraBottomOffset(2f);

        /***XY轴的设置***/
        //X轴设置显示位置在底部
        XAxis xAxis = vChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setYOffset(8f);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.parseColor("#7B889E"));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis axisLeft = vChart.getAxisLeft();
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);
        axisLeft.setDrawZeroLine(true);
        axisLeft.setZeroLineColor(Color.parseColor("#333741"));
        axisLeft.setZeroLineWidth(1f);
        axisLeft.setSpaceTop(0f);
        axisLeft.setSpaceBottom(0f);
        axisLeft.setValueLineInside(true);
        axisLeft.setLabelCount(2, true);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });
        vChart.setRenderer(new HCFVRenderer(vChart, vChart.getAnimator(), vChart.getViewPortHandler()));
    }

    private void getTestData() {
        List<Integer> color = new ArrayList<>();
        List<BarEntry> entryList = new ArrayList<>();
        for (int i = 3; i >= 0; i--) {
            BarEntry barEntry = new BarEntry(i, i * 1000);
            entryList.add(barEntry);
            color.add(upColor);
        }
        BarEntry barEntry = new BarEntry(4, -2900);
        entryList.add(barEntry);
        color.add(downColor);
        setData(entryList, color);
    }

    private void setData(List<BarEntry> entryList, List<Integer> color) {
        BarDataSet barDataSet = new BarDataSet(entryList, "");
        barDataSet.setValueTextSize(10f);
        barDataSet.setColors(color);
        barDataSet.setValueTextColors(color);
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0) {
                    return "0.0";
                }
                return String.format("%+.1f", value);
            }
        });
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        vChart.getXAxis().setLabelCount(entryList.size());
        vChart.setData(barData);
    }

    class HCFVRenderer extends BarChartRenderer {

        private double zeroY = 0;//0 的y值
        private float space = 0;
        private float zeroLineWidth = 0;
        private float textSpace = 0;

        public HCFVRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
            super(chart, animator, viewPortHandler);
            space = Utils.convertDpToPixel(5f);
            zeroLineWidth = Utils.convertDpToPixel(1f);
            textSpace = Utils.convertDpToPixel(1f);
        }

        @Override
        public void drawValues(Canvas c) {
            MPPointD pos = mChart.getTransformer(YAxis.AxisDependency.LEFT).getPixelForValues(0f, 0f);
            zeroY = pos.y;
            super.drawValues(c);
        }

        @Override
        public void drawValue(Canvas c, String valueText, float x, float y, int color) {
            if (y > zeroY) {
                y = (float) (zeroY - space - zeroLineWidth);
            } else {
                y = (float) zeroY + space + testSizePx - textSpace;
            }
            super.drawValue(c, valueText, x, y, color);
        }
    }
}
