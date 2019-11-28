package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.zhuorui.securities.market.R;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/22
 * Desc:板块消息组件
 * */
public class MarketPartInfoView extends FrameLayout {

    private TextView tv_part_info;
    private int type;

    public MarketPartInfoView(Context context) {
        this(context,null);
    }

    public MarketPartInfoView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MarketPartInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a= context.obtainStyledAttributes(attrs,R.styleable.MarketPartInfoView);
        type = a.getInteger(R.styleable.MarketPartInfoView_market_info_type,-1);
        inflate(context, R.layout.layout_markt_part_info,this);
        initView();
        getTestData();
    }

    private void initView() {
        tv_part_info = findViewById(R.id.tv_part_info);
        if(type==1) {
            tv_part_info.setText("行业板块");
        }else{
            tv_part_info.setText("概念板块");
        }
    }

    private void  getTestData(){

    }
}
