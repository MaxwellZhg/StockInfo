package com.zhuorui.securities.market.customer.view.kline;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.customer.view.kline.model.TimeDataModel;
import com.zhuorui.securities.market.ui.kline.IKLineHighlightView;
import org.jetbrains.annotations.NotNull;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-11-11 16:47
 * desc   :
 */
public class KLineDayHighlightView extends FrameLayout implements IKLineHighlightView {

    private int defColor = Color.parseColor("#C0CCE0");
    private TextView vPrice;
    private TextView vDiffPrice;
    private TextView vDiffRate;
    private TextView vVolume;
    private TextView vAveragePrice;
    private LocalSettingsConfig config;

    public KLineDayHighlightView(Context context) {
        this(context, null);
    }

    public KLineDayHighlightView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KLineDayHighlightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_kline_day_highlight, this);
        vPrice = findViewById(R.id.tv_price);
        vDiffPrice = findViewById(R.id.tv_diff_pirce);
        vDiffRate = findViewById(R.id.tv_diff_rate);
        vVolume = findViewById(R.id.tv_volume);
        vAveragePrice = findViewById(R.id.tv_average_price);
        config = LocalSettingsConfig.Companion.getInstance();
    }


    @Override
    public void setData(@NotNull Object data) {
        TimeDataModel tData = (TimeDataModel) data;
        final double price = tData.getNowPrice();
        final double preClose = tData.getPreClose();
        final double averagePrice = tData.getAveragePrice();
        int color = config.getUpDownColor(price, preClose, defColor);
        vPrice.setTextColor(color);
        vDiffPrice.setTextColor(color);
        vDiffRate.setTextColor(color);
        color = config.getUpDownColor(averagePrice, preClose, defColor);
        vAveragePrice.setTextColor(color);
        final double diffPrice = price - preClose;
        final double diffRate = diffPrice / preClose * 100;
        vPrice.setText(String.format("%.3f", price));
        vDiffPrice.setText(String.format("%+.3f", diffPrice));
        vDiffRate.setText(String.format("%+.2f%%", diffRate));
        vVolume.setText(String.valueOf(tData.getVolume()));
        vAveragePrice.setText(String.format("%.3f", averagePrice));
    }

    @NotNull
    @Override
    public View getView() {
        return this;
    }
}
