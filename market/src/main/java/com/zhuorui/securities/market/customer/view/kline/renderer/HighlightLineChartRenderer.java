package com.zhuorui.securities.market.customer.view.kline.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
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

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-11-27 17:14
 * desc   : LineChart Highlight 绘制效果订制
 */
public class HighlightLineChartRenderer extends LineChartRenderer {

    public HighlightLineChartRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

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
}
