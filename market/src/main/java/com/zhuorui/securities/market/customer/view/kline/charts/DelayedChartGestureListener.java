package com.zhuorui.securities.market.customer.view.kline.charts;

import android.os.Handler;
import android.view.MotionEvent;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-11-27 17:31
 * desc   : Highight 延时关闭监听
 */
public class DelayedChartGestureListener implements OnChartGestureListener {

    protected Chart srcChart;

    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            srcChart.highlightValue(null, true);
        }
    };

    public DelayedChartGestureListener(Chart srcChart) {
        this.srcChart = srcChart;
        handler = new Handler();
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP) {
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 4000);
        }
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        handler.removeCallbacks(runnable);

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
}
