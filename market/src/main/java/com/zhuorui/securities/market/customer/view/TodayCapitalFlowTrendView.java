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
        setData();
    }

    private void setData() {
        List<Entry> entrys = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            Entry entry = new Entry(i, random.nextFloat());
            entrys.add(entry);
        }

        vChart.setData(getLineData(entrys));
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
        leftAxis.setLabelCount(3, true);
        leftAxis.setValueLineInside(true);

        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

    }

    class XValueFormatter extends ValueFormatter {

    }

    class MyXAxisRenderer extends XAxisRenderer {

        int size = 20;
        float x = 9;
        String label = "12:00/13:00";
        float intervalX = 0;

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

        private void drawCustomGridLine(Canvas c) {
            int clipRestoreCount = c.save();
            c.clipRect(getGridClippingRect());
            Path gridLinePath = mRenderGridLinesPath;
            gridLinePath.reset();
            float[] positions = {x,0};
            mTrans.pointValuesToPixel(positions);
            drawGridLine(c, positions[0], positions[0], gridLinePath);
            c.restoreToCount(clipRestoreCount);
        }

        private void drawCustomLabel(Canvas c, float pos, MPPointF anchor) {
//            if (mViewPortHandler.isInBoundsX(x)) {
            float[] positions = {x,0};
            mTrans.pointValuesToPixel(positions);
            drawLabel(c, label, positions[0], pos, anchor, mXAxis.getLabelRotationAngle());
//            }
        }

    }
}
