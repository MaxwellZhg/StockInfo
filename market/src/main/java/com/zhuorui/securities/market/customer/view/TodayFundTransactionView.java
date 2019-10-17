package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.zhuorui.securities.market.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-14 13:45
 * desc   : 今日资金成交分布
 */
public class TodayFundTransactionView extends FrameLayout {

    private PieChart vPieChart;
    private ComparisonMapView vComparisonMap;
    private int mOut1Color;
    private int mOut2Color;
    private int mOut3Color;
    private int mIn1Color;
    private int mIn2Color;
    private int mIn3Color;

    ArrayList<Integer> colors = new ArrayList<Integer>();

    public TodayFundTransactionView(Context context) {
        this(context, null);
    }

    public TodayFundTransactionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TodayFundTransactionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initColor();
        inflate(context, R.layout.view_today_fund_transaction, this);
        initPieChart();
        initComparisonMap();
    }

    private void initColor() {
        mOut1Color = Color.parseColor("#00AB3B");
        mOut2Color = Color.parseColor("#336666");
        mOut3Color = Color.parseColor("#339966");
        mIn1Color = Color.parseColor("#C4000A");
        mIn2Color = Color.parseColor("#D73239");
        mIn3Color = Color.parseColor("#CC6666");
        colors = new ArrayList();
        colors.add(mOut1Color);
        colors.add(mOut2Color);
        colors.add(mOut3Color);
        colors.add(mIn3Color);
        colors.add(mIn2Color);
        colors.add(mIn1Color);
    }

    private void initPieChart() {
        vPieChart = findViewById(R.id.pie_cahart);
        vPieChart.setUsePercentValues(true);//使用百分比显示
        vPieChart.getDescription().setEnabled(false);//是否启用描述
        vPieChart.getLegend().setEnabled(false);//是否启用图列
        vPieChart.setRotationAngle(270);//设置pieChart图表起始角度
        vPieChart.setMinOffset(0f);
        vPieChart.setRotationEnabled(false);              //设置pieChart图表是否可以手动旋转
        vPieChart.setHighlightPerTapEnabled(false);       //设置piecahrt图表点击Item高亮是否可用
        // 设置 pieChart 图表Item文本属性
        vPieChart.setDrawEntryLabels(false);              //设置pieChart是否显示文字（true：下面属性才有效果）
        //设置 pieChart 内部圆环属性
        vPieChart.setDrawHoleEnabled(true);              //是否显示PieChart内部圆环(true:下面属性才有意义)
        vPieChart.setHoleRadius(60);
        vPieChart.setHoleColor(Color.parseColor("#211F2A"));             //设置PieChart内部圆的颜色
        vPieChart.setDrawCenterText(true);               //是否绘制PieChart内部中心文本（true：下面属性才有意义）
        vPieChart.setCenterText("今日资金");                 //设置PieChart内部圆文字的内容
        vPieChart.setCenterTextSize(18f);                //设置PieChart内部圆文字的大小
        vPieChart.setCenterTextColor(Color.WHITE);         //设置PieChart内部圆文字的颜色
        vPieChart.setTransparentCircleRadius(0f);       //设置PieChart内部透明圆的半径(这里设置31.0f)
    }

    private void initComparisonMap() {
        vComparisonMap = findViewById(R.id.comparison_map);
        vComparisonMap.setTitle("流入", "流出", "净流入");
    }

    private ArrayList<PieEntry> getPieEntrys(List<Float> out, List<Float> in) {
        ArrayList<PieEntry> pieEntryList = new ArrayList<PieEntry>();
        //饼图实体 PieEntry
        pieEntryList.add(new PieEntry(out.get(0), "大单流出"));
        pieEntryList.add(new PieEntry(out.get(1), "中单流出"));
        pieEntryList.add(new PieEntry(out.get(2), "小单流出"));
        pieEntryList.add(new PieEntry(in.get(2), "小单流入"));
        pieEntryList.add(new PieEntry(in.get(1), "中单流入"));
        pieEntryList.add(new PieEntry(in.get(0), "大单流入"));
        return pieEntryList;
    }

    private PieData getPieData(ArrayList<PieEntry> data) {
        //饼状图数据集 PieDataSet
        PieDataSet pieDataSet = new PieDataSet(data, "今日资金成交分布");
        pieDataSet.setColors(colors);           //为DataSet中的数据匹配上颜色集(饼图Item颜色)
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(14f);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setUsingSliceColorAsValueLineColor(true);
//        pieDataSet.setValueLinePart1Length(0.3f);
//        pieDataSet.setValueLinePart2Length(0.4f);
        //最终数据 PieData
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);            //设置是否显示数据实体(百分比，true:以下属性才有意义)
        pieData.setValueFormatter(new PercentFormatter("###,###,##0.00"));//设置所有DataSet内数据实体（百分比）的文本字体格式
        return pieData;
    }

    private List<ComparisonMapView.IComparisonMapData> getComparisonMapData(List<Float> outData, List<Float> inData) {
        List<Float> list = new ArrayList<>();
        list.addAll(outData);
        list.addAll(inData);
        float max = 0;
        for (int i = 0, s = list.size(); i < s; i++) {
            float idata = list.get(i);
            max = max < idata ? idata : max;
        }
        List<ComparisonMapView.IComparisonMapData> datas = new ArrayList<>();
        datas.add(new ComparisonMapData("大单", inData.get(0), mIn1Color, outData.get(0), mOut1Color, max));
        datas.add(new ComparisonMapData("中单", inData.get(1), mIn2Color, outData.get(1), mOut2Color, max));
        datas.add(new ComparisonMapData("小单", inData.get(2), mIn3Color, outData.get(2), mOut3Color, max));
        return datas;
    }

    public void setData(List<Float> outData, List<Float> inData) {
        vComparisonMap.setData(getComparisonMapData(outData, inData));
        vPieChart.setData(getPieData(getPieEntrys(outData, inData)));
        vPieChart.highlightValues(null);
        vPieChart.invalidate();                    //将图表重绘以显示设置的属性和数据
    }


    class ComparisonMapData implements ComparisonMapView.IComparisonMapData {

        private final CharSequence title;
        private final float value1;
        private final int v1Color;
        private final float value2;
        private final int v2Color;
        private final float maxValue;

        public ComparisonMapData(CharSequence title, float value1, int v1Color, float value2, int v2Color, float maxValue) {
            this.title = title;
            this.value1 = value1;
            this.v1Color = v1Color;
            this.value2 = value2;
            this.v2Color = v2Color;
            this.maxValue = maxValue;
        }

        @Override
        public CharSequence getTitle() {
            return title;
        }

        @Override
        public float getValue1() {
            return value1;
        }

        @Override
        public int getValue1Color() {
            return v1Color;
        }

        @Override
        public float getValue2() {
            return value2;
        }

        @Override
        public int getValue2Color() {
            return v2Color;
        }

        @Override
        public float getMaxValue() {
            return maxValue;
        }
    }


}
