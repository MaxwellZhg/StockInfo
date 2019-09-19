package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.zhuorui.securities.market.R;

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/9/18 16:55
 * desc   :
 */
public class StockTransDataView extends RelativeLayout {

    private TextView priceText, volumeText;

    public StockTransDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StockTransDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StockTransDataView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StockTransDataView);
        float density = getResources().getDisplayMetrics().density;

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // 创建左侧标题如：买1、卖1
        TextView titleText = new TextView(getContext());
        titleText.setTextColor(Color.WHITE);
        titleText.setTextSize(8);
        int padding = (int) (density * 3);
        titleText.setText(typedArray.getString(R.styleable.StockTransDataView_title_text));
        titleText.setPadding(padding, 0, padding, 0);
        titleText.setBackgroundColor(typedArray.getColor(R.styleable.StockTransDataView_title_background, Color.BLACK));
        params.addRule(ALIGN_PARENT_START);
        params.addRule(CENTER_VERTICAL);
        params.setMarginStart((int) (density * 5));
        titleText.setLayoutParams(params);
        addView(titleText);

        // 创建价格如：56.22
        priceText = new TextView(getContext());
        priceText.setTextColor(typedArray.getColor(R.styleable.StockTransDataView_price_text_color, Color.BLACK));
        priceText.setTextSize(9);
        params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_START);
        params.addRule(CENTER_VERTICAL);
        params.setMarginStart((int) (density * 28));
        priceText.setLayoutParams(params);
        addView(priceText);

        // 创建交易量 如：116.80K(8)、1.23M(455)
        volumeText = new TextView(getContext());
        volumeText.setTextColor(Color.parseColor("#FF282828"));
        volumeText.setTextSize(9);
        params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_END);
        params.addRule(CENTER_VERTICAL);
        params.setMarginEnd((int) (density * 5));
        volumeText.setLayoutParams(params);
        addView(volumeText);
    }

    public void setPriceText(String priceText) {
        this.priceText.setText(priceText);
    }

    public void setVolumeText(String volumeText) {
        this.volumeText.setText(volumeText);
    }
}
