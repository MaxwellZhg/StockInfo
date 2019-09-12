package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-09-12 10:44
 * desc   : 金额显示控件
 */
public class ZRAmountTextView extends AppCompatTextView {

    private boolean showSymbol;
    private boolean relativeDecimal = true;
    private int decimalDigit = 2;
    boolean isInit;

    public ZRAmountTextView(Context context) {
        this(context, null);
    }

    public ZRAmountTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZRAmountTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZRAmountTextView);
        showSymbol = a.getBoolean(R.styleable.ZRAmountTextView_zr_showSymbol, showSymbol);
        relativeDecimal = a.getBoolean(R.styleable.ZRAmountTextView_zr_relativeDecimal, relativeDecimal);
        decimalDigit = a.getInt(R.styleable.ZRAmountTextView_zr_decimalDigit, decimalDigit);
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
            final float amt = Float.parseFloat(text.toString());
            final String format = (showSymbol ? "%+" : "%") + ",." + decimalDigit + "f";
            final String tx = String.format(format, amt);
            super.setText(relativeDecimal ? changTVsize(tx) : tx, type);
        } catch (Exception e) {
            super.setText(text, type);
        }
    }

    public static SpannableString changTVsize(String value) {
        SpannableString spannableString = new SpannableString(value);
        if (value.contains(".")) {
            spannableString.setSpan(new RelativeSizeSpan(0.8f), value.indexOf("."), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
