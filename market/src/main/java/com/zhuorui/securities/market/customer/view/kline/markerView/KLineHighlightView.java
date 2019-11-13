package com.zhuorui.securities.market.customer.view.kline.markerView;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.customer.view.kline.model.KLineDataModel;
import com.zhuorui.securities.market.ui.kline.IKLineHighlightView;
import org.jetbrains.annotations.NotNull;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-11-11 16:47
 * desc   :
 */
public class KLineHighlightView extends FrameLayout implements IKLineHighlightView {

    private int defColor = Color.parseColor("#C0CCE0");
    private TextView vHighPrice;
    private TextView vLowPrice;
    private TextView vOpenPrice;
    private TextView vClosePrice;
    private TextView vDiffPrice;
    private TextView vDiffRate;
    private TextView vVolume;
    private TextView vTotalPrice;
    private LocalSettingsConfig config;

    public KLineHighlightView(Context context) {
        this(context, null);
    }

    public KLineHighlightView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KLineHighlightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_kline_highlight, this);
        vHighPrice = findViewById(R.id.tv_high_price);
        vLowPrice = findViewById(R.id.tv_low_price);
        vOpenPrice = findViewById(R.id.tv_open_price);
        vClosePrice = findViewById(R.id.tv_close_price);
        vDiffPrice = findViewById(R.id.tv_diff_price);
        vDiffRate = findViewById(R.id.tv_diff_rate);
        vVolume = findViewById(R.id.tv_volume);
        vTotalPrice = findViewById(R.id.tv_total_price);
        config = LocalSettingsConfig.Companion.getInstance();
    }


    @Override
    public void setData(@NotNull Object data) {
        KLineDataModel tData = (KLineDataModel) data;
        final double closeprice = tData.getClose();
        final double preClose = tData.getPreClose();
        int color = config.getUpDownColor(closeprice, preClose, defColor);
        vClosePrice.setTextColor(color);
        vDiffPrice.setTextColor(color);
        vDiffRate.setTextColor(color);
        final double diffPrice = closeprice - preClose;
        final double diffRate = diffPrice / preClose * 100;
        vClosePrice.setText(String.format("%.3f", closeprice));
        vDiffPrice.setText(String.format("%+.3f", diffPrice));
        vDiffRate.setText(String.format("%+.2f%%", diffRate));
        setText(vOpenPrice, tData.getOpen(), preClose);
        setText(vHighPrice, tData.getHigh(), preClose);
        setText(vLowPrice, tData.getLow(), preClose);
        vVolume.setText(String.valueOf(tData.getVolume()));
        vTotalPrice.setText(String.valueOf(tData.getTotal()));
    }

    private void setText(TextView v, double value, double preClose) {
        v.setTextColor(config.getUpDownColor(value, preClose, defColor));
        v.setText(String.format("%.3f", value));
    }

    @NotNull
    @Override
    public View getView() {
        return this;
    }
}
