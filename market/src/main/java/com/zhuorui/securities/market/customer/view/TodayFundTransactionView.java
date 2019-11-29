package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.socket.vo.CapitalData;
import com.zhuorui.securities.market.util.MarketUtil;
import com.zhuorui.securities.market.util.MathUtil;
import com.zhuorui.securities.personal.config.LocalAccountConfig;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-14 13:45
 * desc   : 今日资金成交分布
 */
public class TodayFundTransactionView extends FrameLayout {

    private ComparisonMapView vComparisonMap;
    private MarketPieChatView pie_cahart_view;
    private TextView vUnit;
    private int mOut1Color;
    private int mOut2Color;
    private int mOut3Color;
    private int mIn1Color;
    private int mIn2Color;
    private int mIn3Color;
    private int defColor;
    private int inValueColor;
    private int outValueColor;


    public TodayFundTransactionView(Context context) {
        this(context, null);
    }

    public TodayFundTransactionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TodayFundTransactionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_today_fund_transaction, this);
        initView();
        initColor();
    }

    private void initView() {
        pie_cahart_view = findViewById(R.id.pie_cahart_view);
        vComparisonMap = findViewById(R.id.comparison_map);
        vUnit = findViewById(R.id.tv_unit);
        vComparisonMap.setTitle("流入", "流出", "净流入");
    }

    private void initColor() {
        defColor = Color.parseColor("#C3CDE3");
        inValueColor = LocalSettingsConfig.Companion.getInstance().getUpColor();
        outValueColor = LocalSettingsConfig.Companion.getInstance().getDownColor();
        mOut1Color = Color.parseColor("#00AB3B");
        mOut2Color = Color.parseColor("#336666");
        mOut3Color = Color.parseColor("#339966");
        mIn1Color = Color.parseColor("#C4000A");
        mIn2Color = Color.parseColor("#D73239");
        mIn3Color = Color.parseColor("#CC6666");
    }

    private ArrayList<PieEntry> getPieEntrys(float largeSingleOut, float largeSingleIn, float mediumOut, float mediumIn, float smallOut, float smallIn) {
        ArrayList<PieEntry> pieEntryList = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList();
        //饼图实体 PieEntry
        if (largeSingleOut > 0) {
            pieEntryList.add(new PieEntry(largeSingleOut, "大单流出"));
            colors.add(mOut1Color);
        }
        if (mediumOut > 0) {
            pieEntryList.add(new PieEntry(mediumOut, "中单流出"));
            colors.add(mOut2Color);
        }
        if (smallOut > 0) {
            pieEntryList.add(new PieEntry(smallOut, "小单流出"));
            colors.add(mOut3Color);
        }
        if (smallIn > 0) {
            pieEntryList.add(new PieEntry(smallIn, "小单流入"));
            colors.add(mIn3Color);
        }
        if (mediumIn > 0) {
            pieEntryList.add(new PieEntry(mediumIn, "中单流入"));
            colors.add(mIn2Color);
        }
        if (largeSingleIn > 0) {
            pieEntryList.add(new PieEntry(largeSingleIn, "大单流入"));
            colors.add(mIn1Color);
        }
        pie_cahart_view.setColors(colors);
        return pieEntryList;
    }

    private void setComparisonMapData(float largeSingleOut, float largeSingleIn, float mediumOut, float mediumIn, float smallOut, float smallIn) {
        List<Float> list = new ArrayList<>();
        list.add(largeSingleOut);
        list.add(largeSingleIn);
        list.add(mediumOut);
        list.add(mediumIn);
        list.add(smallOut);
        list.add(smallIn);
        float max = 0;
        for (int i = 0, s = list.size(); i < s; i++) {
            float idata = list.get(i);
            max = max < idata ? idata : max;
        }
        if (max == 0) {
            vComparisonMap.setValueTextColor(defColor, defColor);
        } else {
            vComparisonMap.setValueTextColor(inValueColor, outValueColor);
        }
        List<ComparisonMapView.IComparisonMapData> datas = new ArrayList<>();
        datas.add(new ComparisonMapData("大单", largeSingleIn, mIn1Color, largeSingleOut, mOut1Color, max));
        datas.add(new ComparisonMapData("中单", mediumIn, mIn2Color, mediumOut, mOut2Color, max));
        datas.add(new ComparisonMapData("小单", smallIn, mIn3Color, smallOut, mOut3Color, max));
        vComparisonMap.setData(datas);
    }

    public void setData(CapitalData data) {
        float largeSingleOut = 0f;
        float largeSingleIn = 0f;
        float mediumOut = 0f;
        float mediumIn = 0f;
        float smallOut = 0f;
        float smallIn = 0f;
        if (data != null) {
            BigDecimal largeOutBig = data.getTotalLargeSingleOutflow() == null ? BigDecimal.valueOf(0) : data.getTotalLargeSingleOutflow();
            BigDecimal largeInBig = data.getTotalLargeSingleInflow() == null ? BigDecimal.valueOf(0) : data.getTotalLargeSingleInflow();
            BigDecimal mediumOutBig = data.getTotalMediumOutflow() == null ? BigDecimal.valueOf(0) : data.getTotalMediumOutflow();
            BigDecimal mediumInBig = data.getTotalMediumInflow() == null ? BigDecimal.valueOf(0) : data.getTotalMediumInflow();
            BigDecimal smallOutBig = data.getTotalSmallOutflow() == null ? BigDecimal.valueOf(0) : data.getTotalSmallOutflow();
            BigDecimal smallInBig = data.getTotalSmallInflow() == null ? BigDecimal.valueOf(0) : data.getTotalSmallInflow();
            BigDecimal unit = MarketUtil.getUnitBigDecimal(largeOutBig, largeInBig, mediumOutBig, mediumInBig, smallOutBig, smallInBig);
            largeSingleOut = MathUtil.INSTANCE.divide(largeOutBig, unit, 6).floatValue();
            largeSingleIn = MathUtil.INSTANCE.divide(largeInBig, unit, 6).floatValue();
            mediumOut = MathUtil.INSTANCE.divide(mediumOutBig, unit, 6).floatValue();
            smallOut = MathUtil.INSTANCE.divide(smallOutBig, unit, 6).floatValue();
            smallIn = MathUtil.INSTANCE.divide(smallInBig, unit, 6).floatValue();
            vUnit.setText(String.format(getResources().getString(R.string.unit_yuan),MarketUtil.getUnitName(unit)));
        }else {
            vUnit.setText(String.format(getResources().getString(R.string.unit_yuan),""));
        }
        setComparisonMapData(largeSingleOut, largeSingleIn, mediumOut, mediumIn, smallOut, smallIn);
        pie_cahart_view.setData(getPieEntrys(largeSingleOut, largeSingleIn, mediumOut, mediumIn, smallOut, smallIn));
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
