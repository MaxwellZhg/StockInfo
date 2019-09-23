package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/19
 * Desc:
 */
public class ZREmptyView extends FrameLayout {
    private View emptyView;
    private View emptyTipsView;
    public ZREmptyView(Context context) {
        this(context,null);
    }

    public ZREmptyView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZREmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZREmptyView);
        int width=a.getDimensionPixelOffset(R.styleable.ZREmptyView_empty_img_width,64);
        int height = a.getDimensionPixelOffset(R.styleable.ZREmptyView_empty_img_height,64);
        int resId=a.getResourceId(R.styleable.ZREmptyView_empty_img_src,-1);
        int margin = a.getDimensionPixelOffset(R.styleable.ZREmptyView_empty_img_tips_margin,42);
        String tips=a.getString(R.styleable.ZREmptyView_empty_img_tips);
        setEmptyView(getEmptyView(width,height,resId));
        setEmptyTipsView(getEmptyTipsView(margin));
        setTips(tips);
        a.recycle();
    }

    private void setTips(String tips) {
        if (emptyTipsView != null && emptyTipsView instanceof TextView) {
            ((TextView) emptyTipsView).setText(tips);
        }
    }

    private void setEmptyTipsView(View v) {
        if (v == null) return;
        emptyTipsView = v;
        addView(v);
    }

    private View getEmptyTipsView(int margin) {
        TextView tv = new TextView(getContext());
        tv.setTextColor(Color.parseColor("#cccccc"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        tv.setLayoutParams(lp);
        tv.setPadding(0,0,20,0);
        lp.topMargin=margin;
        return tv;
    }

    private void setEmptyView(View v) {
        if (v == null) return;
        emptyView = v;
        addView(v);
    }

    private View getEmptyView(int width,int height,int resId) {
        ImageView iv = new ImageView(getContext());
        iv.setImageResource(resId);
        FrameLayout.LayoutParams lp = new LayoutParams(width, height, Gravity.CENTER);
        iv.setLayoutParams(lp);
        return iv;
     }
}
