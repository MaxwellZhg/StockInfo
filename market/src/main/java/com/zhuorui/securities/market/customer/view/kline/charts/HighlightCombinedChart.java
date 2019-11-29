package com.zhuorui.securities.market.customer.view.kline.charts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-11-29 17:42
 * desc   : 订制Highlight CombinedChart效果
 */
public class HighlightCombinedChart extends CombinedChart{

    public HighlightCombinedChart(Context context) {
        super(context);
    }

    public HighlightCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HighlightCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void highlightValue(Highlight high, boolean callListener) {
        super.highlightValue(high, callListener);
        //解决在同一X上下滑动，Y不流畅问题
        getOnTouchListener().setLastHighlighted(null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 当触摸K线显示指标线时，请求父控件不拦截上下滑动
        if (valuesToHighlight()) {
            disableScroll();
        }
        return super.dispatchTouchEvent(ev);
    }
}
