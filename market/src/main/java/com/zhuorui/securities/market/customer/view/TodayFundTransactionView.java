package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.zhuorui.securities.market.R;

import java.util.ArrayList;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-14 13:45
 * desc   : 今日成交分布
 */
public class TodayFundTransactionView extends FrameLayout {

    private PieChart vPieChart;
    private final float selectionShift = 10f;
    private final float tblr = 10f;

    public TodayFundTransactionView(Context context) {
        this(context, null);
    }

    public TodayFundTransactionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TodayFundTransactionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_today_fund_transaction, this);
        initPieChart();
        setData(getTestPieData());

    }

    private void setData(PieData data) {
        vPieChart.setData(data);
        vPieChart.highlightValues(null);
        vPieChart.invalidate();                    //将图表重绘以显示设置的属性和数据

    }

    private PieData getTestPieData() {
        ArrayList<PieEntry> pieEntryList = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#00AB3B"));
        colors.add(Color.parseColor("#336666"));
        colors.add(Color.parseColor("#339966"));
        colors.add(Color.parseColor("#CC6666"));
        colors.add(Color.parseColor("#D73239"));
        colors.add(Color.parseColor("#C4000A"));
        //饼图实体 PieEntry
        pieEntryList.add(new PieEntry(10, "item1"));
        pieEntryList.add(new PieEntry(8, "item2"));
        pieEntryList.add(new PieEntry(40, "item3"));
        pieEntryList.add(new PieEntry(10, "item4"));
        pieEntryList.add(new PieEntry(30, "item5"));
        pieEntryList.add(new PieEntry(20, "item6"));
        //饼状图数据集 PieDataSet
        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "资产总览");
//        pieDataSet.setSliceSpace(3f);           //设置饼状Item之间的间隙
//        pieDataSet.setSelectionShift(selectionShift);      //设置饼状Item被选中时变化的距离
        pieDataSet.setColors(colors);           //为DataSet中的数据匹配上颜色集(饼图Item颜色)
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(14f);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        pieDataSet.setValueLinePart1Length(0.3f);
//        pieDataSet.setValueLinePart2Length(0.4f);
        //最终数据 PieData
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);            //设置是否显示数据实体(百分比，true:以下属性才有意义)
        pieData.setValueFormatter(new PercentFormatter("###,###,##0.00"));//设置所有DataSet内数据实体（百分比）的文本字体格式
        return pieData;
    }

    private void initPieChart() {
        vPieChart = findViewById(R.id.pie_cahart);
        vPieChart.setUsePercentValues(true);//使用百分比显示
        vPieChart.getDescription().setEnabled(false);//是否启用描述
        vPieChart.getLegend().setEnabled(false);//是否启用图列
        vPieChart.setRotationAngle(270);//设置pieChart图表起始角度
        vPieChart.setExtraOffsets(tblr, tblr, tblr, tblr);//设置pieChart图表上下左右的偏移，类似于外边距
        vPieChart.setRotationEnabled(false);              //设置pieChart图表是否可以手动旋转
        vPieChart.setHighlightPerTapEnabled(false);       //设置piecahrt图表点击Item高亮是否可用
        // 设置 pieChart 图表Item文本属性
        vPieChart.setDrawEntryLabels(false);              //设置pieChart是否显示文字（true：下面属性才有效果）
//        vPieChart.setEntryLabelColor(Color.WHITE);       //设置pieChart图表文本字体颜色
//        vPieChart.setEntryLabelTextSize(30f);            //设置pieChart图表文本字体大小
        //设置 pieChart 内部圆环属性
        vPieChart.setDrawHoleEnabled(true);              //是否显示PieChart内部圆环(true:下面属性才有意义)
        vPieChart.setHoleRadius(60);
        vPieChart.setHoleColor(Color.parseColor("#211F2A"));             //设置PieChart内部圆的颜色
        vPieChart.setDrawCenterText(true);               //是否绘制PieChart内部中心文本（true：下面属性才有意义）
        vPieChart.setCenterText("今日资金");                 //设置PieChart内部圆文字的内容
        vPieChart.setCenterTextSize(18f);                //设置PieChart内部圆文字的大小
        vPieChart.setCenterTextColor(Color.WHITE);         //设置PieChart内部圆文字的颜色
        vPieChart.setTransparentCircleRadius(0f);       //设置PieChart内部透明圆的半径(这里设置31.0f)
//        vPieChart.setTransparentCircleColor(Color.BLACK);//设置PieChart内部透明圆与内部圆间距(31f-28f)填充颜色
//        vPieChart.setTransparentCircleAlpha(50);         //设置PieChart内部透明圆与内部圆间距(31f-28f)透明度[0~255]数值越小越透明

    }
}
