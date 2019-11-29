package com.zhuorui.securities.market.customer.view.kline.charts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.highlight.Highlight;
import com.zhuorui.securities.market.customer.view.kline.renderer.HighlightLineChartRenderer;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-11-27 17:22
 * desc   : 订制Highlight LineChart效果
 */
public class HighlightLineChart extends LineChart {

    /**
     * Highlight 宽
     */
    public static float HIGHLIGHT_LINE_WIDTH = 0.5f;
    /**
     * Highlight circle 大小
     */
    public static float HIGHLIGHT_CIRCLE_RADIUS = 4f;

    public HighlightLineChart(Context context) {
        super(context);
    }

    public HighlightLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HighlightLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void initRenderer() {
        mRenderer = new HighlightLineChartRenderer(this, mAnimator, mViewPortHandler);
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
