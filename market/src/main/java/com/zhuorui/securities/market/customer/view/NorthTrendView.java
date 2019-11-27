package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.zhuorui.securities.market.R;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/28
 * Desc:市场南向北向趋势图表
 * */
public class NorthTrendView extends FrameLayout {

    private TextView tv_trend;
    private TextView tv_sh_trend;
    private TextView tv_sz_trend;

    public NorthTrendView(Context context) {
        this(context,null);
    }

    public NorthTrendView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public NorthTrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_north_trend, this);
        tv_trend = (TextView)findViewById(R.id.tv_trend);
        tv_sh_trend = (TextView)findViewById(R.id.tv_sh_trend);
        tv_sz_trend = (TextView)findViewById(R.id.tv_sz_trend);
    }

   public void setType(int type){
         if(type==1){
             tv_trend.setText("北向");
             tv_sh_trend.setText("沪股通");
             tv_sz_trend.setText("深股通");
         }else{
             tv_trend.setText("南向");
             tv_sh_trend.setText("沪股通(沪)");
             tv_sz_trend.setText("深股通(深)");
         }
   }
}
