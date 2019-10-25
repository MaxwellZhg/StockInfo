package com.zhuorui.securities.market.customer.view.kline.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import android.view.MotionEvent;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.DataTimeUtil;
import com.zhuorui.securities.market.customer.view.kline.dataManage.TimeDataManage;
import com.zhuorui.securities.market.customer.view.kline.markerView.BarBottomMarkerView;
import com.zhuorui.securities.market.customer.view.kline.markerView.LeftMarkerView;
import com.zhuorui.securities.market.customer.view.kline.markerView.TimeRightMarkerView;
import com.zhuorui.securities.market.customer.view.kline.renderer.TimeLineChartRenderer;
import com.zhuorui.securities.market.customer.view.kline.renderer.TimeXAxisRenderer;


/***
 * 绘制分时K线
 */
public class TimeLineChart extends LineChart {
    private LeftMarkerView myMarkerViewLeft;
    private TimeRightMarkerView myMarkerViewRight;
    private BarBottomMarkerView myMarkerBottom;
    private TimeDataManage kTimeData;
    private VolSelected volSelected;

    public void setVolSelected(VolSelected volSelected) {
        this.volSelected = volSelected;
    }

    public interface VolSelected {
        void onVolSelected(int value);

        void onValuesSelected(double price, double upDown, int vol, double avg);
    }

    public TimeLineChart(Context context) {
        super(context);
    }

    public TimeLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void initRenderer() {
        mRenderer = new TimeLineChartRenderer(this, mAnimator, mViewPortHandler);
    }

    @Override
    protected void initXAxisRenderer() {
        mXAxisRenderer = new TimeXAxisRenderer(mViewPortHandler, (TimeXAxis) mXAxis, mLeftAxisTransformer, this);
    }

    @Override
    public void initXAxis() {
        mXAxis = new TimeXAxis();
    }

    /*返回转型后的左右轴*/
    public void setMarker(LeftMarkerView markerLeft, TimeRightMarkerView markerRight, BarBottomMarkerView markerBottom, TimeDataManage kTimeData) {
        this.myMarkerViewLeft = markerLeft;
        this.myMarkerViewRight = markerRight;
        this.myMarkerBottom = markerBottom;
        this.kTimeData = kTimeData;
        ((TimeLineChartRenderer) mRenderer).setPreClose(kTimeData.getPreClose());
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        // if there is no marker view or drawing marker is disabled
        if (!isDrawMarkersEnabled() || !valuesToHighlight()) {
            return;
        }

        for (int i = 0; i < mIndicesToHighlight.length; i++) {

            Highlight highlight = mIndicesToHighlight[i];

            IDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());

            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
            int entryIndex = set.getEntryIndex(e);

            // make sure entry not null
            if (e == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX()) {
                continue;
            }

            float[] pos = getMarkerPosition(highlight);

            // check bounds
            if (!mViewPortHandler.isInBounds(pos[0], pos[1])) {
                continue;
            }

            Float yValForXIndex1 = null;
            Float yValForXIndex2 = null;
            float[] yValue = mIndicesToHighlight[i].getTouchYValue();
            if (yValue != null) {
                yValForXIndex1 = yValue[0];
                yValForXIndex2 = yValue[1];
            }

            if (volSelected != null) {
                volSelected.onVolSelected(kTimeData.getDatas().get((int) mIndicesToHighlight[i].getX()).getVolume());
                volSelected.onValuesSelected(kTimeData.getDatas().get((int) mIndicesToHighlight[i].getX()).getNowPrice(),
                        kTimeData.getDatas().get((int) mIndicesToHighlight[i].getX()).getPer(),
                        kTimeData.getDatas().get((int) mIndicesToHighlight[i].getX()).getVolume(),
                        kTimeData.getDatas().get((int) mIndicesToHighlight[i].getX()).getAveragePrice());
            }
            /*重新设值并计算大小*/
            if (yValForXIndex1 != null) {
                myMarkerViewLeft.setData(yValForXIndex1);
                myMarkerViewLeft.refreshContent(e, mIndicesToHighlight[i]);
                myMarkerViewLeft.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                myMarkerViewLeft.layout(0, 0, myMarkerViewLeft.getMeasuredWidth(), myMarkerViewLeft.getMeasuredHeight());
                if (getAxisLeft().getLabelPosition() == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                    myMarkerViewLeft.draw(canvas, mViewPortHandler.contentLeft() - myMarkerViewLeft.getWidth() / 2f, pos[1] + myMarkerViewLeft.getHeight() / 2f);
                } else {
                    float y = pos[1] + myMarkerViewLeft.getHeight() / 2f;
                    if (y > mViewPortHandler.contentBottom()) {
                        y = mViewPortHandler.contentBottom();
                    }
                    myMarkerViewLeft.draw(canvas, mViewPortHandler.contentLeft() + myMarkerViewLeft.getWidth() / 2f - 0.5f, y);
                }
            }
            if (yValForXIndex2 != null) {
                myMarkerViewRight.setData(yValForXIndex2);
                myMarkerViewRight.refreshContent(e, mIndicesToHighlight[i]);
                myMarkerViewRight.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                myMarkerViewRight.layout(0, 0, myMarkerViewRight.getMeasuredWidth(), myMarkerViewRight.getMeasuredHeight());
                if (getAxisRight().getLabelPosition() == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                    myMarkerViewRight.draw(canvas, mViewPortHandler.contentRight() + myMarkerViewRight.getWidth() / 2f, pos[1] + myMarkerViewRight.getHeight() / 2f);//- myMarkerViewRight.getWidth()
                } else {
                    float y = pos[1] + myMarkerViewRight.getHeight() / 2f;
                    if (y > mViewPortHandler.contentBottom()) {
                        y = mViewPortHandler.contentBottom();
                    }
                    myMarkerViewRight.draw(canvas, mViewPortHandler.contentRight() - myMarkerViewRight.getWidth() / 2f, y);//- myMarkerViewRight.getWidth()
                }
            }
            myMarkerBottom.setData(DataTimeUtil.secToDateTime(kTimeData.getDatas().get((int) e.getX()).getTimeMills()));
            myMarkerBottom.refreshContent(e, highlight);
            myMarkerBottom.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            myMarkerBottom.layout(0, 0, myMarkerBottom.getMeasuredWidth(), myMarkerBottom.getMeasuredHeight());
            int width = myMarkerBottom.getWidth() / 2;
            if (mViewPortHandler.contentRight() - pos[0] <= width) {
                myMarkerBottom.draw(canvas, mViewPortHandler.contentRight() - myMarkerBottom.getWidth() / 2f, mViewPortHandler.contentBottom() + myMarkerBottom.getHeight());//-markerBottom.getHeight()   CommonUtil.dip2px(getContext(),65.8f)
            } else if (pos[0] - mViewPortHandler.contentLeft() <= width) {
                myMarkerBottom.draw(canvas, mViewPortHandler.contentLeft() + myMarkerBottom.getWidth() / 2f, mViewPortHandler.contentBottom() + myMarkerBottom.getHeight());
            } else {
                myMarkerBottom.draw(canvas, pos[0], mViewPortHandler.contentBottom() + myMarkerBottom.getHeight());
            }
            // callbacks to update the content
//            mMarker.refreshContent(e, highlight);

            // draw the marker
//            mMarker.draw(canvas, pos[0], pos[1]);
        }
    }


    //    public void setHighlightValue(Highlight h) {
