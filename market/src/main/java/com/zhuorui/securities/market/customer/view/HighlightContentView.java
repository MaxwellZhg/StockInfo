package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhuorui.securities.market.R;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-11-27 18:00
 * desc   : 选中Highlight内容展示View
 */
public class HighlightContentView extends LinearLayout {

    public HighlightContentView(Context context) {
        this(context, null);
    }

    public HighlightContentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighlightContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    public void setData(LinkedHashMap<CharSequence,CharSequence> data) {
        init();
        int position = 0;
        for (Map.Entry<CharSequence, CharSequence> entry : data.entrySet()) {
            View lineView = getLineView(position);
            TextView vTitle = getTitleViewByLineView(lineView);
            TextView vConttentView = getConttentViewByLineView(lineView);
            vTitle.setText(entry.getKey());
            vConttentView.setText(entry.getValue());
            position++;
        }
    }

    private void init() {
        if (getChildCount() != 0) {
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).setVisibility(GONE);
            }
        }
    }

    private TextView getConttentViewByLineView(View lineView) {
        return lineView.findViewById(R.id.h_tv_content);
    }

    private TextView getTitleViewByLineView(View lineView) {
        return lineView.findViewById(R.id.h_tv_title);
    }

    private View getLineView(int position) {
        View v;
        if (position < getChildCount()) {
            v = getChildAt(position);
            v.setVisibility(VISIBLE);
        } else {
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_highlight_content, null);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = position == 0 ? 0 : (int) (getResources().getDisplayMetrics().density * 4);
            addView(v,lp);
        }
        return v;
    }
}
