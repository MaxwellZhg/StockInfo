package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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
    boolean isInit;
    private boolean isUpDown;
    private int state = 0;
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
        a.recycle();
        isInit = true;
        String tx = getText().toString();
        if (!TextUtils.isEmpty(tx)) {
            setText(tx);
        }

    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!isInit) {
            super.setText(text, type);
            return;
        }
        try {
            if (isSymbol && state == 0) {
                final BigDecimal amt = new BigDecimal(text.toString());
                if (amt.compareTo(BigDecimal.ZERO) > 0) {
                    final String format = "+%s";
                    final String tx = String.format(format, text.toString());
                    super.setText(changTvColor(tx, state, isSymbol), type);
                } else if (amt.compareTo(BigDecimal.ZERO) < 0) {
                    super.setText(changTvColor(text.toString(), state, isSymbol), type);
                } else {
                    final String format = "+%s";
                    final String tx = String.format(format, text.toString());
                    super.setText(changTvColor(tx, state, isSymbol), type);
                }
            } else if (!isSymbol && state == 0) {
                final BigDecimal amt = new BigDecimal(text.toString());
                if (amt.compareTo(BigDecimal.ZERO) > 0) {
                    final String format = "+%s";
                    final String tx = String.format(format, text.toString());
                    super.setText(changTvColor(tx, state, isSymbol), type);
                } else if (amt.compareTo(BigDecimal.ZERO) < 0) {
                    super.setText(changTvColor(text.toString(), state, isSymbol), type);
                } else {
                    final String format = "+%s";
                    final String tx = String.format(format, text.toString());
                    super.setText(changTvColor(tx, state, isSymbol), type);
                }
            } else if (!isSymbol && state == 1) {
                super.setText(changTvColor(text.toString(), state, isSymbol), type);
            } else if (!isSymbol && state == 2) {
                super.setText(changTvColor(text.toString(), state, isSymbol), type);
            }
        } catch (Exception e) {
            Log.e("tttttt", e.toString());
            super.setText(text, type);
        }
    }


    public void setUpDownChange(boolean isUpDown) {
        this.isUpDown = isUpDown;
        if (isUpDown) {
            this.state = 1;
        } else {
            this.state = 2;
        }
    }

    public static SpannableString changTvColor(String value, int state, boolean isSymbol) {
        String str = "";
        SpannableString spannableString;
        if (isSymbol && state == 0) {
            str = value + "%";
            spannableString = new SpannableString(str);
        } else {
            spannableString = new SpannableString(value);
        }

        int color;
        if (state == 0) {
            color = LocalSettingsConfig.Companion.read().getUpDownColor(new BigDecimal(value));
        } else if (state == 1) {
            color = LocalSettingsConfig.Companion.read().getUpColor();
        } else {
            color = LocalSettingsConfig.Companion.read().getDownColor();
        }
        if (isSymbol && state == 0) {
            spannableString.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannableString.setSpan(new ForegroundColorSpan(color), 0, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

}
