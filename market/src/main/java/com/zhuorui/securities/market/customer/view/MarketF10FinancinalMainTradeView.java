package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.github.mikephil.charting.data.PieEntry;
import com.zhuorui.securities.base2app.util.ResUtil;
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
public class MarketF10FinancinalMainTradeView extends FrameLayout implements View.OnClickListener, MainTradeYearsInfoPopWindow.OnYearInfoCallBack {
    ArrayList<FinancialReportResponse.BusinessReport> listinfo1;
    ArrayList<FinancialReportResponse.BusinessReport> listinfo2;
    ArrayList<String> showDate = new ArrayList<>();
    ArrayList<Float> pieData = new ArrayList<>();
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
        this(context, null);
    }

    public MarketF10FinancinalMainTradeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarketF10FinancinalMainTradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_f10_financinal_main_trade, this);
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
        ArrayList<Integer> colors = new ArrayList();
        colors.add(Color.parseColor("#EEA74D"));
        colors.add(Color.parseColor("#5DA6F2"));
        colors.add(Color.parseColor("#3A79C8"));
        colors.add(Color.parseColor("#6F6F6F"));
        pie_cahart_view.setColors(colors);
    }

    public void setData(List<Float> outData) {
        //将图表重绘以显示设置的属性和数据
        pie_cahart_view.setData(getOutPieEntrys(outData));
    }


    private ArrayList<PieEntry> getOutPieEntrys(List<Float> out) {
        ArrayList<PieEntry> pieEntryList = new ArrayList<>();
        //饼图实体 PieEntry
        if (out.size() > 0 && out.size() == 4) {
            if (out.get(0) > 0) pieEntryList.add(new PieEntry(out.get(0), "寿险及健康险"));
            if (out.get(1) > 0) pieEntryList.add(new PieEntry(out.get(1), "财产保险"));
            if (out.get(2) > 0) pieEntryList.add(new PieEntry(out.get(2), "银行"));
            if (out.get(3) > 0) pieEntryList.add(new PieEntry(out.get(3), "其他收入"));
        } else if (out.size() > 0 && out.size() == 3) {
            if (out.get(0) > 0) pieEntryList.add(new PieEntry(out.get(0), "寿险及健康险"));
            if (out.get(1) > 0) pieEntryList.add(new PieEntry(out.get(1), "财产保险"));
            if (out.get(2) > 0) pieEntryList.add(new PieEntry(out.get(2), "银行"));
        }
        return pieEntryList;
    }

    public void setPieCharBean(FinancialReportResponse.Business business) {
        this.business = business;
        if (this.business != null) {
            listinfo1 = business.get20190630();
            listinfo2 = business.get20181231();
            if (listinfo1 != null && listinfo2 != null) {
                detailData(listinfo1);
                showDate.add("2019" + ResUtil.INSTANCE.getString(R.string.middele_years_report));
                showDate.add("2018" + ResUtil.INSTANCE.getString(R.string.all_years_report));
                showDate.add("2018" + ResUtil.INSTANCE.getString(R.string.middele_years_report));
                showDate.add("2017" + ResUtil.INSTANCE.getString(R.string.all_years_report));
                showDate.add("2017" + ResUtil.INSTANCE.getString(R.string.middele_years_report));
                tv_years.setText(showDate.get(0));
            } else if (listinfo1 == null && listinfo2 != null) {
                showDate.add("2018" + ResUtil.INSTANCE.getString(R.string.all_years_report));
                showDate.add("2018" + ResUtil.INSTANCE.getString(R.string.middele_years_report));
                showDate.add("2017" + ResUtil.INSTANCE.getString(R.string.all_years_report));
                showDate.add("2017" + ResUtil.INSTANCE.getString(R.string.middele_years_report));
                showDate.add("2016" + ResUtil.INSTANCE.getString(R.string.all_years_report));
                tv_years.setText(showDate.get(0));
                detailData(listinfo2);
            } else {
                showDate.add("2018" + ResUtil.INSTANCE.getString(R.string.all_years_report));
                showDate.add("2018" + ResUtil.INSTANCE.getString(R.string.middele_years_report));
                showDate.add("2017" + ResUtil.INSTANCE.getString(R.string.all_years_report));
                showDate.add("2017" + ResUtil.INSTANCE.getString(R.string.middele_years_report));
                showDate.add("2016" + ResUtil.INSTANCE.getString(R.string.all_years_report));
                ArrayList<FinancialReportResponse.BusinessReport> listinfo = new ArrayList<>();
                tv_years.setText(showDate.get(0));
                detailData(listinfo);
            }
        } else {
            showDate.add("2018" + ResUtil.INSTANCE.getString(R.string.all_years_report));
            showDate.add("2018" + ResUtil.INSTANCE.getString(R.string.middele_years_report));
            showDate.add("2017" + ResUtil.INSTANCE.getString(R.string.all_years_report));
            showDate.add("2017" + ResUtil.INSTANCE.getString(R.string.middele_years_report));
            showDate.add("2016" + ResUtil.INSTANCE.getString(R.string.all_years_report));
            tv_years.setText(showDate.get(0));
            ArrayList<FinancialReportResponse.BusinessReport> listinfo = new ArrayList<>();
            detailData(listinfo);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tv_years) {
            MainTradeYearsInfoPopWindow.Companion.create(getContext(), mDateNum, showDate, this).showAsDropDown(tv_years);
        }
    }

    @Override
    public void onYearsInfoClick(int num) {
        mDateNum = num;
        upDateNumText();
        // getTestData();
    }

    private void upDateNumText() {
        switch (mDateNum) {
            case 1:
                tv_years.setText(showDate.get(0));
                if (business != null) {
                    if (showDate.get(0).contains("2019")) {
                        listinfo1 = business.get20190630();
                    } else {
                        listinfo1 = business.get20181231();
                    }
                }
                if (listinfo1 != null) {
                    detailData(listinfo1);
                } else {
                    ArrayList<FinancialReportResponse.BusinessReport> listinfo = new ArrayList<>();
                    detailData(listinfo);
                }
                break;
            case 2:
                tv_years.setText(showDate.get(1));
                if (business != null) {
                    if (showDate.get(0).contains("2019")) {
                        listinfo1 = business.get20181231();
                    } else {
                        listinfo1 = business.get20180630();
                    }
                }
                if (listinfo1 != null) {
                    detailData(listinfo1);
                } else {
                    ArrayList<FinancialReportResponse.BusinessReport> listinfo = new ArrayList<>();
                    detailData(listinfo);
                }
                break;
            case 3:
                tv_years.setText(showDate.get(2));
                if (business != null) {
                    if (showDate.get(0).contains("2019")) {
                        listinfo1 = business.get20190630();
                    } else {
                        listinfo1 = business.get20171231();
                    }
                }
                if (listinfo1 != null) {
                    detailData(listinfo1);
                } else {
                    ArrayList<FinancialReportResponse.BusinessReport> listinfo = new ArrayList<>();
                    detailData(listinfo);
                }
                break;
            case 4:
                tv_years.setText(showDate.get(3));
                if (business != null) {
                    if (showDate.get(0).contains("2019")) {
                        listinfo1 = business.get20171231();
                    } else {
                        listinfo1 = business.get20170630();
                    }
                }
                if (listinfo1 != null) {
                    detailData(listinfo1);
                } else {
                    ArrayList<FinancialReportResponse.BusinessReport> listinfo = new ArrayList<>();
                    detailData(listinfo);
                }
                break;
            case 5:
                tv_years.setText(showDate.get(4));
                if (business != null) {
                    if (showDate.get(0).contains("2019")) {
                        listinfo1 = business.get20170630();
                    } else {
                        listinfo1 = business.get20161231();
                    }
                }
                if (listinfo1 != null) {
                    detailData(listinfo1);
                } else {
                    ArrayList<FinancialReportResponse.BusinessReport> listinfo = new ArrayList<>();
                    detailData(listinfo);
                }
                break;
        }
    }

    public void detailData(ArrayList<FinancialReportResponse.BusinessReport> listinfo) {
        pieData.clear();
        for (int i = 0; i < listinfo.size(); i++) {
            if (MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(i).getCurrentYearTurnover()) < 0f) {
                pieData.add(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(i).getCurrentYearTurnover()) * -1f);
            } else {
                pieData.add(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(i).getCurrentYearTurnover()));
            }
        }
        setData(pieData);
        if (pieData.size() > 3) {
            tv_frist_income.setText(listinfo.get(0).getBusinessCate());
            tv_frist_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(0).getCurrentYearTurnover()) + "亿元");
            tv_sec_income.setText(listinfo.get(1).getBusinessCate());
            tv_sec_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(1).getCurrentYearTurnover()) + "亿元");
            tv_thrid_income.setText(listinfo.get(2).getBusinessCate());
            tv_thrid_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(2).getCurrentYearTurnover()) + "亿元");
            tv_four_income.setText(listinfo.get(3).getBusinessCate());
            tv_four_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(3).getCurrentYearTurnover()) + "亿元");
        } else if (pieData.size() == 3) {
            tv_frist_income.setText(listinfo.get(0).getBusinessCate());
            tv_frist_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(0).getCurrentYearTurnover()) + "亿元");
            tv_sec_income.setText(listinfo.get(1).getBusinessCate());
            tv_sec_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(1).getCurrentYearTurnover()) + "亿元");
            tv_thrid_income.setText(listinfo.get(2).getBusinessCate());
            tv_thrid_income_count.setText(MathUtil.INSTANCE.convertToUnitFloat(listinfo.get(2).getCurrentYearTurnover()) + "亿元");
            tv_four_income.setText("--");
            tv_four_income_count.setText("--");
        } else {
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
