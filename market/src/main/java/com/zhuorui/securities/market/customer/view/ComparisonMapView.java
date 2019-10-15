package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.zhuorui.securities.market.R;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-15 10:41
 * desc   : 对比图
 */
public class ComparisonMapView extends LinearLayout {
    public ComparisonMapView(Context context) {
        this(context, null);
    }

    public ComparisonMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComparisonMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private View getTitleView() {
        View v = inflate(getContext(), R.layout.layout_comparison_map_view_title, null);
        return v;
    }

    private View getItemView(){
        View v = inflate(getContext(),R.layout.layout_comparison_map_view_item,null);
        return v;
    }

}
