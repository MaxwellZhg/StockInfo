package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhuorui.securities.market.R;

import java.util.List;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-15 10:41
 * desc   : 对比图
 */
public class ComparisonMapView extends LinearLayout {

    private View vTitleRoot;
    private TextView vTitle1;
    private TextView vTitle2;
    private TextView vTitle3;
    private String mTitle1;
    private String mTitle2;
    private String mTitle3;
    private int mValue1TextColor = Color.parseColor("#D9001B");
    private int mValue2TextColor = Color.parseColor("#00CC00");

    public ComparisonMapView(Context context) {
        this(context, null);
    }

    public ComparisonMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComparisonMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(VERTICAL);
    }

    private LayoutParams getItemLayoutParams() {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.topMargin = (int) (getResources().getDisplayMetrics().density * 10);
        return lp;
    }

    private View getTitleView() {
        View v = inflate(getContext(), R.layout.layout_comparison_map_view_title, null);
        v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        return v;
    }

    private View getItemView() {
        View v = inflate(getContext(), R.layout.layout_comparison_map_view_item, null);
        v.setLayoutParams(getItemLayoutParams());
        return v;
    }

    private int getOffs() {
        return vTitleRoot == null ? 0 : 1;
    }

    private IItemHolder getItemHolder(int index) {
        int offs = getOffs();
        IItemHolder holder;
        if (getChildCount() - offs > index) {
            holder = (IItemHolder) getChildAt(index + offs).getTag();
        } else {
            View v = getItemView();
            addView(v);
            holder = new ItemHolder(v);
            v.setTag(holder);
        }
        return holder;
    }

    public void setTitle(String title1, String title2, String title3) {
        mTitle1 = title1;
        mTitle2 = title2;
        mTitle3 = title3;
        if (vTitleRoot == null) {
            vTitleRoot = getTitleView();
            addView(vTitleRoot, 0);
            vTitle1 = vTitleRoot.findViewById(R.id.tv_title1);
            vTitle2 = vTitleRoot.findViewById(R.id.tv_title2);
            vTitle3 = vTitleRoot.findViewById(R.id.tv_title3);
        }
        vTitle1.setText(mTitle1);
        vTitle2.setText(mTitle2);
        vTitle3.setText(mTitle3);
    }

    public void setValueTextColor(int color1, int color2) {
        mValue1TextColor = color1;
        mValue2TextColor = color2;
        for (int i = 0, l = getChildCount() - getOffs(); i < l; i++) {
            IItemHolder holder = getItemHolder(i);
            holder.setValueTextColor(mValue1TextColor, mValue2TextColor);
        }
    }

    public void setData(List<IComparisonMapData> datas) {
        if (datas.size() < getChildCount() - (vTitleRoot == null ? 0 : 1)) {
            removeAllViews();
            if (vTitleRoot != null) {
                setTitle(mTitle1, mTitle2, mTitle3);
            }
        }
        for (int i = 0, l = datas.size(); i < l; i++) {
            IItemHolder holder = getItemHolder(i);
            holder.setValueTextColor(mValue1TextColor, mValue2TextColor);
            holder.setData(datas.get(i));
        }
    }

    public interface IItemHolder {

        void setValueTextColor(int color1, int color2);

        void setData(IComparisonMapData data);
    }

    public interface IComparisonMapData {

        CharSequence getTitle();

        float getValue1();

        int getValue1Color();

        float getValue2();

        int getValue2Color();

        float getMaxValue();
    }

    class ItemHolder implements IItemHolder {

        private int mTextColor1;
        private int mTextColor2;
        private float mValue1;
        private float mValue2;
        private float mMaxValue;
        private float mMaxValueWidth = 0f;
        private boolean isReComputation;
        private TextView vTitle;
        private View vValue1;
        private TextView vValue1Text;
        private View vValue2;
        private TextView vValue2Text;
        private TextView vValue3;

        public ItemHolder(View v) {
            vTitle = v.findViewById(R.id.tv_title);
            vValue1 = v.findViewById(R.id.v_left);
            vValue1Text = v.findViewById(R.id.tv_left_text);
            vValue2 = v.findViewById(R.id.v_right);
            vValue2Text = v.findViewById(R.id.tv_right_text);
            vValue3 = v.findViewById(R.id.tv_text3);
            v.post(new Runnable() {
                @Override
                public void run() {
                    mMaxValueWidth = v.getWidth() - vTitle.getWidth() - vValue1Text.getWidth() - vValue2Text.getWidth() - vValue3.getWidth();
                    mMaxValueWidth = mMaxValueWidth * 0.5f;
                    if (isReComputation) {
                        computationWidth();
                    }
                }
            });
        }

        @Override
        public void setValueTextColor(int color1, int color2) {
            mTextColor1 = color1;
            mTextColor2 = color2;
            vValue1Text.setTextColor(mTextColor1);
            vValue2Text.setTextColor(mTextColor2);
            setText3Color();
        }

        @Override
        public void setData(IComparisonMapData data) {
            vTitle.setText(data.getTitle());
            vValue1.setBackgroundColor(data.getValue1Color());
            vValue2.setBackgroundColor(data.getValue2Color());
            mValue1 = data.getValue1();
            mValue2 = data.getValue2();
            mMaxValue = data.getMaxValue();
            setText3Color();
            vValue1Text.setText(String.format("%.2f", mValue1));
            vValue2Text.setText(String.format("%.2f", mValue2));
            if (mMaxValueWidth > 0) {
                computationWidth();
            } else {
                isReComputation = true;
            }
            vValue3.setText(String.format("%+.2f", mValue1 - mValue2));
        }

        private void setText3Color() {
            vValue3.setTextColor(mValue1 >= mValue2 ? mTextColor1 : mTextColor2);
        }

        private void computationWidth() {
            isReComputation = false;
            int v1w = (int) (mValue1 / mMaxValue * mMaxValueWidth);
            ViewGroup.LayoutParams v1lp = vValue1.getLayoutParams();
            v1lp.width = v1w;
            vValue1.setLayoutParams(v1lp);
            int v2w = (int) (mValue2 / mMaxValue * mMaxValueWidth);
            ViewGroup.LayoutParams v2lp = vValue2.getLayoutParams();
            v2lp.width = v2w;
            vValue2.setLayoutParams(v2lp);
        }
    }

}
