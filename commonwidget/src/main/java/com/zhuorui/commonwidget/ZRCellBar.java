package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
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
 * Date: 2019/9/6
 * Desc:
 */
public class ZRCellBar extends FrameLayout {
    private View mLeftView;
    private View mTitleView;
    private View mRightTipsView;
    public ZRCellBar(Context context) {
        this(context, null);
    }

    public ZRCellBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZRCellBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ZRCellBar);
        String mTitle = a.getString(R.styleable.ZRCellBar_zr_cellbarTitle);
        String tips = a.getString(R.styleable.ZRCellBar_zr_cellbar_right_tips);
        int resleftwidth=a.getDimensionPixelOffset(R.styleable.ZRCellBar_zr_cellbar_left_icon_width,20);
        int resleftheight=a.getDimensionPixelOffset(R.styleable.ZRCellBar_zr_cellbar_left_icon_height,17);
        int resleftmargin=a.getDimensionPixelOffset(R.styleable.ZRCellBar_zr_cellbar_left_icon_margin,13);
        int resrightwidth=a.getDimensionPixelOffset(R.styleable.ZRCellBar_zr_cellbar_righttips_icon_width,9);
        int resrightheight=a.getDimensionPixelOffset(R.styleable.ZRCellBar_zr_cellbar_righttips_icon_height,14);
        int resrightmargin=a.getDimensionPixelOffset(R.styleable.ZRCellBar_zr_cellbar_righttips_icon_margin,13);
        int resLeftId=a.getResourceId(R.styleable.ZRCellBar_zr_cellbar_left_icon,-1);
        int margintitle=a.getDimensionPixelOffset(R.styleable.ZRCellBar_zr_cellbar_title_margin,40);
        int margintips=a.getDimensionPixelOffset(R.styleable.ZRCellBar_zr_cellbar_right_tips_margin,34);
        setLeftIconView(getLeftIconView(resLeftId,resleftwidth,resleftheight,resleftmargin));
        setBackgroundColor(Color.parseColor("#FFFFFF"));
        setTitleView(getTitleView(margintitle));
        setTitle(mTitle);
        setRightTipsView(getRightTipsView(margintips));
        setRightTips(tips);
        setRightIconView(getRightIconView(resrightwidth,resrightheight,resrightmargin));
        a.recycle();
    }


    private View getLeftIconView(int resId,int resleftwidth,int resleftheight,int resleftmargin) {
        ImageView iv = new ImageView(getContext());
        iv.setImageResource(resId);
        FrameLayout.LayoutParams lp = new LayoutParams(resleftwidth, resleftheight, Gravity.START | Gravity.CENTER_VERTICAL);
        lp.leftMargin=resleftmargin;
        iv.setLayoutParams(lp);
        return iv;
    }

    public void setLeftIconView(View v){
        if (v == null) return;
        mLeftView = v;
        addView(v);
    }

    private View getTitleView(int margin) {
        TextView tv = new TextView(getContext());
        tv.setTextColor(Color.parseColor("#232323"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.START|Gravity.CENTER_VERTICAL);
        lp.leftMargin=margin;
        tv.setLayoutParams(lp);
        return tv;
    }

    private void setTitleView(View v){
        if (v == null) return;
        mTitleView = v;
        addView(v);
    }


    public void setTitle(String mTitle) {
        if (mTitleView != null && mTitleView instanceof TextView) {
            ((TextView) mTitleView).setText(mTitle);
        }
    }

    private View getRightIconView(int resrightwidth,int resrightheight,int resrightmargin) {
        ImageView iv = new ImageView(getContext());
        iv.setImageResource(R.mipmap.right_tips_icon);
        FrameLayout.LayoutParams lp = new LayoutParams(resrightwidth, resrightheight, Gravity.END | Gravity.CENTER_VERTICAL);
        lp.rightMargin=resrightmargin;
        iv.setLayoutParams(lp);
        return iv;
    }
    private void setRightIconView(View v) {
        if (v == null) return;
        addView(v);
    }

    private View getRightTipsView(int margintips) {
        TextView tv = new TextView(getContext());
        tv.setTextColor(Color.parseColor("#A1A1A1"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.END|Gravity.CENTER_VERTICAL);
        lp.rightMargin=margintips;
        tv.setLayoutParams(lp);
        return tv;
    }
    private void setRightTipsView(View v) {
        if (v == null) return;
        mRightTipsView = v;
        addView(v);
    }
    public void setRightTips(String mTitle) {
        if (mRightTipsView != null && mRightTipsView instanceof TextView) {
            ((TextView) mRightTipsView).setText(mTitle);
        }
    }

    public String getTipsValue(){
        return  ((TextView) mRightTipsView).getText().toString();
    }


}
