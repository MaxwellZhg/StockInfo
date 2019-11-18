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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.*;
import com.zhuorui.commonwidget.ZREmptyView;
import com.zhuorui.securities.base2app.util.ResUtil;
import com.zhuorui.securities.base2app.util.TimeZoneUtil;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.net.response.FinancialReportResponse;
import com.zhuorui.securities.market.util.MathUtil;
import me.jessyan.autosize.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/11
 * Desc:
 */
public class MarketFinanicalDetailCrashFlowView extends FrameLayout {
    private int mOut1Color;
    private int mOut2Color;
    private int mOut3Color;
    ArrayList<Integer> colors = new ArrayList<Integer>();
    private LineChart chart;
    private final int mGridColor = Color.parseColor("#337B889E");
    private final int mTextColor = Color.parseColor("#7B889E");
    private boolean mEmpty;
    private List<FinancialReportResponse.CashFlowReport> profitList ;
    List<Entry> entries1 =new ArrayList<>();
    List<Entry> entries2 =new ArrayList<>();
    List<Entry> entries3 =new ArrayList<>();
    List<String> xAisxDate =new ArrayList<>();
    private ZREmptyView empty_view;

    public MarketFinanicalDetailCrashFlowView(Context context) {
       this(context,null);
    }

    public MarketFinanicalDetailCrashFlowView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MarketFinanicalDetailCrashFlowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initColor();
        inflate(context, R.layout.layout_market_finacinal_crash_flow,this);
        initChart();
        initView();
    }

    private void initView() {
        empty_view = findViewById(R.id.empty_view);
    }

    private void initChart() {
        chart = findViewById(R.id.line_chart);
        chart.setNoDataText("");
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
        // 图表左边的y坐标轴线
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextColor(mTextColor);
        leftAxis.setGridColor(mGridColor);
        leftAxis.setTextSize(12f);
        leftAxis.setGridLineWidth(0.5f);
        // 设置文字偏移量
        leftAxis.setXOffset(5f);
        leftAxis.setEdgeYOffset(5f);
        leftAxis.setSpaceBottom(5f);
        leftAxis.setSpaceTop(5f);
        leftAxis.setLabelCount(2, true);
        leftAxis.setValueLineInside(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value == 0 || mEmpty ? "0" : String.format("%.2f", value)+"亿";
            }
        });
        MyYAxisRenderer yAxisRenderer = new MyYAxisRenderer(chart.getViewPortHandler(), chart.getAxisLeft(), chart.getTransformer(YAxis.AxisDependency.LEFT));
        chart.setRendererLeftYAxis(yAxisRenderer);

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
    }

    private LineData getLineData(/*List<Entry> entrys*/) {
       // mEmpty = entrys.isEmpty();
 /*       LineDataSet lineDataSet;
        if (mEmpty) {
            entrys.add(new Entry(0, 1));
            entrys.add(new Entry(0, -1));
            lineDataSet = new LineDataSet(entrys, "");
            lineDataSet.setColor(Color.parseColor("#00FF8E1B"));
        } else {
            lineDataSet = new LineDataSet(entrys, "");
            lineDataSet.setColor(Color.parseColor("#FF8E1B"));
        }*/
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet set1 = new LineDataSet(entries1, "Line DataSet");
        set1.setColor(colors.get(0));
        set1.setLineWidth(0.8f);
        set1.setCircleColor(colors.get(0));
        set1.setCircleRadius(1f);
        set1.setFillColor(colors.get(0));
        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setDrawValues(true);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });
        LineDataSet set2 = new LineDataSet(entries2, "Line DataSet");
        set2.setColor(colors.get(1));
        set2.setLineWidth(0.8f);
        set2.setCircleColor(colors.get(1));
        set2.setCircleRadius(1f);
        set2.setFillColor(colors.get(1));
        set2.setMode(LineDataSet.Mode.LINEAR);
        set2.setDrawValues(true);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });

        LineDataSet set3 = new LineDataSet(entries3, "Line DataSet");
        set3.setColor(colors.get(2));
        set3.setLineWidth(0.8f);
        set3.setCircleColor(colors.get(2));
        set3.setCircleRadius(1f);
        set3.setFillColor(colors.get(2));
        set3.setMode(LineDataSet.Mode.LINEAR);
        set3.setDrawValues(true);
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);
        set3.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        LineData data = new LineData(dataSets);
        return data;
    }

    public void setData() {
        setStockTs();
        LineData lineData = getLineData();
        YAxis leftAxis = chart.getAxisLeft();
        if (lineData.getYMax() < 0) {
            leftAxis.setAxisMaximum(0f);
            leftAxis.resetAxisMinimum();
        } else if (lineData.getYMin() > 0) {
            leftAxis.resetAxisMaximum();
            leftAxis.setAxisMinimum(0);
        } else {
            leftAxis.resetAxisMaximum();
            leftAxis.resetAxisMinimum();
        }
        chart.setData(lineData);
        chart.invalidate();
    }

    public void setProfitListData(List<FinancialReportResponse.CashFlowReport>  profitList ){
        this.profitList =profitList;
        if(profitList!=null) {
            empty_view.setVisibility(INVISIBLE);
            detailListData(profitList);
        }else{
            empty_view.setVisibility(  VISIBLE);
        }
    }

    private void detailListData(List<FinancialReportResponse.CashFlowReport> profitList) {
         entries1.clear();
         entries2.clear();
         entries3.clear();
         if(profitList!=null) {
             for (int i = 0; i < profitList.size(); i++) {
                 xAisxDate.add(TimeZoneUtil.timeFormat(profitList.get(i).getDate(), "yyyy-MM-dd"));
             }
             entries1.add(new Entry(1.25f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(0).getNetOperating())));
             entries1.add(new Entry(2.50f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(1).getNetOperating())));
             entries1.add(new Entry(3.75f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(2).getNetOperating())));
             entries1.add(new Entry(5.00f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(3).getNetOperating())));
             entries1.add(new Entry(6.25f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(4).getNetOperating())));
             entries2.add(new Entry(1.25f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(0).getNetInvestment())));
             entries2.add(new Entry(2.50f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(1).getNetInvestment())));
             entries2.add(new Entry(3.75f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(2).getNetInvestment())));
             entries2.add(new Entry(5.00f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(3).getNetInvestment())));
             entries2.add(new Entry(6.25f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(4).getNetInvestment())));
             entries3.add(new Entry(1.25f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(0).getNetFinancing())));
             entries3.add(new Entry(2.50f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(1).getNetFinancing())));
             entries3.add(new Entry(3.75f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(2).getNetFinancing())));
             entries3.add(new Entry(5.00f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(3).getNetFinancing())));
             entries3.add(new Entry(6.25f, MathUtil.INSTANCE.convertToUnitFloat(profitList.get(4).getNetFinancing())));
             setData();
         }
    }

    public void setStockTs() {
        ((MyXAxisRenderer) chart.getRendererXAxis()).setStockTs();
        chart.invalidate();
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
            opening = xAisxDate.get(0);//开市时间
            second = xAisxDate.get(1);//休盘时间
            thrid = xAisxDate.get(2);//收市时间
            fourth =xAisxDate.get(3);//收市时间
            fifth = xAisxDate.get(4);//收市时间
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
