package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.*;
import com.zhuorui.securities.base2app.infra.LogInfra;
import com.zhuorui.securities.base2app.util.ResUtil;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.net.response.FinancialReportResponse;
import com.zhuorui.securities.market.util.MathUtil;
import me.jessyan.autosize.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/7
 * Desc:
 */
public class MarketFinancinalProfitView extends FrameLayout {
    private List<FinancialReportResponse.ProfitReport> profitReport;
    private List<FinancialReportResponse.LiabilistyReport> liabilistyReport;
    private int mOut1Color;
    private int mOut2Color;
    private int mOut3Color;
    ArrayList<Integer> colors = new ArrayList<Integer>();
    private CombinedChart chart;
    private boolean mEmpty;
    private final int count = 5;
    private final int mGridColor = Color.parseColor("#337B889E");
    private final int mTextColor = Color.parseColor("#7B889E");
    private TextView tv_tips_one;
    private TextView tv_tips_two;
    private TextView tv_tips_three;
    private int type;
    private TextView tv_title;
    ArrayList<BarEntry> entries1 = new ArrayList<>();
    ArrayList<BarEntry> entries2 = new ArrayList<>();
    ArrayList<Entry>  entries3 = new ArrayList<>();
    ArrayList<Float> yAxisData = new ArrayList<>();
    List<String> xAisxDate = new ArrayList<>();
    private YAxis leftAxis;
    private YAxis rightAxis;

    public MarketFinancinalProfitView(Context context) {
        this(context, null);
    }

