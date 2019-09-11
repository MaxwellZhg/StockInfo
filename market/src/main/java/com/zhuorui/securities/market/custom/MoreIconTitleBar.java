package com.zhuorui.securities.market.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.zhuorui.commonwidget.ZhuoRuiTopBar;
import com.zhuorui.securities.market.R;

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/9/11 10:26
 * desc   : 左右各两个点击图标的TitleBar
 */
public class MoreIconTitleBar extends ZhuoRuiTopBar {

    private View mLeftView;
    private View mRight2View;

    public MoreIconTitleBar(Context context) {
        this(context, null);
    }

    public MoreIconTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoreIconTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MoreIconTitleBar);

        // 左侧返回键旁的icon
        int resId = a.getResourceId(R.styleable.MoreIconTitleBar_left_icon, -1);
        if (resId != -1) {
            int width = a.getDimensionPixelOffset(R.styleable.MoreIconTitleBar_left_icon_width, ViewGroup.LayoutParams.WRAP_CONTENT);
            int hight = a.getDimensionPixelOffset(R.styleable.MoreIconTitleBar_left_icon_height, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = a.getDimensionPixelOffset(R.styleable.MoreIconTitleBar_left_icon_margin, 55);
            mLeftView = getView(resId, width, hight, Gravity.START, margin);
            addView(mLeftView);
        }

        // 右侧第二个icon
        resId = a.getResourceId(R.styleable.MoreIconTitleBar_right2_icon, -1);
        if (resId != -1) {
            int width = a.getDimensionPixelOffset(R.styleable.MoreIconTitleBar_right2_icon_width, ViewGroup.LayoutParams.WRAP_CONTENT);
            int hight = a.getDimensionPixelOffset(R.styleable.MoreIconTitleBar_right2_icon_height, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = a.getDimensionPixelOffset(R.styleable.MoreIconTitleBar_right2_icon_margin, 55);
            mRight2View = getView(resId, width, hight, Gravity.END, margin);
            addView(mRight2View);
        }

        a.recycle();
    }

    public void setLeftClickListener(OnClickListener l) {
        if (mLeftView != null) mLeftView.setOnClickListener(l);
    }

    public void setRight2ClickListener(OnClickListener l) {
        if (mRight2View != null) mRight2View.setOnClickListener(l);
    }

    public View getView(int resId, int width, int height, int gravity, int margin) {
        ImageView iv = new ImageView(getContext());
        iv.setImageResource(resId);
        LayoutParams lp = new LayoutParams(width, height, gravity | Gravity.CENTER_VERTICAL);
        if (Gravity.START == gravity) {
            lp.leftMargin = margin;
        } else {
            lp.rightMargin = margin;
        }
        iv.setLayoutParams(lp);
        return iv;
    }
}
