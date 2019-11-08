package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import androidx.appcompat.widget.AppCompatTextView;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.commonwidget.model.Observer;
import com.zhuorui.commonwidget.model.Subject;

import java.math.BigDecimal;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/26
 * Desc:
 */
public class ZRStockTextView extends AppCompatTextView implements Observer {
    // 0无涨跌 1涨 2跌
    private int diffState = 0;
    // 是否在需要在数字之前添加“+”、“-”，如股价跌涨幅 +1.03，-1.66
    private boolean isUpDown;
    // 是否支持百分号
    private boolean isSymbol;
    private String originValue;
    private LocalSettingsConfig settingsConfig;

    public ZRStockTextView(Context context) {
        this(context, null);
    }

    public ZRStockTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZRStockTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZRStockTextView);
        isSymbol = a.getBoolean(R.styleable.ZRStockTextView_zr_showPrecentSymbol, false);
        isUpDown = a.getBoolean(R.styleable.ZRStockTextView_zr_showPrecentUpDown, false);
        a.recycle();
        settingsConfig = LocalSettingsConfig.Companion.read();
        // 监听修改配置颜色
        settingsConfig.registerObserver(this);
    }

    /**
     * 设置显示内容
     *
     * @param text      内容
     * @param diffState 根据类型显示不同的颜色 0无涨跌 1涨 2跌
     * @param {@link    android.widget.TextView.BufferType} which defines whether the text is
     *                  stored as a static text, styleable/spannable text, or editable text
     */
    public void setText(CharSequence text, int diffState) {
        try {
            this.diffState = diffState;
            // 支持百分号，如跌涨幅 +1.009%
            if (isSymbol) {
                final BigDecimal amt = new BigDecimal(text.toString());
                if (amt.compareTo(BigDecimal.ZERO) > 0) {
                    final String format = "+%s";
                    originValue = String.format(format, text.toString());
                } else {
                    originValue = text.toString();
                }
            }
            // 不支持百分号，如股价 1.33
            else {
                final BigDecimal amt = new BigDecimal(text.toString());
                if (amt.compareTo(BigDecimal.ZERO) > 0 && isUpDown) {
                    final String format = "+%s";
                    originValue = String.format(format, text.toString());
                } else {
                    originValue = text.toString();
                }
            }
            super.setText(changTvColor());
        } catch (Exception e) {
            originValue = text.toString();
            super.setText(changTvColor());
        }
    }

    /**
     * 设置字体颜色
     */
    public SpannableString changTvColor() {
        String str = "";
        SpannableString spannableString;
        if (isSymbol) {
            str = originValue + "%";
            spannableString = new SpannableString(str);
        } else {
            spannableString = new SpannableString(originValue);
        }

        int color;
        if (diffState == 0) {
            color = settingsConfig.getDefaultColor();
        } else if (diffState == 1) {
            color = settingsConfig.getUpColor();
        } else {
            color = settingsConfig.getDownColor();
        }
        if (isSymbol) {
            spannableString.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannableString.setSpan(new ForegroundColorSpan(color), 0, originValue.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    @Override
    public void update(Subject subject) {
        settingsConfig = (LocalSettingsConfig) subject;
        // 更改字体颜色
        super.setText(changTvColor());
    }

    @Override
    protected void onDetachedFromWindow() {
        if (settingsConfig != null)
            settingsConfig.removeObserver(this);
        super.onDetachedFromWindow();
    }
}