package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.TransitionManager;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.securities.base2app.util.TimeZoneUtil;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.customer.CapitalFlowNumPopWindow;
import com.zhuorui.securities.market.customer.view.kline.charts.DelayedChartGestureListener;
import com.zhuorui.securities.market.customer.view.kline.charts.HighlightLineChart;
import com.zhuorui.securities.market.customer.view.kline.renderer.HighlightLineChartRenderer;
import com.zhuorui.securities.market.model.CapitalTrendModel;
import com.zhuorui.securities.market.util.MarketUtil;
import com.zhuorui.securities.market.util.MathUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-17 11:43
 * desc   : 历史资金流向
 */
public class HistoricalCapitalFlowView extends FrameLayout implements View.OnClickListener, CapitalFlowNumPopWindow.OnSelectCallBack, OnChartValueSelectedListener {

    private final int upColor = Color.parseColor("#D9001B");
    private final int downColor = Color.parseColor("#00AB3B");
    private final int defColor = Color.parseColor("#7B889E");
    private final int mLineColor = Color.parseColor("#FF8E1B");
    private int mDateNum = 5;
    private HighlightContentView vHighlightContent;
    private CombinedChart vChart;
    private TextView vNum;
    private TextView vTotal;
    private TextView vUnit;
    private OnSelectDayListener mListener;
    private BigDecimal mUnit;
    private float mPrice;
    private long mHighlightTime;
    private float mHighlightValue;
    private String[] mHighlightContentTitle;

    public HistoricalCapitalFlowView(Context context) {
        this(context, null);
    }

