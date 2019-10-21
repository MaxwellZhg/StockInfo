package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zhuorui.commonwidget.ZRThreePartLineLayout;
import com.zhuorui.securities.market.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/21
 * Desc:
 */
public class MarketUpDownInfoView extends FrameLayout {
    private final int upColor = Color.parseColor("#D9001B");
    private final int downColor = Color.parseColor("#00AB3B");
    private final int zeroColor = Color.parseColor("#B3BCD0");
    private int mDateNum = 5;
    private BarChart vChart;
    private ZRThreePartLineLayout ll_three_part;

    public MarketUpDownInfoView(Context context) {
        this(context,null);
    }

    public MarketUpDownInfoView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MarketUpDownInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
         inflate(context, R.layout.layout_market_up_down_info,this   );
         initBarChart();
         getTestData();
    }


    private void initBarChart() {
        ll_three_part = findViewById(R.id.ll_three_part);
        vChart = findViewById(R.id.bar_market_infochart);
        vChart.setNoDataText("暂无数据");
        vChart.setNoDataTextColor(Color.parseColor("#C3CDE3"));
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
        vChart.getDescription().setEnabled(false);
        vChart.getAxisRight().setEnabled(false);
        vChart.getLegend().setEnabled(false);
        vChart.setMinOffset(0f);
        vChart.setExtraBottomOffset(2f);

        /***XY轴的设置***/
        //X轴设置显示位置在底部
        XAxis xAxis = vChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setYOffset(8f);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.parseColor("#7B889E"));
        xAxis.setLabelCount(10,false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String str ="";
              if(value==0){
                  str= "≥6%";
              }else if(value==1){
                  str="5~1%";
              }else if(value==2){
                  str="0%";
              }else if(value==3){
                  str="-1~-5%";
              }else{
                  str="≤-6%";
              }
              return str;
            }
        });
        YAxis axisLeft = vChart.getAxisLeft();
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);
        axisLeft.setDrawZeroLine(false);
        axisLeft.setZeroLineColor(Color.parseColor("#333741"));
        axisLeft.setZeroLineWidth(1f);
        axisLeft.setSpaceTop(20f);
        axisLeft.setAxisMaximum(1000f);
        axisLeft.setValueLineInside(true);
        axisLeft.setLabelCount(2, true);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });
        vChart.setRenderer(new HCFVRenderer(vChart, vChart.getAnimator(), vChart.getViewPortHandler()));
    }

    private void getTestData() {
        List<Integer> color = new ArrayList<>();
        List<BarEntry> entryList = new ArrayList<>();
        BarEntry barEntry = new BarEntry(0, 456);
        entryList.add(barEntry);
        BarEntry barEntry1 = new BarEntry(1, 788);
        entryList.add(barEntry1);
        BarEntry barEntry2= new BarEntry(2, 344);
        entryList.add(barEntry2);
        BarEntry barEntry3= new BarEntry(3, 577);
        entryList.add(barEntry3);
        BarEntry barEntry4= new BarEntry(4, 20);
        entryList.add(barEntry4);
        color.add(upColor);
        color.add(upColor);
        color.add(zeroColor);
        color.add(downColor);
        color.add(downColor);
        setData(entryList, color);
        ll_three_part.setType(1);
        ll_three_part.setValues(1128,334,1039);
    }

    private void setData(List<BarEntry> entryList, List<Integer> color) {
        BarDataSet barDataSet = new BarDataSet(entryList, "");
        barDataSet.setValueTextSize(10f);
        barDataSet.setColors(color);
        barDataSet.setValueTextColors(color);
        barDataSet.setDrawValues(true);
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0) {
                    return "0.0";
                }
                return String.format("%.1f", value);
            }
        });
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        //数据上下留空间出显示Values
  /*     float max = barDataSet.getYMax();
        float min = barDataSet.getYMin();
        double maxMinAbs = Math.abs(max - min);
        float maxMinAbs15 = (float) (maxMinAbs * 0.15);
        float maxMinAbs05 = (float) (maxMinAbs * 0.05);
        if (max < maxMinAbs15) {
            vChart.getAxisLeft().setAxisMaximum(maxMinAbs15);
            vChart.getAxisLeft().setAxisMinimum(min - maxMinAbs05);
        } else if (min > -maxMinAbs15) {
            vChart.getAxisLeft().setAxisMaximum(max + maxMinAbs05);
            vChart.getAxisLeft().setAxisMinimum(-maxMinAbs15);
        } else {
            vChart.getAxisLeft().setAxisMaximum(max + maxMinAbs05);
            vChart.getAxisLeft().setAxisMinimum(min - maxMinAbs05);
        }*/
        vChart.getXAxis().setLabelCount(entryList.size());
        vChart.setData(barData);
        vChart.invalidate();
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
   /*         if (y > zeroY) {
                y = (float) (zeroY - space - zeroLineWidth);
            } else {
                y = (float) zeroY + space + testSizePx - textSpace;
            }*/
            super.drawValue(c, valueText, x, y, color);
        }
    }
}
