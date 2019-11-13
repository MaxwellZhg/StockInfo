package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.customer.MainTradeYearsInfoPopWindow;
import com.zhuorui.securities.market.net.response.FinancialReportResponse;
import com.zhuorui.securities.market.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/6
 * Desc:
 */
public class MarketF10FinancinalMainTradeView extends FrameLayout implements View.OnClickListener,MainTradeYearsInfoPopWindow.OnYearInfoCallBack {
    ArrayList<FinancialReportResponse.BusinessReport> listinfo;
    ArrayList<Float> pieData=new ArrayList<>();
    private TextView tv_years;
    private int mDateNum = 1;
    private MarketPieChatView pie_cahart_view;
    private FinancialReportResponse.Business business;
    private TextView tv_frist_income;
    private TextView tv_frist_income_count;
    private TextView tv_sec_income;
    private TextView tv_sec_income_count;
    private TextView tv_thrid_income;
    private TextView tv_thrid_income_count;
    private TextView tv_four_income;
    private TextView tv_four_income_count;

    public MarketF10FinancinalMainTradeView(Context context) {
        this(context,null);
    }

    public MarketF10FinancinalMainTradeView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MarketF10FinancinalMainTradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_f10_financinal_main_trade,this);
        initView();
    }

    private void initView() {
        tv_years = findViewById(R.id.tv_years);
        tv_years.setOnClickListener(this);
        pie_cahart_view = findViewById(R.id.pie_cahart_view);
        tv_frist_income = findViewById(R.id.tv_frist_income);
        tv_frist_income_count = findViewById(R.id.tv_frist_income_count);
        tv_sec_income = findViewById(R.id.tv_sec_income);
        tv_sec_income_count = findViewById(R.id.tv_sec_income_count);
        tv_thrid_income = findViewById(R.id.tv_thrid_income);
        tv_thrid_income_count = findViewById(R.id.tv_thrid_income_count);
        tv_four_income = findViewById(R.id.tv_four_income);
        tv_four_income_count = findViewById(R.id.tv_four_income_count);
    }

    public void setData(List<Float> outData) {
        //将图表重绘以显示设置的属性和数据
        pie_cahart_view.setData(outData,null);
    }

    public void setPieCharBean(FinancialReportResponse.Business business){
         this.business = business;
        listinfo= business.get20181231();
        detailData(listinfo);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_years) {
            MainTradeYearsInfoPopWindow.Companion.create(getContext(),mDateNum,this).showAsDropDown(tv_years);
        }
    }

    @Override
    public void onYearsInfoClick(int num) {
        mDateNum = num;
        upDateNumText();
       // getTestData();
    }

    private void upDateNumText() {
        switch (mDateNum){
            case 1:
                tv_years.setText("2018年报");
                listinfo= business.get20181231();
                detailData(listinfo);
                break;
            case 2:
                tv_years.setText("2018中报");
                listinfo= business.get20180630();
                detailData(listinfo);
                break;
            case 3:
                tv_years.setText("2017年报");
                listinfo= business.get20171231();
                detailData(listinfo);
                break;
            case 4:
                tv_years.setText("2017中报");
                listinfo= business.get20170630();
                detailData(listinfo);
                break;
            case 5:
                tv_years.setText("2016年报");
                listinfo= business.get20161231();
                detailData(listinfo);
                break;
        }
    }

    public void detailData(ArrayList<FinancialReportResponse.BusinessReport> listinfo){
          pieData.clear();
         for(int i=0;i<listinfo.size();i++){
             pieData.add(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(i).getCurrentYearTurnover()));
         }
         setData(pieData);
         if(pieData.size()>3){
             tv_frist_income.setText(listinfo.get(0).getBusinessCate());
             tv_frist_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(0).getCurrentYearTurnover())+"亿元");
             tv_sec_income.setText(listinfo.get(1).getBusinessCate());
             tv_sec_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(1).getCurrentYearTurnover())+"亿元");
             tv_thrid_income.setText(listinfo.get(2).getBusinessCate());
             tv_thrid_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(2).getCurrentYearTurnover())+"亿元");
             tv_four_income.setText(listinfo.get(3).getBusinessCate());
             tv_four_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(3).getCurrentYearTurnover())+"亿元");
         }else if(pieData.size()==3){
             tv_frist_income.setText(listinfo.get(0).getBusinessCate());
             tv_frist_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(0).getCurrentYearTurnover())+"亿元");
             tv_sec_income.setText(listinfo.get(1).getBusinessCate());
             tv_sec_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(1).getCurrentYearTurnover())+"亿元");
             tv_thrid_income.setText(listinfo.get(2).getBusinessCate());
             tv_thrid_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(2).getCurrentYearTurnover())+"亿元");
             tv_four_income.setText("--");
             tv_four_income_count.setText("--");
         }else{
             tv_frist_income.setText("--");
             tv_frist_income_count.setText("--");
             tv_sec_income.setText("--");
             tv_sec_income_count.setText("--");
             tv_thrid_income.setText("--");
             tv_thrid_income_count.setText("--");
             tv_four_income.setText("--");
             tv_four_income_count.setText("--");
         }
    }


}
