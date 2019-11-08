package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.*;
import com.zhuorui.securities.market.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/7
 * Desc:
 */
public class MarketFinancinalProfitView  extends FrameLayout {
    private int mOut1Color;
    private int mOut2Color;
    private int mOut3Color;
    ArrayList<Integer> colors = new ArrayList<Integer>();
    private CombinedChart chart;
    private final int count = 5;
    private final int mGridColor = Color.parseColor("#337B889E");
    private final int mTextColor = Color.parseColor("#7B889E");
    private TextView tv_tips_one;
    private TextView tv_tips_two;
    private TextView tv_tips_three;
    private int type;
    private TextView tv_title;

    public MarketFinancinalProfitView(Context context) {
        this(context,null);
    }

    public MarketFinancinalProfitView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MarketFinancinalProfitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MarketFinancinalProfitView);//获得属性值
        type = a.getInt(R.styleable.MarketFinancinalProfitView_fina_type,-1);
        initColor();
        inflate(context, R.layout.layout_market_profit_view,this);
        initChart();
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_tips_one = findViewById(R.id.tv_tips_one);
        tv_tips_two = findViewById(R.id.tv_tips_two);
        tv_tips_three = findViewById(R.id.tv_tips_three);
        if(type==2){
            tv_title.setText("资产负债表");
            tv_tips_one.setText("总资产");
            tv_tips_two.setText("总负债");
            tv_tips_three.setText("资产负债率");
        }
    }

    private void initChart() {
        chart = findViewById(R.id.combine_chart);
        chart.setNoDataText("暂无数据");
        chart.setNoDataTextColor(Color.parseColor("#C3CDE3"));
        chart.setTouchEnabled(false);//是否有触摸事件
        chart.setDrawGridBackground(false);//是否展示网格线
        chart.setDragEnabled(false); //是否可以拖动
        chart.setScaleEnabled(false);// 可缩放
        chart.setBorderWidth(0.5f);
        chart.setBorderColor(Color.parseColor("#337B889E"));
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setMinOffset(0f);
        chart.setExtraBottomOffset(2f);
        // draw bars behind lines
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextColor(mTextColor);
        leftAxis.setGridColor(mGridColor);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextSize(12f);
        leftAxis.setGridLineWidth(0.5f);
        // 设置文字偏移量
        leftAxis.setXOffset(5f);
        leftAxis.setEdgeYOffset(5f);
        leftAxis.setSpaceBottom(0f);
        leftAxis.setSpaceTop(0f);
        leftAxis.setLabelCount(4, true);
        leftAxis.setValueLineInside(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value+"亿";
            }
        });
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(1f);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setXOffset(1f);
        xAxis.setAxisMinimum(1f);
        xAxis.setLabelCount(7, true);
        MyXAxisRenderer xAxisRenderer = new MyXAxisRenderer(chart.getViewPortHandler(), chart.getXAxis(), chart.getTransformer(YAxis.AxisDependency.LEFT));
        chart.setXAxisRenderer(xAxisRenderer);


        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());
        xAxis.setAxisMaximum(data.getXMax() + 2f);
        ((MyXAxisRenderer) chart.getRendererXAxis()).setStockTs();
        chart.setData(data);
        //柱形图使用自定义BarChartRenderer ，必须在setData 后设置
        List<DataRenderer> renders = new ArrayList<>();
        renders.add(new CombineLineRender(chart, chart.getAnimator(), chart.getViewPortHandler()));
        ((CombinedChartRenderer) chart.getRenderer()).setSubRenderers(renders);
        chart.getRenderer().initBuffers();
        chart.invalidate();
    }

    private BarData generateBarData() {
        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        entries1.add(new BarEntry(0.9f, 4500f));
        entries1.add(new BarEntry(1.75f, 5400f));
        entries1.add(new BarEntry(2, 6500f));
        entries1.add(new BarEntry(3, 7000f));
        entries1.add(new BarEntry(4, 7500f));
        // stacked
        entries2.add(new BarEntry(0, 1500f));
        entries2.add(new BarEntry(1, 1600f));
        entries2.add(new BarEntry(2, 1400f));
        entries2.add(new BarEntry(3, 1700f));
        entries2.add(new BarEntry(4, 1900f));

        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");
        set1.setColor(colors.get(0));
        set1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set2 = new BarDataSet(entries2, "");
        set2.setColors(colors.get(1));
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });
        float groupSpace = 0.30f;
        float barSpace = 0.03f; // x2 dataset
        float barWidth = 0.40f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1, set2);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(1.0f, groupSpace, barSpace); // start at x = 0

        return d;
    }

    private LineData generateLineData(){
        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1.60f,2500f));
        entries.add(new Entry(2.75f,3500f));
        entries.add(new Entry(3.90f, 2400f));
        entries.add(new Entry(5.05f,2800f));
        entries.add(new Entry(6.25f,3800f));
        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(colors.get(2));
        set.setLineWidth(0.8f);
        set.setCircleColor(colors.get(2));
        set.setCircleRadius(1f);
        set.setFillColor(colors.get(2));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawValues(true);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });
        d.addDataSet(set);
        return d;
    }

    private void initColor() {
        mOut1Color = Color.parseColor("#3A79C8");
        mOut2Color = Color.parseColor("#5DA6F2");
        mOut3Color = Color.parseColor("#FF8E1B");
        colors = new ArrayList();
        colors.add(mOut1Color);
        colors.add(mOut2Color);
        colors.add(mOut3Color);
    }

    class CombineLineRender extends CombinedChartRenderer{
        private double zeroX = 0;//0 的y值
        private float space = 0;
        private float zeroLineWidth = 0;
        private float textSpace = 0;
        private final float testSizePx = Utils.convertDpToPixel(10f);
        public CombineLineRender(CombinedChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
            super(chart, animator, viewPortHandler);
        }

        @Override
        public void drawValues(Canvas c) {
            MPPointD pos = chart.getTransformer(YAxis.AxisDependency.LEFT).getPixelForValues(0f, 0f);
            zeroX = pos.x;
            super.drawValues(c);
        }

        @Override
        public void drawValue(Canvas c, String valueText, float x, float y, int color) {
            x= (float) (zeroX+5f);
            super.drawValue(c, valueText, x, y, color);
        }

    }

    class MyXAxisRenderer extends XAxisRenderer {

        private float maxX;//最大数据点1分钟占一点
        private String opening;//开市时间
        private String second;//休盘时间
        private String thrid;//收市时间
        private String fourth;//收市时间
        private String fifth;//收市时间
        public MyXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
            super(viewPortHandler, xAxis, trans);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                   if (value == 0) {
                        return "";
                    } else if (value == 1.25f) {
                        return opening;
                    } else if (value == 2.50f) {
                        return second;
                    } else if (value ==3.75f) {
                        return thrid;
                    }else if (value == 5.00f) {
                        return fourth;
                    }else if (value == 6.25f) {
                       return fifth;
                   }else if(value==7.50f){
                       return "";
                   }
                    return super.getFormattedValue(value);
                }
            });
        }

        public void setStockTs() {
            maxX=7.5f;
            opening = "2017-06-30";//开市时间
            second = "2017-12-31";//休盘时间
            thrid = "2018-06-30";//收市时间
            fourth = "2018-12-31";//收市时间
            fifth = "2019-06-30";//收市时间
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
    /*        int clipRestoreCount = c.save();
            c.clipRect(getGridClippingRect());
            Path gridLinePath = mRenderGridLinesPath;
            gridLinePath.reset();
            int index = 6 ;
            float[] positions = new float[maxX];
            positions[index] = 7;
            mTrans.pointValuesToPixel(positions);
            drawGridLine(c, positions[index], positions[index], gridLinePath);
            c.restoreToCount(clipRestoreCount);*/
        }

        /**
         * 绘制休盘分割Label
         *
         * @param c
         * @param pos
         * @param anchor
         */
        private void drawCustomLabel(Canvas c, float pos, MPPointF anchor) {
      /*      float[] positions = new float[maxX];
            int index = maxX-1;
            positions[index] = maxX;
            mTrans.pointValuesToPixel(positions);
            drawLabel(c, fifth, positions[index], pos, anchor, mXAxis.getLabelRotationAngle());*/
        }

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
}
