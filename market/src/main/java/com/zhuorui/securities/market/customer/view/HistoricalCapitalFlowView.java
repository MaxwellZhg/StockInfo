package com.zhuorui.securities.market.customer.view;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.customer.CapitalFlowNumPopWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-17 11:43
 * desc   : 历史资金流向
 */
public class HistoricalCapitalFlowView extends FrameLayout implements View.OnClickListener, CapitalFlowNumPopWindow.OnSelectCallBack {

    private final int upColor = Color.parseColor("#D9001B");
    private final int downColor = Color.parseColor("#00AB3B");
    private int mDateNum = 5;
    private CombinedChart vChart;
    private TextView vNum;
    private TextView vTotal;

    public HistoricalCapitalFlowView(Context context) {
        this(context, null);
    }

    public HistoricalCapitalFlowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoricalCapitalFlowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_historical_capital_flow, this);
        vTotal = findViewById(R.id.tv_total);
        vNum = findViewById(R.id.tv_date_num);
        vNum.setOnClickListener(this);
        initAnimator();
        initBarChart();
        getTestData();
    }

    private void initAnimator() {
        ((ViewGroup) findViewById(R.id.root_view)).setLayoutTransition(new LayoutTransition());
    }

    private void initBarChart() {
        vChart = findViewById(R.id.bar_cahart);
        vChart.setNoDataText("暂无数据");
        vChart.setNoDataTextColor(Color.parseColor("#C3CDE3"));
        vChart.setTouchEnabled(false);//是否有触摸事件
        vChart.setDrawGridBackground(false);//是否展示网格线
        vChart.setDragEnabled(false); //是否可以拖动
        vChart.setScaleEnabled(false);// 可缩放
        vChart.setBorderWidth(0.5f);
        vChart.setBorderColor(Color.parseColor("#337B889E"));
        vChart.getDescription().setEnabled(false);
        vChart.getAxisRight().setEnabled(false);
        vChart.getLegend().setEnabled(false);
        vChart.setMinOffset(0f);
        vChart.setExtraBottomOffset(2f);
        //X轴设置
        XAxis xAxis = vChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setYOffset(8f);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.parseColor("#7B889E"));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //Y轴设置
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
    }

    private void getTestData() {
        if (mDateNum > 5) {
            List<Entry> entryList = new ArrayList<>();
            int[] d = {-1, 1};
            Random random = new Random();
            float total = 0;
            for (int i = 0; i < mDateNum; i++) {
                int v = (i + 1) * random.nextInt(1000) * d[random.nextInt(d.length)];
                Entry entry = new Entry(i, v);
                entryList.add(entry);
                total += v;
            }
            setLineData(entryList, total);
        } else {
            List<Integer> color = new ArrayList<>();
            List<BarEntry> entryList = new ArrayList<>();
            int[] d = {-1, 1};
            Random random = new Random();
            for (int i = 0; i < mDateNum; i++) {
                int v = (i + 1) * random.nextInt(1000) * d[random.nextInt(d.length)];
                BarEntry barEntry = new BarEntry(i, v);
                entryList.add(barEntry);
                color.add(v < 0 ? downColor : upColor);
            }
            setBarData(entryList, color);
        }
    }

    private void setBarData(List<BarEntry> entryList, List<Integer> color) {
        BarDataSet barDataSet = new BarDataSet(entryList, "");
        vTotal.setVisibility(GONE);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) vChart.getLayoutParams();
        lp.leftMargin = (int) (getResources().getDisplayMetrics().density * 15);
        lp.rightMargin = lp.leftMargin;
        vChart.setLayoutParams(lp);
        vChart.setDrawBorders(false);
        XAxis xAxis = vChart.getXAxis();
        xAxis.setLabelCount(entryList.size());
        xAxis.setAvoidFirstLastClipping(false);
        //解决柱状图左右两边只显示了一半的问题
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum((float) (entryList.size() - 0.5));
        YAxis leftAxis = vChart.getAxisLeft();
        leftAxis.setDrawZeroLine(true);
        //数据上下留空间出显示Values
        float max = barDataSet.getYMax();
        float min = barDataSet.getYMin();
        double maxMinAbs = Math.abs(max - min);
        float maxMinAbs15 = (float) (maxMinAbs * 0.15);
        float maxMinAbs05 = (float) (maxMinAbs * 0.05);
        if (max < maxMinAbs15) {
            leftAxis.setAxisMaximum(maxMinAbs15);
            leftAxis.setAxisMinimum(min - maxMinAbs05);
        } else if (min > -maxMinAbs15) {
            leftAxis.setAxisMaximum(max + maxMinAbs05);
            leftAxis.setAxisMinimum(-maxMinAbs15);
        } else {
            leftAxis.setAxisMaximum(max + maxMinAbs05);
            leftAxis.setAxisMinimum(min - maxMinAbs05);
        }
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
        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        vChart.setData(combinedData);
        //柱形图使用自定义BarChartRenderer ，必须在setData 后设置
        List<DataRenderer> renders = new ArrayList<>();
        renders.add(new HCFVRenderer(vChart, vChart.getAnimator(), vChart.getViewPortHandler()));
        ((CombinedChartRenderer) vChart.getRenderer()).setSubRenderers(renders);
        vChart.getRenderer().initBuffers();
        vChart.invalidate();
    }

    private void setLineData(List<Entry> entryList, float total) {
        vTotal.setVisibility(VISIBLE);
        vTotal.setText(String.format("%1s天总净流入%+.2f", mDateNum, total));
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) vChart.getLayoutParams();
        lp.leftMargin = 0;
        lp.rightMargin = 0;
        vChart.setLayoutParams(lp);
        vChart.setData(null);
        vChart.setDrawBorders(true);
        XAxis xAxis = vChart.getXAxis();
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.resetAxisMaximum();
        xAxis.resetAxisMinimum();
        xAxis.setLabelCount(2, true);
        YAxis axisLeft = vChart.getAxisLeft();
        axisLeft.resetAxisMinimum();
        axisLeft.resetAxisMaximum();
        axisLeft.setDrawZeroLine(false);
        LineDataSet dataSet = new LineDataSet(entryList, "");
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(0.5f);
        dataSet.setColor(Color.parseColor("#FF8E1B"));
        LineData barData = new LineData(dataSet);
        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        vChart.setData(combinedData);
        vChart.invalidate();
    }

    private void upDateNumText() {
        vNum.setText(mDateNum + "天");
    }

    @Override
    public void onClick(View v) {
        if (v == vNum) {
            CapitalFlowNumPopWindow.Companion.create(getContext(),mDateNum,this).showAsDropDown(vNum);
        }
    }

    @Override
    public void onSelected(int num) {
        mDateNum = num;
        upDateNumText();
        getTestData();
    }

    class HCFVRenderer extends BarChartRenderer {

        private double zeroY = 0;//0 的y值
        private float space = 0;
        private float zeroLineWidth = 0;
        private float textSpace = 0;
        private final float testSizePx = Utils.convertDpToPixel(10f);

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
