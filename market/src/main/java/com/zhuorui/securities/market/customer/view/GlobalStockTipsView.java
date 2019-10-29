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
 * Date: 2019/10/29
 * Desc:
 */
public class GlobalStockTipsView extends FrameLayout {

    private int type;
    private TextView tv_tips_info;

    public GlobalStockTipsView(Context context) {
        this(context,null);
    }

    public GlobalStockTipsView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GlobalStockTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a= context.obtainStyledAttributes(attrs, R.styleable.GlobalStockTipsView);
        type = a.getInteger(R.styleable.GlobalStockTipsView_global_info_type,-1);
        inflate(context, R.layout.layout_global_stock_tips,this);
        tv_tips_info = (TextView)findViewById(R.id.tv_tips_info);
    }

    public void setTipsData(int type){
        if(type==1){
            tv_tips_info.setText("常用指数");
        }else if(type==2){
            tv_tips_info.setText("美洲指数");
        }else if(type==3){
            tv_tips_info.setText("欧洲指数");
        }else if(type==4){
            tv_tips_info.setText("亚洲指数");
        }
    }


}
