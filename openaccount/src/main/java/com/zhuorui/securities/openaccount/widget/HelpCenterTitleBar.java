package com.zhuorui.securities.openaccount.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.zhuorui.commonwidget.ZhuoRuiTopBar;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/5
 * Desc: 帮助中心titlebar
 */
public class HelpCenterTitleBar extends ZhuoRuiTopBar {
    private View mCancleView;
    public HelpCenterTitleBar(Context context) {
        this(context, null);
    }

    public HelpCenterTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HelpCenterTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, com.zhuorui.commonwidget.R.styleable.HelpCenterTitleBar);
        int resId = a.getResourceId(com.zhuorui.commonwidget.R.styleable.HelpCenterTitleBar_help_center_cancle_icon, -1);
        if (resId != -1) {
            int width = a.getDimensionPixelOffset(com.zhuorui.commonwidget.R.styleable.HelpCenterTitleBar_help_center_cancle_width, ViewGroup.LayoutParams.WRAP_CONTENT);
            int hight = a.getDimensionPixelOffset(com.zhuorui.commonwidget.R.styleable.HelpCenterTitleBar_help_center_cancle_height, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = a.getDimensionPixelOffset(com.zhuorui.commonwidget.R.styleable.HelpCenterTitleBar_help_center_cancle_margin,55);
            setCancleView(getCancleView(resId, width, hight, margin));
        }
        a.recycle();
    }

    public void setCancleView(View v) {
        if (v == null) return;
        mCancleView = v;
        addView(v);
    }
    public void setCancleClickListener(OnClickListener l) {
        if (mCancleView != null) mCancleView.setOnClickListener(l);
    }


    public View getCancleView(int resId,int width,int height,int margin){
        ImageView iv = new ImageView(getContext());
        iv.setImageResource(resId);
        FrameLayout.LayoutParams lp = new LayoutParams(width, height, Gravity.LEFT|Gravity.CENTER_VERTICAL);
        lp.leftMargin = margin;
        iv.setLayoutParams(lp);
        return iv;
    }





}
