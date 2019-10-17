package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zhuorui.securities.market.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/17
 * Desc:
 */
public class HistoryFundTransactionView extends FrameLayout {
    private boolean mSimple;// true 简单 false 详细
    private BarChart vBarChart;
    private TextView tv_date_select;


    public HistoryFundTransactionView(Context context) {
        this(context,null);
    }

    public HistoryFundTransactionView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HistoryFundTransactionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFiveDataView();
    }


    private void initBarChart() {
        vBarChart = findViewById(R.id.bar_hchart);
        vBarChart.setNoDataText("暂无数据");

        vBarChart.setBackgroundColor(Color.parseColor("#211F2A"));
        vBarChart.setExtraTopOffset(-30f);
        vBarChart.setExtraBottomOffset(10f);
        // chart.setExtraLeftOffset(54f);
        // chart.setExtraRightOffset(54f);

        vBarChart.setDrawBarShadow(false);
        vBarChart.setDrawValueAboveBar(true);

        vBarChart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        vBarChart.setPinchZoom(false);

        vBarChart.setDrawGridBackground(false);
        vBarChart.setScaleEnabled(false);
        XAxis xAxis = vBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(tfRegular);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.parseColor("#7B889E"));
        xAxis.setTextSize(13f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(6,true);
        xAxis.setGranularity(15f);

        YAxis left = vBarChart.getAxisLeft();
        left.setDrawLabels(false);
        // left.setSpaceTop(25f);
        // left.setSpaceBottom(25f);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(true); // draw a zero line
        left.setZeroLineColor(Color.parseColor("#333741"));
        left.setZeroLineWidth(0.7f);
        vBarChart.getAxisRight().setEnabled(false);
        vBarChart.getLegend().setEnabled(false);
        // THIS IS THE ORIGINAL DATA YOU WANT TO PLOT
        final List<Data> data = new ArrayList<>();
        data.add(new Data(0.5f, 2823.5f, "08-28"));
        data.add(new Data(1.5f, -1229f, "08-29"));
        data.add(new Data(2.5f, 9999.99f, "08-30"));
        data.add(new Data(3.5f, 3308.3f, "09-02"));
        data.add(new Data(4.5f, -9999.99f, "09-03"));

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                Log.e("ttttt-----","###---"+ value);
                Log.e("ttttt-----","###---"+Math.min(Math.max((int) value, 0), data.size()-1));
                return data.get(Math.min(Math.max((int) value, 0), data.size()-1)).xAxisValue;
            }
        });
        setData(data);
    }



    private void setData(List<Data> dataList) {

        ArrayList<BarEntry> values = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {

            Data d = dataList.get(i);
            BarEntry entry = new BarEntry(d.xValue, d.yValue);
            values.add(entry);

            // specific colors
            if (d.yValue >= 0) {
                colors.add(Color.parseColor("#D9001B"));
            }else {
                colors.add(Color.parseColor("#00AB3B"));
            }
        }

        BarDataSet set;

        if (vBarChart.getData() != null &&
                vBarChart.getData().getDataSetCount() > 0) {
            set = (BarDataSet) vBarChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            vBarChart.setData(vBarChart.getData());
            vBarChart.invalidate();
        } else {
            set = new BarDataSet(values, "Values");
            set.setColors(colors);
            set.setValueTextColors(colors);
            BarData data = new BarData(set);
            data.setValueTextSize(13f);
           // data.setValueTypeface(tfRegular);
            data.setValueFormatter(new Formatter());
            data.setBarWidth(0.5f);

            vBarChart.setData(data);
            vBarChart.invalidate();
        }
    }



    /**
     * Demo class representing data.
     */
    private class Data {

        final String xAxisValue;
        final float yValue;
        final float xValue;

        Data(float xValue, float yValue, String xAxisValue) {
            this.xAxisValue = xAxisValue;
            this.yValue = yValue;
            this.xValue = xValue;
        }
    }

    private class Formatter extends ValueFormatter
    {

        private final DecimalFormat mFormat;

        Formatter() {
            mFormat = new DecimalFormat("######.00");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value);
        }
    }

    private void setMutiDayView() {
        mSimple = false;
        removeAllViews();
        inflate(getContext(), R.layout.view_history_muti_day_fund_transaction, this);
        tv_date_select = findViewById(R.id.tv_date_select);
        tv_date_select.setOnClickListener(view -> changeView());
    }

    private void changeView() {
        if (!mSimple) {
            setFiveDataView();
            tv_date_select.setText("5天");
        } else {
            setMutiDayView();
            tv_date_select.setText("10天");
        }
    }

    private void setFiveDataView(){
        mSimple = true;
        removeAllViews();
        inflate(getContext(), R.layout.view_history_fund_transaction, this);
        initBarChart();
        tv_date_select = findViewById(R.id.tv_date_select);
        tv_date_select.setOnClickListener(view -> changeView());
    }


}
