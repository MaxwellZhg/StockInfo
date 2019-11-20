package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.*;
import com.zhuorui.securities.base2app.util.ResUtil;
import com.zhuorui.securities.market.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/28
 * Desc:
 */
public class ChinaHKFundTransactionView extends FrameLayout {
    private final int mGridColor = Color.parseColor("#337B889E");
    private final int mTextColor = Color.parseColor("#7B889E");
    private boolean isSelect=false;
    private boolean mEmpty;
    private LineChart vChart;
    private TextView tv_north_trend;
    private TextView tv_sourth_trend;

    public ChinaHKFundTransactionView(Context context) {
        this(context,null);
    }

    public ChinaHKFundTransactionView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ChinaHKFundTransactionView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_china_fund_transaction, this);
        initLineChart();
        setStockTs("SZ");
        setData();
        tv_north_trend = findViewById(R.id.tv_north_trend);
        tv_sourth_trend = findViewById(R.id.tv_sourth_trend);
        tv_north_trend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_north_trend.setBackgroundColor(ResUtil.INSTANCE.getColor(R.color.color_B3BCD0));
                tv_north_trend.setTextColor(ResUtil.INSTANCE.getColor(R.color.color_191821));
                tv_sourth_trend.setBackgroundColor(ResUtil.INSTANCE.getColor(R.color.color_2D2C35));
                tv_sourth_trend.setTextColor(ResUtil.INSTANCE.getColor(R.color.color_B3BCD0));
                isSelect=!isSelect;
                setData();
                // redraw
                vChart.invalidate();
            }
        });
        tv_sourth_trend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_north_trend.setBackgroundColor(ResUtil.INSTANCE.getColor(R.color.color_2D2C35));
                tv_north_trend.setTextColor(ResUtil.INSTANCE.getColor(R.color.color_B3BCD0));
                tv_sourth_trend.setBackgroundColor(ResUtil.INSTANCE.getColor(R.color.color_B3BCD0));
                tv_sourth_trend.setTextColor(ResUtil.INSTANCE.getColor(R.color.color_191821));
                isSelect=!isSelect;
                setData();
                // redraw
                vChart.invalidate();
            }
        });
    }

    private void initLineChart() {
        vChart = findViewById(R.id.line_cahart);
        vChart.setNoDataText("");
        vChart.setNoDataTextColor(Color.parseColor("#C3CDE3"));
        //是否有触摸事件
        vChart.setTouchEnabled(false);
        //是否展示网格线
        vChart.setDrawGridBackground(false);
        //是否显示边界
        vChart.setDrawBorders(true);
        vChart.setBorderWidth(0.5f);
        vChart.setBorderColor(mGridColor);
        //是否可以拖动
        vChart.setDragEnabled(false);
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
        xl.setDrawGridLines(false);
        xl.setTextColor(mTextColor);
        xl.setGridColor(mGridColor);
        xl.setTextSize(12f);
        xl.setGridLineWidth(0.5f);
        xl.setLabelCount(2, true);
        // 将X坐标轴放置在底部，默认是在顶部。
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setAvoidFirstLastClipping(true);
        vChart.getAxisRight().setEnabled(false);
        MyXAxisRenderer xAxisRenderer = new MyXAxisRenderer(vChart.getViewPortHandler(), vChart.getXAxis(), vChart.getTransformer(YAxis.AxisDependency.LEFT));
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
        leftAxis.setLabelCount(2, true);
        leftAxis.setValueLineInside(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value == 0 || mEmpty ? "0" : String.format("%.2f", value);
            }
        });
       MyYAxisRenderer yAxisRenderer = new MyYAxisRenderer(vChart.getViewPortHandler(), vChart.getAxisLeft(), vChart.getTransformer(YAxis.AxisDependency.LEFT));
        vChart.setRendererLeftYAxis(yAxisRenderer);
    }

    private LineData getLineData(List<Entry> entrys) {
        LineDataSet lineDataSet;
        mEmpty = entrys.isEmpty();
        if (mEmpty) {
            entrys.add(new Entry(0, 1));
            entrys.add(new Entry(0, -1));
            lineDataSet = new LineDataSet(entrys, "");
            lineDataSet.setColor(mGridColor);
        }else {
            lineDataSet = new LineDataSet(entrys, "");
            if(isSelect) {
                lineDataSet.setColor(Color.parseColor("#53A0FD"));
            }else{
                lineDataSet.setColor(Color.parseColor("#FF8E1B"));
            }
        }
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setLineWidth(0.5f);
        LineData lineData = new LineData(lineDataSet);
        lineData.setValueTextColor(Color.WHITE);
        return lineData;
    }


    private void setData() {
        int[] d = {1, 1, -1};
        List<Entry> entrys = new ArrayList<>();
    /*    Random random = new Random();
        for (int i = 0; i < 149; i += 4) {
            Entry entry = new Entry(i, (random.nextInt(60) * d[random.nextInt(d.length)]));
            entrys.add(entry);
        }*/
        LineData lineData = getLineData(entrys);
        YAxis leftAxis = vChart.getAxisLeft();
        if (lineData.getYMax() < 0){
            leftAxis.setAxisMaximum(0f);
            leftAxis.resetAxisMinimum();
        }else if (lineData.getYMin() > 0){
            leftAxis.resetAxisMaximum();
            leftAxis.setAxisMinimum(0);
        }else {
            leftAxis.resetAxisMaximum();
            leftAxis.resetAxisMinimum();
        }
        vChart.setData(lineData);
        //vChart.invalidate();
    }

    public void setStockTs(String ts) {
        ((MyXAxisRenderer) vChart.getRendererXAxis()).setStockTs(ts);
        vChart.invalidate();
    }

    class MyYAxisRenderer extends YAxisRenderer {

        private float dividerY;
        private float dividerValue;

        public MyYAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
            super(viewPortHandler, yAxis, trans);
        }

        @Override
        public void computeAxis(float min, float max, boolean inverted) {
            super.computeAxis(min, max, inverted);
            if (max > 0 && min < 0) {
                dividerValue = 0;
            } else {
                double range = Math.abs(max - min);
                dividerValue = (float) (max - (range / 2));
            }
            MPPointD pos = mTrans.getPixelForValues(0f, dividerValue);
            dividerY = (float) pos.y;
        }

        @Override
        protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
            super.drawYLabels(c, fixedPosition, positions, offset);
            c.drawText(mYAxis.getValueFormatter().getFormattedValue(dividerValue), fixedPosition, dividerY + Utils.convertDpToPixel(0.7f) + offset, mAxisLabelPaint);
        }

        @Override
        public void renderGridLines(Canvas c) {
            super.renderGridLines(c);
            int clipRestoreCount = c.save();
            c.clipRect(getGridClippingRect());
            Path gridLinePath = mRenderGridLinesPath;
            gridLinePath.reset();
            gridLinePath.moveTo(mViewPortHandler.offsetLeft(), dividerY);
            gridLinePath.lineTo(mViewPortHandler.contentRight(), dividerY);
            c.drawPath(gridLinePath, mGridPaint);
            gridLinePath.reset();
            c.restoreToCount(clipRestoreCount);
        }
    }

    class MyXAxisRenderer extends XAxisRenderer {

        private int maxX;//最大数据点1分钟占一点
        private int breakIndex;//休盘点坐标
        private String openingHours;//开市时间
        private String breakTime;//休盘时间
        private String closingHours;//收市时间

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
                    breakTime = "11:30/13:00";//休盘时间
                    closingHours = "15:00";//收市时间
                    break;
            }
            mXAxis.setAxisMaximum(maxX);
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

    }
}
