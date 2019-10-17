package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zhuorui.securities.market.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-16 09:53
 * desc   :
 */
public class TodayCapitalFlowTrendView extends FrameLayout {

    private final int mGridColor = Color.parseColor("#191821");
    private final int mTextColor = Color.parseColor("#7B889E");
    private int maxX;//最大数据点1分钟占一点
    private int breakIndex;//休盘点坐标
    private String openingHours;//开市时间
    private String breakTime;//休盘时间
    private String closingHours;//收市时间

    private LineChart vChart;

    public TodayCapitalFlowTrendView(Context context) {
        this(context, null);
    }

    public TodayCapitalFlowTrendView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TodayCapitalFlowTrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_today_capital_flow_trend, this);
        initLineChart();
        setStockTs("HK");
        setData();
    }

    private void initLineChart() {
        vChart = findViewById(R.id.line_cahart);
        vChart.setNoDataText("暂无数据");

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
//        vChart.setPinchZoom(true);
        vChart.getLegend().setEnabled(false);
        vChart.getDescription().setEnabled(false);
        vChart.setMinOffset(0f);
        vChart.setExtraTopOffset(1f);
        vChart.setExtraBottomOffset(18f);
        vChart.setExtraLeftOffset(1f);
        vChart.setExtraRightOffset(1f);

        // x坐标轴
        XAxis xl = vChart.getXAxis();
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(true);
        xl.setTextColor(mTextColor);
        xl.setGridColor(mGridColor);
        xl.setGridLineWidth(1f);
        xl.setLabelCount(2, true);
        // 将X坐标轴放置在底部，默认是在顶部。
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setAvoidFirstLastClipping(true);
        xl.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0) {
                    return openingHours;
                }
                if (value == maxX) {
                    return closingHours;
                }
                return super.getFormattedValue(value);
            }
        });
        vChart.getAxisRight().setEnabled(false);
        MyXAxisRenderer xAxisRenderer = new MyXAxisRenderer(vChart.getViewPortHandler(), vChart.getXAxis(), vChart.getTransformer(YAxis.AxisDependency.LEFT));
        vChart.setXAxisRenderer(xAxisRenderer);

        // 图表左边的y坐标轴线
        YAxis leftAxis = vChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(true);
//        leftAxis.setDrawZeroLine(true);
//        leftAxis.setZeroLineColor(mGridColor);
        leftAxis.setTextColor(mTextColor);
        leftAxis.setGridColor(mGridColor);
        leftAxis.setGridLineWidth(1f);
        leftAxis.setXOffset(0f);
        leftAxis.setSpaceBottom(0f);
        leftAxis.setSpaceTop(0f);
        leftAxis.setLabelCount(3, true);
        leftAxis.setValueLineInside(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
    }

    private LineData getLineData(List<Entry> entrys) {
        LineDataSet lineDataSet = new LineDataSet(entrys, "");
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setLineWidth(0.5f);
        lineDataSet.setColor(Color.parseColor("#FF8E1B"));
        LineData lineData = new LineData(lineDataSet);
        lineData.setValueTextColor(Color.WHITE);
        return lineData;
    }


    private void setData() {
        List<Entry> entrys = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 149; i += 4) {
            Entry entry = new Entry(i, random.nextFloat());
            entrys.add(entry);
        }
        Entry entry = new Entry(149, 0);
        entrys.add(entry);
        vChart.setData(getLineData(entrys));
    }

    public void setStockTs(String ts) {
        switch (ts) {
            case "HK":
                maxX = 330;//最大数据点1分钟占一点
                breakIndex = 149;//休盘点坐标
                openingHours = "09:30";//开市时间
                breakTime = "12:00/13:00";//休盘时间
                closingHours = "16:00";//收市时间
                break;
            case "US":
                maxX = 390;//最大数据点1分钟占一点
                breakIndex = 194;//中间线坐标
                openingHours = "09:30";//开市时间
                breakTime = "12:45";//中间线
                closingHours = "16:00";//收市时间
                break;
            case "SZ":
            case "SH":
                maxX = 240;//最大数据点1分钟占一点
                breakIndex = 119;//休盘点坐标
                openingHours = "09:30";//开市时间
                breakTime = "1:30/13:00";//休盘时间
                closingHours = "15:00";//收市时间
                break;
        }
        vChart.getXAxis().setAxisMaximum(maxX);
    }

    class MyXAxisRenderer extends XAxisRenderer {


        public MyXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
            super(viewPortHandler, xAxis, trans);
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
         * 绘制休市分割线
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
         * 绘制休市分割Label
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

    }
}
