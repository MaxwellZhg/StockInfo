package com.zhuorui.securities.market.customer.view.kline.renderer;

import android.graphics.*;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zhuorui.securities.base2app.infra.LogInfra;
import com.zhuorui.securities.base2app.rxbus.RxBus;
import com.zhuorui.securities.market.customer.view.kline.event.BaseEvent;
import com.zhuorui.securities.market.customer.view.kline.model.CirclePositionTime;

import java.util.List;

/**
 * 绘制K线渲染器
 * Created by ly on 2017/7/3.
 */

public class TimeLineChartRenderer extends LineChartRenderer {

    private final String TAG = this.getClass().getSimpleName();

    private double preClose;//昨收价

    public TimeLineChartRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    /**
     * Draws a normal line.
     *
     * @param c
     * @param dataSet
     */
    @Override
    protected void drawLinear(Canvas c, ILineDataSet dataSet) {

        int entryCount = dataSet.getEntryCount();

        final boolean isDrawSteppedEnabled = dataSet.isDrawSteppedEnabled();
        final int pointsPerEntryPair = isDrawSteppedEnabled ? 4 : 2;

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseY = mAnimator.getPhaseY();

        mRenderPaint.setStyle(Paint.Style.STROKE);

        Canvas canvas;

        // if the data-set is dashed, draw on bitmap-canvas
        if (dataSet.isDashedLineEnabled()) {
            canvas = mBitmapCanvas;
        } else {
            canvas = c;
        }

        mXBounds.set(mChart, dataSet);

        // 是否需要画价位虚线
        if (dataSet.isDrawCircleDashMarkerEnabled()) {
            drawDashMarker(canvas, dataSet, trans, phaseY);
        }

        // if drawing filled is enabled
        if (dataSet.isDrawFilledEnabled() && entryCount > 0) {
            drawLinearFill(c, dataSet, trans, mXBounds);
        }

        // more than 1 color
        if (dataSet.getColors().size() > 1) {

            if (mLineBuffer.length <= pointsPerEntryPair * 2) {
                mLineBuffer = new float[pointsPerEntryPair * 4];
            }

            for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {

                Entry e = dataSet.getEntryForIndex(j);
                if (e == null) {
                    continue;
                }

                mLineBuffer[0] = e.getX();
                mLineBuffer[1] = e.getY() * phaseY;

                if (j < mXBounds.max) {

                    e = dataSet.getEntryForIndex(j + 1);

                    if (e == null) {
                        break;
                    }

                    if (isDrawSteppedEnabled) {
                        mLineBuffer[2] = e.getX();
                        mLineBuffer[3] = mLineBuffer[1];
                        mLineBuffer[4] = mLineBuffer[2];
                        mLineBuffer[5] = mLineBuffer[3];
                        mLineBuffer[6] = e.getX();
                        mLineBuffer[7] = e.getY() * phaseY;
                    } else {
                        mLineBuffer[2] = e.getX();
                        mLineBuffer[3] = e.getY() * phaseY;
                    }

                } else {
                    mLineBuffer[2] = mLineBuffer[0];
                    mLineBuffer[3] = mLineBuffer[1];
                }

                trans.pointValuesToPixel(mLineBuffer);

                if (!mViewPortHandler.isInBoundsRight(mLineBuffer[0])) {
                    break;
                }

                // make sure the lines don't do shitty things outside
                // bounds
                if (!mViewPortHandler.isInBoundsLeft(mLineBuffer[2])
                        || (!mViewPortHandler.isInBoundsTop(mLineBuffer[1]) && !mViewPortHandler
                        .isInBoundsBottom(mLineBuffer[3]))) {
                    continue;
                }

                // get the color that is set for this line-segment
                mRenderPaint.setColor(dataSet.getColor(j));

                // 绘制K线
                canvas.drawLines(mLineBuffer, 0, pointsPerEntryPair * 2, mRenderPaint);
            }

        } else { // only one color per dataset

            if (mLineBuffer.length < Math.max((entryCount) * pointsPerEntryPair, pointsPerEntryPair) * 2) {
                mLineBuffer = new float[Math.max((entryCount) * pointsPerEntryPair, pointsPerEntryPair) * 4];
            }

            Entry e1, e2;

            e1 = dataSet.getEntryForIndex(mXBounds.min);

            if (e1 != null) {

                int j = 0;
                for (int x = mXBounds.min; x <= mXBounds.range + mXBounds.min; x++) {

                    e1 = dataSet.getEntryForIndex(x == 0 ? 0 : (x - 1));
                    e2 = dataSet.getEntryForIndex(x);

                    if (e1 == null || e2 == null) {
                        continue;
                    }

                    mLineBuffer[j++] = e1.getX();
                    mLineBuffer[j++] = e1.getY() * phaseY;

                    if (isDrawSteppedEnabled) {
                        mLineBuffer[j++] = e2.getX();
                        mLineBuffer[j++] = e1.getY() * phaseY;
                        mLineBuffer[j++] = e2.getX();
                        mLineBuffer[j++] = e1.getY() * phaseY;
                    }
                    //这些点与点之间不连接，用于五日分时
                    if (dataSet.getTimeDayType() == 5 && dataSet.getXLabels().indexOfKey(x == 0 ? 0 : (x - 1)) > 0) {
                        mLineBuffer[j++] = e1.getX();
                        mLineBuffer[j++] = e1.getY() * phaseY;
                    } else {
                        mLineBuffer[j++] = e2.getX();
                        mLineBuffer[j++] = e2.getY() * phaseY;
                    }
                }

                if (j > 0) {

                    trans.pointValuesToPixel(mLineBuffer);

                    mRenderPaint.setColor(dataSet.getColor());

                    final int size = Math.max((mXBounds.range + 1) * pointsPerEntryPair, pointsPerEntryPair) * 2;
                    // 绘制K线
                    canvas.drawLines(mLineBuffer, 0, size, mRenderPaint);
                }
            }
        }

        // 是否需要画价位虚线和圆点
        if (dataSet.isDrawCircleDashMarkerEnabled()) {
            drawCircleDashMarker(canvas, dataSet);
        }
    }

