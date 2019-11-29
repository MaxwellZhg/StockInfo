package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.TransitionManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.*;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.securities.base2app.util.TimeZoneUtil;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.customer.view.kline.charts.CoupleChartGestureListener;
import com.zhuorui.securities.market.customer.view.kline.charts.DelayedChartGestureListener;
import com.zhuorui.securities.market.customer.view.kline.charts.HighlightLineChart;
import com.zhuorui.securities.market.model.CapitalTrendModel;
import com.zhuorui.securities.market.util.MarketUtil;
import com.zhuorui.securities.market.util.MathUtil;

import java.math.BigDecimal;
import java.util.*;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-16 09:53
 * desc   : 今日资金流向趋势
 */
public class TodayCapitalFlowTrendView extends FrameLayout implements OnChartValueSelectedListener {

    private final int mGridColor = Color.parseColor("#337B889E");
    private final int mTextColor = Color.parseColor("#7B889E");
    private final int mLineColor = Color.parseColor("#FF8E1B");
    private boolean mEmpty;
    private LineChart vChart;
    private MyXAxisRenderer xAxisRenderer;
    private TextView vUnit;
    private BigDecimal mUnit;
    private HighlightContentView vHighlightContent;
    private float mPrice;
    private long mHighlightTime;
    private float mHighlightValue;
    private String[] mHighlightContentTitle;

    public TodayCapitalFlowTrendView(Context context) {
        this(context, null);
    }

    public TodayCapitalFlowTrendView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TodayCapitalFlowTrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHighlightContentTitle = context.getResources().getStringArray(R.array.TodayCapitalFlowTrendHighlightTitle);
        inflate(context, R.layout.view_today_capital_flow_trend, this);
        vUnit = findViewById(R.id.tv_precen);
        vHighlightContent = findViewById(R.id.highlight_content);
        initLineChart();
    }

    private void initLineChart() {
        vChart = findViewById(R.id.line_cahart);
        vChart.setNoDataText(getResources().getString(R.string.str_no_data));
        vChart.setNoDataTextColor(getResources().getColor(R.color.color_no_data_text));
        //是否展示网格线
        vChart.setDrawGridBackground(false);
        //是否显示边界
        vChart.setDrawBorders(false);
        //是否可以拖动
        vChart.setDragEnabled(true);
        // 可缩放
        vChart.setScaleEnabled(false);
//        vChart.setPinchZoom(true);
        vChart.getLegend().setEnabled(false);
        vChart.getDescription().setEnabled(false);
        vChart.setMinOffset(0f);
        vChart.setExtraTopOffset(0.5f);
        vChart.setExtraLeftOffset(0.5f);
        vChart.setExtraRightOffset(0.5f);
        vChart.setExtraBottomOffset(2f);
        // x坐标轴
        XAxis xl = vChart.getXAxis();
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(true);
        xl.setTextColor(mTextColor);
        xl.setGridColor(mGridColor);
        xl.setTextSize(12f);
        xl.setGridLineWidth(0.5f);
        xl.setLabelCount(2, true);
        // 将X坐标轴放置在底部，默认是在顶部。
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setAvoidFirstLastClipping(true);
        vChart.getAxisRight().setEnabled(false);
        xAxisRenderer = new MyXAxisRenderer(vChart.getViewPortHandler(), vChart.getXAxis(), vChart.getTransformer(YAxis.AxisDependency.LEFT));
        vChart.setXAxisRenderer(xAxisRenderer);

        // 图表左边的y坐标轴线
        YAxis leftAxis = vChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextColor(mTextColor);
        leftAxis.setGridColor(mGridColor);
        leftAxis.setTextSize(12f);
        leftAxis.setGridLineWidth(0.5f);
        // 设置文字偏移量
        leftAxis.setXOffset(5f);
        leftAxis.setEdgeYOffset(5f);
        leftAxis.setSpaceBottom(0f);
        leftAxis.setSpaceTop(0f);
        leftAxis.setLabelCount(5, true);
        leftAxis.setValueLineInside(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value == 0 || mEmpty ? "0" : MathUtil.INSTANCE.convertToUnitString(BigDecimal.valueOf(value), "%.2f");
            }
        });
