package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.DashPathEffect;
import android.util.SparseArray;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;

/**
 * Created by Philpp Jahoda on 21/10/15.
 */
public interface ILineDataSet extends ILineRadarDataSet<Entry> {

    /**
     * Returns the drawing mode for this line dataset
     *
     * @return
     */
    LineDataSet.Mode getMode();

    /**
     * Returns the intensity of the cubic lines (the effect intensity).
     * Max = 1f = very cubic, Min = 0.05f = low cubic effect, Default: 0.2f
     *
     * @return
     */
    float getCubicIntensity();

    @Deprecated
    boolean isDrawCubicEnabled();

    @Deprecated
    boolean isDrawSteppedEnabled();

    /**
     * Returns the size of the drawn circles.
     */
    float getCircleRadius();

    /**
     * Returns the hole radius of the drawn circles.
     */
    float getCircleHoleRadius();

    /**
     * Returns the color at the given index of the DataSet's circle-color array.
     * Performs a IndexOutOfBounds check by modulus.
     *
     * @param index
     * @return
     */
    int getCircleColor(int index);

    /**
     * Returns the number of colors in this DataSet's circle-color array.
     *
     * @return
     */
    int getCircleColorCount();

    /**
     * Returns true if drawing circles for this DataSet is enabled, false if not
     *
     * @return
     */
    boolean isDrawCirclesEnabled();

    /**
     * Returns the color of the inner circle (the circle-hole).
     *
     * @return
     */
    int getCircleHoleColor();

    /**
     * Returns true if drawing the circle-holes is enabled, false if not.
     *
     * @return
     */
    boolean isDrawCircleHoleEnabled();

    /**
     * 是否画最后一点的值和边框
     */
    boolean isDrawCircleDashMarkerEnabled();

    /**
     * Returns the DashPathEffect that is used for drawing the lines.
     *
     * @return
     */
    DashPathEffect getDashPathEffect();

    /**
     * Returns true if the dashed-line effect is enabled, false if not.
     * If the DashPathEffect object is null, also return false here.
     *
     * @return
     */
    boolean isDashedLineEnabled();

    /**
     * Returns the IFillFormatter that is set for this DataSet.
     *
     * @return
     */
    IFillFormatter getFillFormatter();

    //获取
    SparseArray<String> getXLabels();

    /**
     * 在长按触摸图表时，是否需要绘制当前位置的圆点
     * @return
     */
    boolean isDrawHighlightCircle();
}