    public HistoricalCapitalFlowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoricalCapitalFlowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHighlightContentTitle = context.getResources().getStringArray(R.array.TodayCapitalFlowTrendHighlightTitle);
        inflate(context, R.layout.view_historical_capital_flow, this);
        vHighlightContent = findViewById(R.id.highlight_content);
        vTotal = findViewById(R.id.tv_total);
        vUnit = findViewById(R.id.tv_unit);
        vNum = findViewById(R.id.tv_date_num);
        vNum.setOnClickListener(this);
        initBarChart();
        upDateNumText();
    }

    private void initBarChart() {
        vChart = findViewById(R.id.bar_cahart);
        vChart.setNoDataText(getResources().getString(R.string.str_no_data));
        vChart.setNoDataTextColor(Color.parseColor("#C3CDE3"));
        vChart.setDrawGridBackground(false);//是否展示网格线
        vChart.setDragEnabled(true); //是否可以拖动
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
        xAxis.setTextColor(defColor);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                //此处的value 可以看作 index
                CombinedData data = vChart.getData();
                Entry entry = null;
                if (data.getLineData() != null) {
                    entry = data.getLineData().getDataSetByIndex(0).getEntryForIndex((int) value);
                } else if (data.getBarData() != null) {
                    entry = data.getBarData().getDataSetByIndex(0).getEntryForIndex((int) value);
                }
                if (entry != null) {
                    Long time = (Long) entry.getData();
                    return TimeZoneUtil.timeFormat(time, "MM-dd");
                }
                return "";
            }
        });
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

        vChart.setOnChartGestureListener(new DelayedChartGestureListener(vChart));
        vChart.setOnChartValueSelectedListener(this);
    }

    public void setData(List<CapitalTrendModel> data) {
        if (data == null || data.isEmpty()) {
            vChart.setData(null);
            vChart.initRenderer();
            vUnit.setText(String.format(getResources().getString(R.string.unit_yuan), ""));
            vTotal.setText(getTotalSpannableString(null));
            return;
        }
        if (mDateNum > 5) {
            List<Entry> entryList = new ArrayList<>();
            List<BigDecimal> values = new ArrayList<>();
            BigDecimal total = BigDecimal.valueOf(0);
            for (int i = 0; i < mDateNum; i++) {
                BigDecimal value = data.get(i).getValue();
                values.add(value);
                Entry entry = new Entry(i, value.floatValue(), data.get(i).getTime());
                entryList.add(entry);
                total = MathUtil.INSTANCE.add2(total, value);
            }
            vTotal.setText(getTotalSpannableString(total));
            mUnit = MarketUtil.getUnitBigDecimal(values);
            vUnit.setText(String.format(getResources().getString(R.string.unit_yuan), MarketUtil.getUnitName(mUnit)));
            setLineData(entryList);
        } else {
            List<Integer> color = new ArrayList<>();
            List<BarEntry> entryList = new ArrayList<>();
            List<BigDecimal> values = new ArrayList<>();
            for (int i = 0; i < mDateNum; i++) {
                BigDecimal value = data.get(i).getValue();
                values.add(value);
                float v = value.floatValue();
                BarEntry barEntry = new BarEntry(i, v, data.get(i).getTime());
                entryList.add(barEntry);
                color.add(v < 0 ? downColor : v > 0 ? upColor : defColor);
            }
            mUnit = MarketUtil.getUnitBigDecimal(values);
            vUnit.setText(String.format(getResources().getString(R.string.unit_yuan), MarketUtil.getUnitName(mUnit)));
            setBarData(entryList, color);
        }


    }

    private SpannableString getTotalSpannableString(BigDecimal total) {
        int compare = 0;
        String totalStr = "--";
        if (total != null) {
            compare = total.compareTo(BigDecimal.valueOf(0));
            totalStr = String.format("%+.2f", total.doubleValue());
        }
        int totalColor = compare == -1 ? downColor : compare == 1 ? upColor : defColor;
        String text = String.format(getResources().getString(R.string.historical_net_inflow), vNum.getText().toString(), totalStr);
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(totalColor), text.indexOf(totalStr), ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


    public void setPrice(float price) {
        mPrice = price;
        if (vHighlightContent.getVisibility() == VISIBLE) {
            setHighlightData(mHighlightTime, mHighlightValue, mPrice);
        }
    }

    public void setNotText(String text) {
        vChart.setNoDataText(text);
        vChart.invalidate();
    }

    public void setOnSelectDayListener(OnSelectDayListener l) {
        mListener = l;
    }

    private void setBarData(List<BarEntry> entryList, List<Integer> color) {
        BarDataSet barDataSet = new BarDataSet(entryList, "");
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
                return String.format("%+.1f", MathUtil.INSTANCE.divide2(BigDecimal.valueOf(value), mUnit));
            }
        });
        barDataSet.setHighlightEnabled(false);
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

    private void setLineData(List<Entry> entryList) {
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
        dataSet.setHighlightEnabled(true);
        dataSet.setHighLightColor(getResources().getColor(R.color.high_light_color));
        dataSet.setHighlightLineWidth(HighlightLineChart.HIGHLIGHT_LINE_WIDTH);
        dataSet.setCircleColor(mLineColor);
        dataSet.setCircleRadius(HighlightLineChart.HIGHLIGHT_CIRCLE_RADIUS);
        dataSet.setDrawCircleHole(false);
        dataSet.setColor(mLineColor);
        LineData barData = new LineData(dataSet);
        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        vChart.setData(combinedData);
        //Highlight功能使用自定义HighlightLineChartRenderer ，必须在setData 后设置
        List<DataRenderer> renders = new ArrayList<>();
        renders.add(new HighlightLineChartRenderer(vChart, vChart.getAnimator(), vChart.getViewPortHandler()));
        ((CombinedChartRenderer) vChart.getRenderer()).setSubRenderers(renders);
        vChart.getRenderer().initBuffers();
        vChart.invalidate();
    }

    private void upDateNumText() {
        vNum.setText(String.format(getResources().getString(R.string.x_day), String.valueOf(mDateNum)));
        if (mDateNum > 5) {
            vChart.setTouchEnabled(true);
            vTotal.setVisibility(VISIBLE);
            vTotal.setText(getTotalSpannableString(null));
        } else {
            vChart.setTouchEnabled(false);
            vTotal.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == vNum) {
            CapitalFlowNumPopWindow.Companion.create(getContext(), mDateNum, this).showAsDropDown(vNum);
        }
    }

    @Override
    public void onSelected(int num) {
        mDateNum = num;
        vChart.highlightValue(null,true);
        upDateNumText();
        setData(null);
        if (mListener != null) mListener.onSelected(num);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Object obj = e.getData();
        if (obj != null) {
            vHighlightContent.setVisibility(VISIBLE);
            changeHighlightLayout(e.getX());
            mHighlightTime = (long) obj;
            mHighlightValue = e.getY();
            setHighlightData(mHighlightTime, mHighlightValue, mPrice);
        }
    }

    /**
     * 设置Highlight数据
     *
     * @param time
     * @param value
     * @param price
     */
    private void setHighlightData(long time, float value, float price) {
        LinkedHashMap<CharSequence, CharSequence> data = new LinkedHashMap<>();
        data.put(mHighlightContentTitle[0], TimeZoneUtil.timeFormat(time, "yyyy/MM/dd"));
        data.put(mHighlightContentTitle[1], String.format("%.3f", price));
        int color = LocalSettingsConfig.Companion.getInstance().getUpDownColor(value, 0f, Color.WHITE);
        SpannableString ss = new SpannableString(String.format("%+.2f", MathUtil.INSTANCE.divide2(BigDecimal.valueOf(value), mUnit).doubleValue()));
        ss.setSpan(new ForegroundColorSpan(color), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        data.put(mHighlightContentTitle[2], ss);
        vHighlightContent.setData(data);
    }

    private int mHighlightSide = -1;

    /**
     * 改变Highlight位置
     *
     * @param x
     */
    private void changeHighlightLayout(float x) {
        final float max = vChart.getXAxis().getAxisMaximum();
        final float min = vChart.getXAxis().getAxisMinimum();
        double range = Math.abs(max - min);
        float contentX = (float) (max - (range / 2f));
        int side = x > contentX ? ConstraintSet.LEFT : ConstraintSet.RIGHT;
        if (mHighlightSide != side) {
            MarketUtil.changeHighlightLayout(vHighlightContent, vChart, side);
            mHighlightSide = side;
        }
    }

    @Override
    public void onNothingSelected() {
        vHighlightContent.setVisibility(GONE);
    }

    public interface OnSelectDayListener {
        void onSelected(int day);
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
