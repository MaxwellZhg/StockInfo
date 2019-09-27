package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import androidx.appcompat.widget.AppCompatTextView;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;

import java.math.BigDecimal;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/26
 * Desc:
 */
public class ZRStockTextView extends AppCompatTextView {
    private boolean isInit;
    // 0无涨跌 1涨 2跌
    private int diffState = 0;
    // 是否在需要在数字之前添加“+”、“-”，如股价跌涨幅 +1.03，-1.66
    private boolean isUpDown;
    // 是否支持百分号
    private boolean isSymbol;

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
        isInit = true;
        String tx = getText().toString();
        if (!TextUtils.isEmpty(tx)) {
            setText(tx);
        }

    }

    /**
     * 设置显示内容
     *
     * @param text      内容
     * @param diffState 根据类型显示不同的颜色 0无涨跌 1涨 2跌
     * @param       a {@link android.widget.TextView.BufferType} which defines whether the text is
     *                  stored as a static text, styleable/spannable text, or editable text
     */
    public void setText(CharSequence text, int diffState) {
        if (!isInit) {
            super.setText(text);
            return;
        }
        try {
            String formatStr;
            // 支持百分号，如跌涨幅 +1.009%
            if (isSymbol) {
                final BigDecimal amt = new BigDecimal(text.toString());
                if (amt.compareTo(BigDecimal.ZERO) > 0) {
                    final String format = "+%s";
                    formatStr = String.format(format, text.toString());
                } else {
                    formatStr = text.toString();
                }
            }
            // 不支持百分号，如股价 1.33
            else {
                final BigDecimal amt = new BigDecimal(text.toString());
                if (amt.compareTo(BigDecimal.ZERO) > 0 && isUpDown) {
                    final String format = "+%s";
                    formatStr = String.format(format, text.toString());
                } else {
                    formatStr = text.toString();
                }
            }
            super.setText(changTvColor(formatStr, diffState, isSymbol));
        } catch (Exception e) {
            Log.e("tttttt", e.toString());
            super.setText(text);
        }
    }

    /**
     * 设置字体颜色
     *
     * @param value
     * @param diffState 0无涨跌 1涨 2跌
     * @param isSymbol
     * @return
     */
    public static SpannableString changTvColor(String value, int diffState, boolean isSymbol) {
        String str = "";
        SpannableString spannableString;
        if (isSymbol) {
            str = value + "%";
            spannableString = new SpannableString(str);
        } else {
            spannableString = new SpannableString(value);
        }

        int color;
        if (diffState == 0) {
            color = LocalSettingsConfig.Companion.read().getUpDownColor(new BigDecimal(0));
        } else if (diffState == 1) {
            color = LocalSettingsConfig.Companion.read().getUpColor();
        } else {
            color = LocalSettingsConfig.Companion.read().getDownColor();
        }
        if (isSymbol) {
            spannableString.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannableString.setSpan(new ForegroundColorSpan(color), 0, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}