//        if (mData == null)
//            mIndicesToHighlight = null;
//        else {
//            mIndicesToHighlight = new Highlight[]{h};
//        }
//        invalidate();
//    }

    //调换画数据和右轴数据的位置，防止label数据被覆盖
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        if (mData == null)
//            return;
//        long starttime = System.currentTimeMillis();
//
//        mXAxisRenderer.calcXBounds(this, mXAxis.mAxisLabelModulus);
//        mRenderer.calcXBounds(this, mXAxis.mAxisLabelModulus);
//
//        // execute all drawing commands
//        drawGridBackground(canvas);
//
//        if (mAxisLeft.isEnabled())
//            mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum);
//        if (mAxisRight.isEnabled())
//            mAxisRendererRight.computeAxis(mAxisRight.mAxisMinimum, mAxisRight.mAxisMaximum);
//
//        mXAxisRenderer.renderAxisLine(canvas);
//        mAxisRendererLeft.renderAxisLine(canvas);
//        mAxisRendererRight.renderAxisLine(canvas);
//
//        // make sure the graph values and grid cannot be drawn outside the
//        // content-rect
//        int clipRestoreCount = canvas.save();
//        canvas.clipRect(mViewPortHandler.getContentRect());
//
//        mXAxisRenderer.renderGridLines(canvas);
//        mAxisRendererLeft.renderGridLines(canvas);
//        mAxisRendererRight.renderGridLines(canvas);
//
//        if (mXAxis.isDrawLimitLinesBehindDataEnabled())
//            mXAxisRenderer.renderLimitLines(canvas);
//
//        if (mAxisLeft.isDrawLimitLinesBehindDataEnabled())
//            mAxisRendererLeft.renderLimitLines(canvas);
//
//        if (mAxisRight.isDrawLimitLinesBehindDataEnabled())
//            mAxisRendererRight.renderLimitLines(canvas);
//
//        //mRenderer.drawData(canvas);
//
//        // if highlighting is enabled
//        if (valuesToHighlight())
//            mRenderer.drawHighlighted(canvas, mIndicesToHighlight);
//
//        // Removes clipping rectangle
//        canvas.restoreToCount(clipRestoreCount);
//
//        mRenderer.drawExtras(canvas);
//
//        clipRestoreCount = canvas.save();
//        canvas.clipRect(mViewPortHandler.getContentRect());
//
//        if (!mXAxis.isDrawLimitLinesBehindDataEnabled())
//            mXAxisRenderer.renderLimitLines(canvas);
//
//        if (!mAxisLeft.isDrawLimitLinesBehindDataEnabled())
//            mAxisRendererLeft.renderLimitLines(canvas);
//
//        if (!mAxisRight.isDrawLimitLinesBehindDataEnabled())
//            mAxisRendererRight.renderLimitLines(canvas);
//
//        canvas.restoreToCount(clipRestoreCount);
//
//        mXAxisRenderer.renderAxisLabels(canvas);
//        mAxisRendererLeft.renderAxisLabels(canvas);
//        mAxisRendererRight.renderAxisLabels(canvas);
//
//        mRenderer.drawData(canvas);
//
//        mRenderer.drawValues(canvas);
//
//        mLegendRenderer.renderLegend(canvas);
//
//        drawMarkers(canvas);
//
//        drawDescription(canvas);
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 当触摸K线显示指标线时，请求父控件不拦截上下滑动
        if (valuesToHighlight()) {
            disableScroll();
        }
        return super.dispatchTouchEvent(ev);
    }
}