//        MyYAxisRenderer yAxisRenderer = new MyYAxisRenderer(vChart.getViewPortHandler(), vChart.getAxisLeft(), vChart.getTransformer(YAxis.AxisDependency.LEFT));
//        vChart.setRendererLeftYAxis(yAxisRenderer);

        vChart.setOnChartGestureListener(new DelayedChartGestureListener(vChart));
        vChart.setOnChartValueSelectedListener(this);
    }

    private LineData getLineData(List<Entry> entrys) {
        mEmpty = entrys.isEmpty();
        LineDataSet lineDataSet;
        if (mEmpty) {
            vChart.setTouchEnabled(false);
            entrys.add(new Entry(0, 1));
            entrys.add(new Entry(0, -1));
            lineDataSet = new LineDataSet(entrys, "");
            lineDataSet.setColor(Color.TRANSPARENT);
        } else {
            vChart.setTouchEnabled(true);
            lineDataSet = new LineDataSet(entrys, "");
            lineDataSet.setColor(mLineColor);
        }
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setLineWidth(0.5f);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setHighLightColor(getResources().getColor(R.color.high_light_color));
        lineDataSet.setHighlightLineWidth(HighlightLineChart.HIGHLIGHT_LINE_WIDTH);
        lineDataSet.setCircleColor(mLineColor);
        lineDataSet.setCircleRadius(HighlightLineChart.HIGHLIGHT_CIRCLE_RADIUS);
        lineDataSet.setDrawCircleHole(false);
        LineData lineData = new LineData(lineDataSet);
        lineData.setValueTextColor(Color.WHITE);
        return lineData;
    }

    public void setData(String ts, List<CapitalTrendModel> datas) {
        setStockTs(ts);
        LineData lineData = getLineData(getEntry(datas));
        vChart.getAxisLeft().resetAxisMaximum();
        vChart.getAxisLeft().resetAxisMinimum();
        if (lineData.getYMin() == lineData.getYMax() && lineData.getYMin() > 0) {
            vChart.getAxisLeft().setAxisMinimum(0f);
        } else if (lineData.getYMin() == lineData.getYMax() && lineData.getYMax() < 0) {
            vChart.getAxisLeft().setAxisMaximum(0f);
        }
        vChart.setData(lineData);
        vChart.invalidate();
    }

    public void setPrice(float price) {
        mPrice = price;
        if (vHighlightContent.getVisibility() == VISIBLE) {
            setHighlightData(mHighlightTime, mHighlightValue, mPrice);
        }
    }

    public void setNotText(String text){
        vChart.setNoDataText(text);
        vChart.invalidate();
    }

    private List<Entry> getEntry(List<CapitalTrendModel> datas) {
        List<Entry> entrys = new ArrayList<>();
        if (datas != null && !datas.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                datas.sort((o1, o2) -> o1.getTime() > o2.getTime() ? 1 : (o1.getTime() < o2.getTime() ? -1 : 0));
            }
            long dateM = TimeZoneUtil.parseTime(TimeZoneUtil.timeFormat(datas.get(0).getTime(), "yyyyMMdd"), "yyyyMMdd");
            long openingMillisecond = xAxisRenderer.getOpeningMillisecond();
            long breakMillisecond = xAxisRenderer.getBreakMillisecond();
            long breakEndMillisecond = breakMillisecond + xAxisRenderer.getBreakDuration();
            List<BigDecimal> values = new ArrayList<>();
            for (int i = 0, len = datas.size(); i < len; i++) {
                CapitalTrendModel data = datas.get(i);
                float x = getXByTime(dateM, data.getTime(), openingMillisecond, breakMillisecond, breakEndMillisecond);
                if (x > -1) {
                    values.add(data.getValue());
                    entrys.add(new Entry(x, data.getValue().floatValue(), data.getTime()));
                }
            }
            mUnit = MarketUtil.getUnitBigDecimal(values);
            vUnit.setText(String.format(getResources().getString(R.string.unit_yuan), MarketUtil.getUnitName(mUnit)));
        } else {
            vUnit.setText(String.format(getResources().getString(R.string.unit_yuan), ""));
        }
        return entrys;
    }

    private int getXByTime(long date, long time, long openingMillisecond, long breakMillisecond, long breakEndMillisecond) {
        int x = -1;
        long cur = time - date;
        if (cur >= breakEndMillisecond) {
            x = (int) (((breakMillisecond - openingMillisecond) + (cur - breakEndMillisecond)) / 60000);
        } else if (cur <= breakMillisecond) {
            x = (int) ((cur - openingMillisecond) / 60000);
        }
        return x;
    }

    /**
     * 设置股票市场
     *
     * @param ts
     */
    public void setStockTs(String ts) {
        ((MyXAxisRenderer) vChart.getRendererXAxis()).setStockTs(ts);
        vChart.invalidate();
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

    /**
     * 设置Highlight数据
     *
     * @param time
     * @param value
     * @param price
     */
    private void setHighlightData(long time, float value, float price) {
        LinkedHashMap<CharSequence, CharSequence> data = new LinkedHashMap<>();
        data.put(mHighlightContentTitle[0], TimeZoneUtil.timeFormat(time, "HH:mm"));
        data.put(mHighlightContentTitle[1], String.format("%.3f", price));
        int color = LocalSettingsConfig.Companion.getInstance().getUpDownColor(value, 0f, Color.WHITE);
        SpannableString ss = new SpannableString(String.format("%+.2f", MathUtil.INSTANCE.divide2(BigDecimal.valueOf(value), mUnit).doubleValue()));
        ss.setSpan(new ForegroundColorSpan(color), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        data.put(mHighlightContentTitle[2], ss);
        vHighlightContent.setData(data);
    }

    @Override
    public void onNothingSelected() {
        vHighlightContent.setVisibility(GONE);
    }

    class MyXAxisRenderer extends XAxisRenderer {

        private int maxX;//最大数据点1分钟占一点
        private int breakIndex;//休盘点坐标
        private String openingHours;//开市时间
        private String breakTime;//休盘时间
        private String closingHours;//收市时间
        private long openingMillisecond;//开市时间毫秒
        private long breakMillisecond;//休盘时间毫秒
        private long breakDuration;//休盘时长

        public MyXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
            super(viewPortHandler, xAxis, trans);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    if (value == 0) {
                        return openingHours;
                    } else if (value == maxX) {
                        return closingHours;
                    }
                    return super.getFormattedValue(value);
                }
            });
        }

        public void setStockTs(String ts) {
            switch (ts) {
                case "HK":
                    maxX = 330;//最大数据点1分钟占一点
                    breakIndex = 150;//休盘点坐标
                    openingHours = "09:30";//开市时间
                    breakTime = "12:00/13:00";//休盘时间
                    closingHours = "16:00";//收市时间
                    openingMillisecond = 34200000;
                    breakMillisecond = 43200000;
                    breakDuration = 3600000;
                    break;
                case "US":
                    maxX = 390;//最大数据点1分钟占一点
                    breakIndex = 195;//中间线坐标
                    openingHours = "09:30";//开市时间
                    breakTime = "12:45";//中间线
                    closingHours = "16:00";//收市时间
                    openingMillisecond = 34200000;
                    breakMillisecond = 45900000;
                    breakDuration = 0;
                    break;
                case "SZ":
                case "SH":
                    maxX = 240;//最大数据点1分钟占一点
                    breakIndex = 120;//休盘点坐标
                    openingHours = "09:30";//开市时间
                    breakTime = "11:30/13:00";//休盘时间
                    closingHours = "15:00";//收市时间
                    openingMillisecond = 34200000;
                    breakMillisecond = 41400000;
                    breakDuration = 5400000;
                    break;
            }
            mXAxis.setAxisMaximum(maxX);
            mXAxis.setAxisMinimum(0);
        }

        @Override
        protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
            super.drawLabels(c, pos, anchor);
            drawCustomLabel(c, pos, anchor);
        }

        @Override
        public void renderGridLines(Canvas c) {
            super.renderGridLines(c);
            drawCustomGridLine(c);
        }

        /**
         * 绘制休盘分割线
         *
         * @param c
         */
        private void drawCustomGridLine(Canvas c) {
            int clipRestoreCount = c.save();
            c.clipRect(getGridClippingRect());
            Path gridLinePath = mRenderGridLinesPath;
            gridLinePath.reset();
            int index = breakIndex * 2;
            float[] positions = new float[maxX * 2];
            positions[index] = breakIndex;
            mTrans.pointValuesToPixel(positions);
            drawGridLine(c, positions[index], positions[index], gridLinePath);
            c.restoreToCount(clipRestoreCount);
        }

        /**
         * 绘制休盘分割Label
         *
         * @param c
         * @param pos
         * @param anchor
         */
        private void drawCustomLabel(Canvas c, float pos, MPPointF anchor) {
            float[] positions = new float[maxX * 2];
            int index = breakIndex * 2;
            positions[index] = breakIndex;
            mTrans.pointValuesToPixel(positions);
            drawLabel(c, breakTime, positions[index], pos, anchor, mXAxis.getLabelRotationAngle());
        }

        public long getOpeningMillisecond() {
            return openingMillisecond;
        }

        public long getBreakMillisecond() {
            return breakMillisecond;
        }

        public long getBreakDuration() {
            return breakDuration;
        }
    }

    //    class MyYAxisRenderer extends YAxisRenderer {
