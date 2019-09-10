package com.zhuorui.securities.personal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhuorui.commonwidget.ZhuoRuiTopBar;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/9
 * Desc:
 */
public class SetTitleBar extends ZhuoRuiTopBar {
    private  View mRightTextView;
    public SetTitleBar(Context context) {
        this(context, null);
    }

    public SetTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SetTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, com.zhuorui.commonwidget.R.styleable.SettingTitleBar);
        String res = a.getString(com.zhuorui.commonwidget.R.styleable.SettingTitleBar_zr_set_title_bar_text);
        int margin = a.getDimensionPixelOffset(com.zhuorui.commonwidget.R.styleable.SettingTitleBar_zr_set_title_bar_margin, 14);
        setRightTextView(getRightTextView(margin));
        setRightText(res);
        a.recycle();
    }

    private void setRightText(String res) {
        if (mRightTextView != null && mRightTextView instanceof TextView) {
            ((TextView) mRightTextView).setText(res);
        }
    }


    private void setRightTextView(View v) {
        if (v == null) return;
        mRightTextView = v;
        addView(v);
    }

    private View getRightTextView(int margin) {
        TextView tv = new TextView(getContext());
        tv.setTextColor(Color.parseColor("#1A6ED2"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.END|Gravity.CENTER);
        lp.rightMargin = margin;
        tv.setLayoutParams(lp);
        return tv;
    }
    public void setRightTextViewClickListener(OnClickListener l) {
        if (mRightTextView != null) mRightTextView.setOnClickListener(l);
    }

}
