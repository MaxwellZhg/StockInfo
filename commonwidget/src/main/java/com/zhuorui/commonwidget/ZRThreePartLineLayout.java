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
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhuorui.securities.base2app.util.ResUtil;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/18
 * Desc:
 */
public class ZRThreePartLineLayout extends LinearLayout {
    private int allCount = 0;
    private int mWidth;
    private int type;

    public ZRThreePartLineLayout(Context context) {
        this(context, null);
    }

    public ZRThreePartLineLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZRThreePartLineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ZRThreePartLineLayout);
        mWidth = a.getDimensionPixelOffset(R.styleable.ZRThreePartLineLayout_zr_line_width, 0);
        setOrientation(HORIZONTAL);
        a.recycle();
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(HORIZONTAL);
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setValues(int up, int zero, int down) {
        this.allCount = up + zero + down;
        removeAllViews();
        if (type == 1) {
            addView(getUpCentView(up));
            addView(getZeroCentView(zero));
            addView(getDownCentView(down));
        } else {
            addView(getUpTextView(up));
            addView(getZeroTextView(zero));
            addView(getDownTextView(down));
        }

    }

    private View getUpCentView(int up) {
        ImageView iv = new ImageView(getContext());
        iv.setBackground(ResUtil.INSTANCE.getDrawable(R.mipmap.icon_up_precent));
        int wh = mWidth * up / allCount;
        int height = ResUtil.INSTANCE.getDimensionDp2Px(3f);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(wh, 20);
        iv.setLayoutParams(lp);
        return iv;
    }

    private View getZeroCentView(int zero) {
        ImageView iv = new ImageView(getContext());
        iv.setBackground(ResUtil.INSTANCE.getDrawable(R.mipmap.icon_zore_precent));
        int wh = mWidth * zero / allCount;
        int height = ResUtil.INSTANCE.getDimensionDp2Px(3f);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(wh, height);
        iv.setLayoutParams(lp);
        return iv;
    }

    private View getDownCentView(int down) {
        ImageView iv = new ImageView(getContext());
        iv.setBackground(ResUtil.INSTANCE.getDrawable(R.mipmap.icon_down_precent));
        int wh = mWidth * down / allCount;
        int height = ResUtil.INSTANCE.getDimensionDp2Px(3f);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(wh, height);
        iv.setLayoutParams(lp);
        return iv;
    }

    private View getUpTextView(int up) {
        int wh = mWidth * up / allCount;
        TextView tv = new TextView(getContext());
        tv.setTextColor(Color.parseColor("#FF0000"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        tv.setLines(1);
        tv.setCompoundDrawablesWithIntrinsicBounds(ResUtil.INSTANCE.getDrawable(R.mipmap.icon_up), null, null, null);
        tv.setCompoundDrawablePadding(ResUtil.INSTANCE.getDimensionDp2Px(5f));
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        tv.setText("" + up);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(wh, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.START);
        return tv;

    }

    private View getZeroTextView(int zero) {
        int wh = mWidth * zero / allCount;
        TextView tv = new TextView(getContext());
        tv.setTextColor(Color.parseColor("#C0CCE0"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        tv.setText("" + zero);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(wh, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        return tv;

    }

    private View getDownTextView(int down) {
        int wh = mWidth * down / allCount;
        TextView tv = new TextView(getContext());
        tv.setTextColor(Color.parseColor("#00CC00"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        tv.setCompoundDrawablesWithIntrinsicBounds(null, null, ResUtil.INSTANCE.getDrawable(R.mipmap.icon_down), null);
        tv.setCompoundDrawablePadding(ResUtil.INSTANCE.getDimensionDp2Px(5f));
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        tv.setText("" + down);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(wh, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.END);
        return tv;

    }
}