//
//        private float dividerY;
//        private float dividerValue;
//
//        public MyYAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
//            super(viewPortHandler, yAxis, trans);
//        }
//
//        @Override
//        public void computeAxis(float min, float max, boolean inverted) {
//            super.computeAxis(min, max, inverted);
//            if (max > 0 && min < 0) {
//                dividerValue = 0;
//            } else {
//                double range = Math.abs(max - min);
//                dividerValue = (float) (max - (range / 2));
//            }
//            MPPointD pos = mTrans.getPixelForValues(0f, dividerValue);
//            dividerY = (float) pos.y;
//        }
//
//        @Override
//        protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
//            super.drawYLabels(c, fixedPosition, positions, offset);
//            c.drawText(mYAxis.getValueFormatter().getFormattedValue(dividerValue), fixedPosition, dividerY + Utils.convertDpToPixel(0.7f) + offset, mAxisLabelPaint);
//        }
//
//        @Override
//        public void renderGridLines(Canvas c) {
//            super.renderGridLines(c);
//            int clipRestoreCount = c.save();
//            c.clipRect(getGridClippingRect());
//            Path gridLinePath = mRenderGridLinesPath;
//            gridLinePath.reset();
//            gridLinePath.moveTo(mViewPortHandler.offsetLeft(), dividerY);
//            gridLinePath.lineTo(mViewPortHandler.contentRight(), dividerY);
//            c.drawPath(gridLinePath, mGridPaint);
//            gridLinePath.reset();
//            c.restoreToCount(clipRestoreCount);
//        }
//    }
}