    private void drawDashMarker(Canvas canvas, ILineDataSet dataSet, Transformer trans, float phaseY) {
        // 不在当前价格范围内不会绘制虚线
        if (preClose <= dataSet.getYMin() || preClose >= dataSet.getYMax()) return;
        // 灰色当前价格画虚线参数设置
        Path path = new Path();
        float[] line = new float[]{0.0f, (float) (preClose * phaseY)};
        trans.pointValuesToPixel(line);
        float y = line[1];
        path.moveTo(mChart.getXChartMin(), y);
        path.lineTo(mViewPortHandler.contentRight(), y);
        // 设置虚线的间隔长度如“ 一 一 一 ”
        mRenderPaint.setPathEffect(new DashPathEffect(new float[]{25, 10, 25, 10}, 1));
        mRenderPaint.setColor(Color.parseColor("#FFC0CCE0"));
        mRenderPaint.setStrokeWidth(dataSet.getLineWidth() / 2f);
        canvas.drawPath(path, mRenderPaint);
        mRenderPaint.setPathEffect(null);
        mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
    }

    private void drawCircleDashMarker(Canvas canvas, ILineDataSet dataSet) {
        // 画虚线圆点和MarkerView
        int pointOffset = (mXBounds.range + mXBounds.min + 1) * 4;
        float y = mLineBuffer[pointOffset - 1];
        if (dataSet.getEntryCount() != 0) {
            // 黄色当前价格画虚线参数设置
            Path path = new Path();
            path.moveTo(mLineBuffer[0], y);
            path.lineTo(mViewPortHandler.contentRight(), y);
            // 设置虚线的间隔长度如“ - - - ”
            mRenderPaint.setPathEffect(new DashPathEffect(new float[]{10, 10, 10, 10}, 1));
            mRenderPaint.setColor(Color.parseColor("#FFFFB027"));
            mRenderPaint.setStrokeWidth(dataSet.getLineWidth() / 2f);
            canvas.drawPath(path, mRenderPaint);
            mRenderPaint.setPathEffect(null);
        }
        postPosition(dataSet, mLineBuffer[pointOffset - 2], y);
    }

//    public void drawCircleDashMarker(Canvas canvas, ILineDataSet dataSet, int count) {
//
//        //画虚线圆点和MarkerView
//        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
//        Path path = new Path();
//        // 应为这里会复用之前的mLineBuffer，在总长度不变的情况下，取一般就是之前的位置，mXBounds.range + mXBounds.min 是最新的K线的变动范围
//        int pointOffset = (mXBounds.range + mXBounds.min + 1) * 4;
//        if (dataSet.getEntryCount() != 0) {
//            //画虚线参数设置
//            path.moveTo(mLineBuffer[pointOffset - 2], mLineBuffer[pointOffset - 1]);
//            path.lineTo(mViewPortHandler.contentRight(), mLineBuffer[pointOffset - 1]);
//            mRenderPaint.setPathEffect(effects);
//
//            Entry e = dataSet.getEntryForIndex(count - 1);//Utils.convertDpToPixel(35)
//            mRenderPaint.setTextSize(Utils.convertDpToPixel(10));
//            String text = NumberUtils.keepPrecisionR(e.getY(), dataSet.getPrecision());
//            int width = Utils.calcTextWidth(mRenderPaint, text);
//            int height = Utils.calcTextHeight(mRenderPaint, text);
//            float rectLeft = mViewPortHandler.contentRight() - width - Utils.convertDpToPixel(4);
//            float circleX = mLineBuffer[pointOffset - 2];
//
//            if (circleX >= rectLeft) {
//                mRenderPaint.setColor(Color.parseColor("#A65198FA"));
//                mRenderPaint.setStyle(Paint.Style.FILL);
//                float x = mLineBuffer[pointOffset - 2];
//                float y = mLineBuffer[pointOffset - 1];
//                if (y > mViewPortHandler.contentTop() + mViewPortHandler.getChartHeight() / 2) {
//                    canvas.drawRect(rectLeft, y - Utils.convertDpToPixel(22), mViewPortHandler.contentRight(), y - Utils.convertDpToPixel(6), mRenderPaint);
//                    Path pathS = new Path();
//                    pathS.moveTo(x, y - Utils.convertDpToPixel(3));// 此点为多边形的起点
//                    pathS.lineTo(x - Utils.convertDpToPixel(3), y - Utils.convertDpToPixel(6));
//                    pathS.lineTo(x + Utils.convertDpToPixel(3), y - Utils.convertDpToPixel(6));
//                    pathS.close(); // 使这些点构成封闭的多边形
//                    canvas.drawPath(pathS, mRenderPaint);
//                    mRenderPaint.setColor(Color.parseColor("#66FFFFFF"));
//                    canvas.drawText(text, rectLeft + Utils.convertDpToPixel(2), y - Utils.convertDpToPixel(10), mRenderPaint);
//                } else {
//                    canvas.drawRect(rectLeft, y + Utils.convertDpToPixel(6), mViewPortHandler.contentRight(), y + Utils.convertDpToPixel(22), mRenderPaint);
//                    Path pathS = new Path();
//                    pathS.moveTo(x, y + Utils.convertDpToPixel(1));// 此点为多边形的起点
//                    pathS.lineTo(x - Utils.convertDpToPixel(3), y + Utils.convertDpToPixel(6));
//                    pathS.lineTo(x + Utils.convertDpToPixel(3), y + Utils.convertDpToPixel(6));
//                    pathS.close(); // 使这些点构成封闭的多边形
//                    canvas.drawPath(pathS, mRenderPaint);
//                    mRenderPaint.setColor(Color.parseColor("#66FFFFFF"));
//                    canvas.drawText(text, rectLeft + Utils.convertDpToPixel(2), y + Utils.convertDpToPixel(10) + height, mRenderPaint);
//                }
//            } else {
//                canvas.drawPath(path, mRenderPaint);
//                mRenderPaint.setStyle(Paint.Style.FILL);
//                canvas.drawRect(rectLeft, mLineBuffer[pointOffset - 1] - Utils.convertDpToPixel(8), mViewPortHandler.contentRight(), mLineBuffer[pointOffset - 1] + Utils.convertDpToPixel(8), mRenderPaint);
//                mRenderPaint.setColor(Color.parseColor("#FFFFFF"));
//                canvas.drawText(text, rectLeft + Utils.convertDpToPixel(2), mLineBuffer[pointOffset - 1] + Utils.convertDpToPixel(3), mRenderPaint);
//            }
//        }
//        mRenderPaint.setColor(Color.RED);
//        postPosition(dataSet, mLineBuffer[pointOffset - 2], mLineBuffer[pointOffset - 1]);
//    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        LineData lineData = mChart.getLineData();