    public MarketFinancinalProfitView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarketFinancinalProfitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MarketFinancinalProfitView);//获得属性值
        type = a.getInt(R.styleable.MarketFinancinalProfitView_fina_type, -1);
        initColor();
        inflate(context, R.layout.layout_market_profit_view, this);
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_tips_one = findViewById(R.id.tv_tips_one);
        tv_tips_two = findViewById(R.id.tv_tips_two);
        tv_tips_three = findViewById(R.id.tv_tips_three);
        if (type == 2) {
            tv_title.setText(ResUtil.INSTANCE.getString(R.string.outcome_chat));
            tv_tips_one.setText(ResUtil.INSTANCE.getString(R.string.all_total_count));
            tv_tips_two.setText(ResUtil.INSTANCE.getString(R.string.all_total_to_pay));
            tv_tips_three.setText(ResUtil.INSTANCE.getString(R.string.all_total_topay_rate));
        }
    }

    private void initChart() {
        chart = findViewById(R.id.combine_chart);
        chart.setNoDataText(ResUtil.INSTANCE.getString(R.string.temp_no_data));
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
        chart.setDrawBorders(true);
        // draw bars behind lines
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });

        leftAxis = chart.getAxis(YAxis.AxisDependency.LEFT);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextColor(mTextColor);
        leftAxis.setGridColor(mGridColor);
        leftAxis.setTextSize(12f);
        // 设置文字偏移量
        leftAxis.setXOffset(5f);
        leftAxis.setEdgeYOffset(5f);
        leftAxis.setSpaceBottom(0f);
        leftAxis.setSpaceTop(10f);
        leftAxis.setZeroLineColor(mGridColor);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setLabelCount(5, true);
        leftAxis.setValueLineInside(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value == 0 || mEmpty ? "0" : String.format("%.2f", value) + "亿";
            }
        });
        rightAxis = chart.getAxis(YAxis.AxisDependency.RIGHT);

        rightAxis.setEnabled(true);
        rightAxis.setDrawGridLines(true);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setTextColor(mTextColor);
        rightAxis.setGridColor(mGridColor);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setTextSize(12f);
        rightAxis.setGridLineWidth(0.5f);
        rightAxis.setAxisMinimum(yAxisData.get(0) - 10f);
        rightAxis.setAxisMaximum(yAxisData.get(4) + 10f);
        // 设置文字偏移量
        rightAxis.setXOffset(5f);
        rightAxis.setEdgeYOffset(5f);
        rightAxis.setSpaceBottom(0f);
        rightAxis.setSpaceTop(10f);
        rightAxis.setLabelCount(5, true);
        rightAxis.setValueLineInside(true);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        rightAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value == 0 || mEmpty ? "0" : String.format("%.2f", value) + "%";
            }
        });
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(1f);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineWidth(0.1f);
        xAxis.setXOffset(1f);
        xAxis.setAxisLineColor(mGridColor);
        xAxis.setGridColor(mGridColor);
        xAxis.setAxisMinimum(1f);
        xAxis.setLabelCount(7, true);
        MyXAxisRenderer xAxisRenderer = new MyXAxisRenderer(chart.getViewPortHandler(), chart.getXAxis(), chart.getTransformer(YAxis.AxisDependency.LEFT));
        chart.setXAxisRenderer(xAxisRenderer);


        CombinedData data = new CombinedData();
        data.setData(generateBarData());
        data.setData(generateLineData());
        xAxis.setAxisMaximum(data.getXMax() + 2f);
        if (type == 2) {
            leftAxis.setAxisMinimum(data.getYMin() - 100f);
        }
        ((MyXAxisRenderer) chart.getRendererXAxis()).setStockTs();
        chart.setData(data);
        //柱形图使用自定义BarChartRenderer ，必须在setData 后设置
        chart.invalidate();
    }

    private void setRateData(CombinedData data) {
        LogUtils.e(data.getYMax()+"");
        data.setData(generateLineData());

    }

    public void setProfitChatData(List<FinancialReportResponse.ProfitReport> profitReport) {
        this.profitReport = profitReport;
        if (profitReport != null) {
            detailProfitData(profitReport);
        }
    }

    public void setOutProfitChatData(List<FinancialReportResponse.LiabilistyReport> liabilistyReport) {
        this.liabilistyReport = liabilistyReport;
        if (liabilistyReport != null) {
            detailLibProfitData(liabilistyReport);
        }
    }

    private void detailLibProfitData(List<FinancialReportResponse.LiabilistyReport> liabilistyReport) {
        entries1.clear();
        entries1.clear();
        xAisxDate.clear();
        entries1.add(new BarEntry(0.9f, MathUtil.INSTANCE.convertToUnitFloat(liabilistyReport.get(0).getTotalAssets())));
        entries1.add(new BarEntry(1.75f, MathUtil.INSTANCE.convertToUnitFloat(liabilistyReport.get(1).getTotalAssets())));
        entries1.add(new BarEntry(2, MathUtil.INSTANCE.convertToUnitFloat(liabilistyReport.get(2).getTotalAssets())));
        entries1.add(new BarEntry(3, MathUtil.INSTANCE.convertToUnitFloat(liabilistyReport.get(3).getTotalAssets())));
        entries1.add(new BarEntry(4, MathUtil.INSTANCE.convertToUnitFloat(liabilistyReport.get(4).getTotalAssets())));
        entries2.add(new BarEntry(0.9f, MathUtil.INSTANCE.convertToUnitFloat(liabilistyReport.get(0).getTotalLiability())));
        entries2.add(new BarEntry(1.75f, MathUtil.INSTANCE.convertToUnitFloat(liabilistyReport.get(1).getTotalLiability())));
        entries2.add(new BarEntry(2, MathUtil.INSTANCE.convertToUnitFloat(liabilistyReport.get(2).getTotalLiability())));
        entries2.add(new BarEntry(3, MathUtil.INSTANCE.convertToUnitFloat(liabilistyReport.get(3).getTotalLiability())));
        entries2.add(new BarEntry(4, MathUtil.INSTANCE.convertToUnitFloat(liabilistyReport.get(4).getTotalLiability())));
        entries3.add(new Entry(1.60f,MathUtil.INSTANCE.convertToUnitRateFloat(liabilistyReport.get(0).getLiabilityRate())));
        entries3.add(new Entry(2.75f,MathUtil.INSTANCE.convertToUnitRateFloat(liabilistyReport.get(1).getLiabilityRate())));
        entries3.add(new Entry(3.90f,MathUtil.INSTANCE.convertToUnitRateFloat(liabilistyReport.get(2).getLiabilityRate())));
        entries3.add(new Entry(5.05f,MathUtil.INSTANCE.convertToUnitRateFloat(liabilistyReport.get(3).getLiabilityRate())));
        entries3.add(new Entry(6.25f,MathUtil.INSTANCE.convertToUnitRateFloat(liabilistyReport.get(4).getLiabilityRate())));
        for (int i = 0; i < liabilistyReport.size(); i++) {
            xAisxDate.add(liabilistyReport.get(i).getDate());
            yAxisData.add(MathUtil.INSTANCE.convertToUnitRateFloat(liabilistyReport.get(i).getLiabilityRate()));
        }
        Collections.sort(yAxisData);
        initChart();
    }

    private void detailProfitData(List<FinancialReportResponse.ProfitReport> profitReport) {
        entries1.clear();
        entries1.clear();
        xAisxDate.clear();
        entries1.add(new BarEntry(0.9f, MathUtil.INSTANCE.convertToUnitFloat(profitReport.get(0).getIncome())));
        entries1.add(new BarEntry(1.75f, MathUtil.INSTANCE.convertToUnitFloat(profitReport.get(1).getIncome())));
        entries1.add(new BarEntry(2, MathUtil.INSTANCE.convertToUnitFloat(profitReport.get(2).getIncome())));
        entries1.add(new BarEntry(3, MathUtil.INSTANCE.convertToUnitFloat(profitReport.get(3).getIncome())));
        entries1.add(new BarEntry(4, MathUtil.INSTANCE.convertToUnitFloat(profitReport.get(4).getIncome())));
        entries2.add(new BarEntry(0.9f, MathUtil.INSTANCE.convertToUnitFloat(profitReport.get(0).getProfit())));
        entries2.add(new BarEntry(1.75f, MathUtil.INSTANCE.convertToUnitFloat(profitReport.get(1).getProfit())));
        entries2.add(new BarEntry(2, MathUtil.INSTANCE.convertToUnitFloat(profitReport.get(2).getProfit())));
        entries2.add(new BarEntry(3, MathUtil.INSTANCE.convertToUnitFloat(profitReport.get(3).getProfit())));
        entries2.add(new BarEntry(4, MathUtil.INSTANCE.convertToUnitFloat(profitReport.get(4).getProfit())));
        entries3.add(new Entry(1.60f,MathUtil.INSTANCE.convertToUnitRateFloat(profitReport.get(0).getProfitRate())));
        entries3.add(new Entry(2.75f,MathUtil.INSTANCE.convertToUnitRateFloat(profitReport.get(1).getProfitRate())));
        entries3.add(new Entry(3.90f,MathUtil.INSTANCE.convertToUnitRateFloat(profitReport.get(2).getProfitRate())));
        entries3.add(new Entry(5.05f,MathUtil.INSTANCE.convertToUnitRateFloat(profitReport.get(3).getProfitRate())));
        entries3.add(new Entry(6.25f,MathUtil.INSTANCE.convertToUnitRateFloat(profitReport.get(4).getProfitRate())));
        for (int i = 0; i < profitReport.size(); i++) {
            xAisxDate.add(profitReport.get(i).getDate());
            yAxisData.add(MathUtil.INSTANCE.convertToUnitRateFloat(profitReport.get(i).getProfitRate()));
        }
        Collections.sort(yAxisData);
        initChart();
    }


    private BarData generateBarData() {
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

    private LineData generateLineData() {
        LineData d = new LineData();
        LineDataSet set = new LineDataSet(entries3, "Line DataSet");
        set.setColor(colors.get(2));
        set.setLineWidth(0.8f);
        set.setCircleColor(colors.get(2));
        set.setCircleRadius(1f);
        set.setFillColor(colors.get(2));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawValues(true);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
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

    class MyXAxisRenderer extends XAxisRenderer {

        private float maxX;//最大数据点1分钟占一点

        public MyXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
            super(viewPortHandler, xAxis, trans);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    if (value == 0) {
                        return "";
                    } else if (value == 1.25f) {
                        return xAisxDate.get(0);
                    } else if (value == 2.50f) {
                        return xAisxDate.get(1);
                    } else if (value == 3.75f) {
                        return xAisxDate.get(2);
                    } else if (value == 5.00f) {
                        return xAisxDate.get(3);
                    } else if (value == 6.25f) {
                        return xAisxDate.get(4);
                    } else if (value == 7.50f) {
                        return "";
                    }
                    return super.getFormattedValue(value);
                }
            });
        }

        public void setStockTs() {
            maxX = 7.5f;
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
   /*      int clipRestoreCount = c.save();
            c.clipRect(getGridClippingRect());
            Path gridLinePath = mRenderGridLinesPath;
            gridLinePath.reset();
            int index = 6;
            float[] positions = new float[7];
            positions[index] = 7.5f;
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
/*            int index = 6;
            float[] positions = new float[7];
            positions[index] = 7.5f;
            mTrans.pointValuesToPixel(positions);
            drawLabel(c, "", positions[index], pos, anchor, mXAxis.getLabelRotationAngle());*/
        }

    }
}
