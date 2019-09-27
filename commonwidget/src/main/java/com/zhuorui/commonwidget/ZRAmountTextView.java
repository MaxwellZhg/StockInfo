package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

import java.math.BigDecimal;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-09-12 10:44
 * desc   : 金额显示控件 支持逗号分割、缩放显示小数、指定显示几位小数
 */
public class ZRAmountTextView extends AppCompatTextView {

    private boolean showSymbol;//是否显示正数符号
    private boolean relativeDecimal = true;//是否缩小显示小数位
    private boolean keepDecimalComplements = true;//是否保留小数补位
    private int decimalDigit = 2;//小数保留位数，不足补0
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
        keepDecimalComplements = a.getBoolean(R.styleable.ZRAmountTextView_zr_keepDecimalComplements, keepDecimalComplements);
        isInit = true;
        String tx = getText().toString();
        if (!TextUtils.isEmpty(tx)) {
            setText(tx);
        }
        a.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!isInit) {
            super.setText(text, type);
            return;
        }
        try {
            final BigDecimal amt = new BigDecimal(text.toString());
            final String format = (showSymbol ? "%+" : "%") + ",." + decimalDigit + "f";
            final String tx = keepDecimalComplements ? String.format(format, amt) : subZeroAndDot(String.format(format, amt));
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

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