        for (Highlight high : indices) {

            ILineDataSet set = lineData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled()) {
                continue;
            }

            Entry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set))
                continue;

            MPPointD pix;
            float[] touchYValue = high.getTouchYValue();
            if (touchYValue == null) {
                if (set instanceof LineDataSet) {
                    ((LineDataSet) set).setDrawHorizontalHighlightIndicator(false);
                }
                pix = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(), e.getY() * mAnimator
                        .getPhaseY());
            } else {
                if (set instanceof LineDataSet) {
                    ((LineDataSet) set).setDrawHorizontalHighlightIndicator(true);
                }
                pix = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(), touchYValue[0]);
            }

            high.setDraw((float) pix.x, (float) pix.y);

            // draw the lines
            drawHighlightLines(c, (float) pix.x, (float) pix.y, set);

            drawHighlightedCircles(c, high);
            LogInfra.Log.d(TAG, "drawHighlighted x = " + pix.x + ", y = " + pix.y);
        }

    }

    /**
     * 绘制高亮线上所有line线圆点
     *
     * @param c
     * @param highlight
     */
    private void drawHighlightedCircles(Canvas c, Highlight highlight) {
        mRenderPaint.setStyle(Paint.Style.FILL);
        mCirclesBuffer[0] = 0;
        mCirclesBuffer[1] = 0;
        List<ILineDataSet> dataSets = mChart.getLineData().getDataSets();
        Entry highlightEntry = null;
        for (int i = 0; i < dataSets.size(); i++) {
            ILineDataSet dataSet = dataSets.get(i);
            if (!dataSet.isVisible() || dataSet.isDrawCirclesEnabled() || dataSet.getEntryCount() == 0) {
                continue;
            }
            Entry e = dataSet.getEntryForXValue(highlight.getX(), highlight.getY());
            if (e == null) {
                continue;
            } else if (highlight.getDataSetIndex() == i) {
                //高亮线源数据上的点,显示在最上层，最后绘制
                highlightEntry = e;
                continue;
            }
            drawHighlightedCircle(c, dataSet, e);
        }
        if (highlightEntry != null) {
            drawHighlightedCircle(c, dataSets.get(highlight.getDataSetIndex()), highlightEntry);
        }

    }

    /**
     * 绘制高亮线上一条line线圆点
     *
     * @param c
     * @param dataSet
     * @param e
     */
    private void drawHighlightedCircle(Canvas c, ILineDataSet dataSet, Entry e) {
        float phaseY = mAnimator.getPhaseY();
        mCirclePaintInner.setColor(dataSet.getCircleHoleColor());
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
        float circleRadius = dataSet.getCircleRadius();
        float circleHoleRadius = dataSet.getCircleHoleRadius();
        boolean drawCircleHole = dataSet.isDrawCircleHoleEnabled() && circleHoleRadius < circleRadius && circleHoleRadius > 0.f;
        boolean drawTransparentCircleHole = drawCircleHole && dataSet.getCircleHoleColor() == ColorTemplate.COLOR_NONE;
        DataSetImageCache imageCache;
        if (mImageCaches.containsKey(dataSet)) {
            imageCache = mImageCaches.get(dataSet);
        } else {
            imageCache = new DataSetImageCache();
            mImageCaches.put(dataSet, imageCache);
        }
        boolean changeRequired = imageCache.init(dataSet);
        // only fill the cache with new bitmaps if a change is required
        if (changeRequired) {
            imageCache.fill(dataSet, drawCircleHole, drawTransparentCircleHole);
        }
        mXBounds.set(mChart, dataSet);
        mCirclesBuffer[0] = e.getX();
        mCirclesBuffer[1] = e.getY() * phaseY;
        trans.pointValuesToPixel(mCirclesBuffer);
        if (!mViewPortHandler.isInBoundsRight(mCirclesBuffer[0])) {
            return;
        }
        if (!mViewPortHandler.isInBoundsLeft(mCirclesBuffer[0]) ||
                !mViewPortHandler.isInBoundsY(mCirclesBuffer[1])) {
            return;
        }
        Bitmap circleBitmap = imageCache.getBitmap(0);//所有点一样，取第一个就好
        if (circleBitmap != null) {
            c.drawBitmap(circleBitmap, mCirclesBuffer[0] - circleRadius, mCirclesBuffer[1] - circleRadius, null);
        }

    }
    
    public void setPreClose(double preClose) {
        this.preClose = preClose;
    }

    private void postPosition(ILineDataSet dataSet, float x, float y) {
        CirclePositionTime position = new CirclePositionTime();
        position.cx = x;
        position.cy = y;
        BaseEvent event = new BaseEvent(dataSet.getTimeDayType());
        event.obj = position;
        RxBus.getDefault().post(event);
    }
}