package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zhuorui.securities.base2app.util.ResUtil;
import com.zhuorui.securities.market.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/12
 * Desc:市场行情的饼图
 * */
public class MarketPieChatView extends FrameLayout {

    private int defColor;
    private ArrayList<Integer> colors = new ArrayList<>();
    private PieChart vPieChart;
    private String mTitle;

    public MarketPieChatView(Context context) {
        this(context,null);
    }

    public MarketPieChatView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MarketPieChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MarketPieChatView);//获得属性值
        mTitle = a.getString(R.styleable.MarketPieChatView_zr_title);
        defColor = Color.parseColor("#C3CDE3");
        inflate(context,R.layout.layout_market_pie_chat_view,this);
        initPieChart();
    }

    public void setColors(ArrayList<Integer> colors){
        this.colors.clear();
        this.colors.addAll(colors);
    }

    public void setNoDataText(String text){
        vPieChart.setNoDataText(text);
    }

    private void initPieChart() {
        vPieChart = findViewById(R.id.pie_cahart);
        vPieChart.setRenderer(new MyPieChartRenderer(vPieChart, vPieChart.getAnimator(), vPieChart.getViewPortHandler()));
        vPieChart.setNoDataText(getResources().getString(R.string.str_no_data));
        vPieChart.setNoDataTextColor(defColor);
        vPieChart.setUsePercentValues(true);//使用百分比显示
        vPieChart.getDescription().setEnabled(false);//是否启用描述
        vPieChart.getLegend().setEnabled(false);//是否启用图列
        vPieChart.setRotationAngle(270);//设置pieChart图表起始角度
        vPieChart.setMinOffset(0f);
        vPieChart.setExtraTopOffset(10f);
        vPieChart.setExtraBottomOffset(10f);
        vPieChart.setRotationEnabled(false);              //设置pieChart图表是否可以手动旋转
        vPieChart.setHighlightPerTapEnabled(false);       //设置piecahrt图表点击Item高亮是否可用
        // 设置 pieChart 图表Item文本属性
        vPieChart.setDrawEntryLabels(false);              //设置pieChart是否显示文字（true：下面属性才有效果）
        //设置 pieChart 内部圆环属性
        vPieChart.setDrawHoleEnabled(true);              //是否显示PieChart内部圆环(true:下面属性才有意义)
        vPieChart.setHoleRadius(60);
        vPieChart.setHoleColor(Color.parseColor("#211F2A"));             //设置PieChart内部圆的颜色
        vPieChart.setDrawCenterText(true);               //是否绘制PieChart内部中心文本（true：下面属性才有意义）
        vPieChart.setCenterText(mTitle);                 //设置PieChart内部圆文字的内容
        vPieChart.setCenterTextSize(14f);                //设置PieChart内部圆文字的大小
        vPieChart.setCenterTextColor(Color.WHITE);         //设置PieChart内部圆文字的颜色
        vPieChart.setTransparentCircleRadius(0f);       //设置PieChart内部透明圆的半径(这里设置31.0f)

    }

    public void setData(ArrayList<PieEntry> data){
        vPieChart.setData(getPieData(data));
        vPieChart.invalidate();
    }

    /**
     * 解决两端线条对齐问题(解决文法：在源码上基础上修改第二条线结束位置，固定最左使用270度的值，最右使用270度的值)
     */
    class MyPieChartRenderer extends PieChartRenderer {


        public MyPieChartRenderer(PieChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
            super(chart, animator, viewPortHandler);
        }

        private float getf2X(float transformedAngle, float labelRadius, float valueLineLength1, float valueLineLength2, float centerX) {
            final float sliceXBase = (float) Math.cos(transformedAngle * Utils.FDEG2RAD);
            final float pt1x = labelRadius * (1 + valueLineLength1) * sliceXBase + centerX;
            final float polyline2Width = labelRadius * valueLineLength2 * (float) Math.abs(Math.sin(transformedAngle * Utils.FDEG2RAD));
            return pt1x - polyline2Width;
        }

        @Override
        public void drawValues(Canvas c) {

            MPPointF center = mChart.getCenterCircleBox();

            // get whole the radius
            float radius = mChart.getRadius();
            float rotationAngle = mChart.getRotationAngle();
            float[] drawAngles = mChart.getDrawAngles();
            float[] absoluteAngles = mChart.getAbsoluteAngles();

            float phaseX = mAnimator.getPhaseX();
            float phaseY = mAnimator.getPhaseY();

            final float roundedRadius = (radius - (radius * mChart.getHoleRadius() / 100f)) / 2f;
            final float holeRadiusPercent = mChart.getHoleRadius() / 100.f;
            float labelRadiusOffset = radius / 10f * 3.6f;

            if (mChart.isDrawHoleEnabled()) {
                labelRadiusOffset = (radius - (radius * holeRadiusPercent)) / 2f;

                if (!mChart.isDrawSlicesUnderHoleEnabled() && mChart.isDrawRoundedSlicesEnabled()) {
                    // Add curved circle slice and spacing to rotation angle, so that it sits nicely inside
                    rotationAngle += roundedRadius * 360 / (Math.PI * 2 * radius);
                }
            }

            final float labelRadius = radius - labelRadiusOffset;

            PieData data = mChart.getData();
            List<IPieDataSet> dataSets = data.getDataSets();

            float yValueSum = data.getYValueSum();

            boolean drawEntryLabels = mChart.isDrawEntryLabelsEnabled();

            float angle;
            int xIndex = 0;

            c.save();

            float offset = Utils.convertDpToPixel(5.f);

            float leftX = 0;
            float rightX = 0;

            for (int i = 0; i < dataSets.size(); i++) {

                IPieDataSet dataSet = dataSets.get(i);
                if (leftX == 0 || rightX == 0) {
                    leftX = getf2X((rotationAngle + 270 * phaseY), labelRadius, dataSet.getValueLinePart1Length(), dataSet.getValueLinePart2Length(), center.x);
                    rightX = getf2X((rotationAngle + 90 * phaseY), labelRadius, dataSet.getValueLinePart1Length(), dataSet.getValueLinePart2Length(), center.x);
                }

                final boolean drawValues = dataSet.isDrawValuesEnabled();

                if (!drawValues && !drawEntryLabels) {
                    continue;
                }

                final PieDataSet.ValuePosition xValuePosition = dataSet.getXValuePosition();
                final PieDataSet.ValuePosition yValuePosition = dataSet.getYValuePosition();

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                float lineHeight = Utils.calcTextHeight(mValuePaint, "Q")
                        + Utils.convertDpToPixel(4f);

                ValueFormatter formatter = dataSet.getValueFormatter();

                int entryCount = dataSet.getEntryCount();

                mValueLinePaint.setColor(dataSet.getValueLineColor());
                mValueLinePaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getValueLineWidth()));

                final float sliceSpace = getSliceSpace(dataSet);

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                for (int j = 0; j < entryCount; j++) {

                    PieEntry entry = dataSet.getEntryForIndex(j);

                    if (xIndex == 0) {
                        angle = 0.f;
                    } else {
                        angle = absoluteAngles[xIndex - 1] * phaseX;
                    }

                    final float sliceAngle = drawAngles[xIndex];
                    final float sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius);

                    // offset needed to center the drawn text in the slice
                    final float angleOffset = (sliceAngle - sliceSpaceMiddleAngle / 2.f) / 2.f;

                    angle = angle + angleOffset;

                    final float transformedAngle = rotationAngle + angle * phaseY;

                    float value = mChart.isUsePercentValuesEnabled() ? entry.getY()
                            / yValueSum * 100f : entry.getY();
                    String formattedValue = formatter.getPieLabel(value, entry);
                    String entryLabel = entry.getLabel();

                    final float sliceXBase = (float) Math.cos(transformedAngle * Utils.FDEG2RAD);
                    final float sliceYBase = (float) Math.sin(transformedAngle * Utils.FDEG2RAD);

                    final boolean drawXOutside = drawEntryLabels &&
                            xValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                    final boolean drawYOutside = drawValues &&
                            yValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                    final boolean drawXInside = drawEntryLabels &&
                            xValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                    final boolean drawYInside = drawValues &&
                            yValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;

                    if (drawXOutside || drawYOutside) {

                        final float valueLineLength1 = dataSet.getValueLinePart1Length();
                        final float valueLineLength2 = dataSet.getValueLinePart2Length();
                        final float valueLinePart1OffsetPercentage = dataSet.getValueLinePart1OffsetPercentage() / 100.f;

                        float pt2x, pt2y;
                        float labelPtx, labelPty;

                        float line1Radius;

                        if (mChart.isDrawHoleEnabled()) {
                            line1Radius = (radius - (radius * holeRadiusPercent))
                                    * valueLinePart1OffsetPercentage
                                    + (radius * holeRadiusPercent);
                        } else {
                            line1Radius = radius * valueLinePart1OffsetPercentage;
                        }

//                        final float polyline2Width = dataSet.isValueLineVariableLength()
//                                ? labelRadius * valueLineLength2 * (float) Math.abs(Math.sin(
//                                transformedAngle * Utils.FDEG2RAD))
//                                : labelRadius * valueLineLength2;

                        final float pt0x = line1Radius * sliceXBase + center.x;
                        final float pt0y = line1Radius * sliceYBase + center.y;

                        final float pt1x = labelRadius * (1 + valueLineLength1) * sliceXBase + center.x;
                        final float pt1y = labelRadius * (1 + valueLineLength1) * sliceYBase + center.y;

                        if (transformedAngle % 360.0 >= 90.0 && transformedAngle % 360.0 <= 270.0) {
//                            pt2x = pt1x - polyline2Width;
                            pt2x = leftX;
                            pt2y = pt1y;

                            mValuePaint.setTextAlign(Paint.Align.RIGHT);

                            if (drawXOutside) {
                                getPaintEntryLabels().setTextAlign(Paint.Align.RIGHT);
                            }

                            labelPtx = pt2x - offset;
                            labelPty = pt2y;
                        } else {
//                            pt2x = pt1x + polyline2Width;
                            pt2x = rightX;
                            pt2y = pt1y;
                            mValuePaint.setTextAlign(Paint.Align.LEFT);

                            if (drawXOutside) {
                                getPaintEntryLabels().setTextAlign(Paint.Align.LEFT);
                            }

                            labelPtx = pt2x + offset;
                            labelPty = pt2y;

                        }

                        if (dataSet.getValueLineColor() != ColorTemplate.COLOR_NONE) {

                            if (dataSet.isUsingSliceColorAsValueLineColor()) {
                                mValueLinePaint.setColor(dataSet.getColor(j));
                            }

                            c.drawLine(pt0x, pt0y, pt1x, pt1y, mValueLinePaint);
                            c.drawLine(pt1x, pt1y, pt2x, pt2y, mValueLinePaint);
                        }

                        // draw everything, depending on settings
                        if (drawXOutside && drawYOutside) {

                            drawValue(c, formattedValue, labelPtx, labelPty, dataSet.getValueTextColor(j));

                            if (j < data.getEntryCount() && entryLabel != null) {
                                drawEntryLabel(c, entryLabel, labelPtx, labelPty + lineHeight);
                            }

                        } else if (drawXOutside) {
                            if (j < data.getEntryCount() && entryLabel != null) {
                                drawEntryLabel(c, entryLabel, labelPtx, labelPty + lineHeight / 2.f);
                            }
                        } else if (drawYOutside) {

                            drawValue(c, formattedValue, labelPtx, labelPty + lineHeight / 2.f, dataSet.getValueTextColor(j));
                        }
                    }

                    if (drawXInside || drawYInside) {
                        // calculate the text position
                        float x = labelRadius * sliceXBase + center.x;
                        float y = labelRadius * sliceYBase + center.y;

                        mValuePaint.setTextAlign(Paint.Align.CENTER);

                        // draw everything, depending on settings
                        if (drawXInside && drawYInside) {

                            drawValue(c, formattedValue, x, y, dataSet.getValueTextColor(j));

                            if (j < data.getEntryCount() && entryLabel != null) {
                                drawEntryLabel(c, entryLabel, x, y + lineHeight);
                            }

                        } else if (drawXInside) {
                            if (j < data.getEntryCount() && entryLabel != null) {
                                drawEntryLabel(c, entryLabel, x, y + lineHeight / 2f);
                            }
                        } else if (drawYInside) {
                            drawValue(c, formattedValue, x, y + lineHeight / 2f, dataSet.getValueTextColor(j));
                        }
                    }

                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                        Drawable icon = entry.getIcon();

                        float x = (labelRadius + iconsOffset.y) * sliceXBase + center.x;
                        float y = (labelRadius + iconsOffset.y) * sliceYBase + center.y;
                        y += iconsOffset.x;

                        Utils.drawImage(
                                c,
                                icon,
                                (int) x,
                                (int) y,
                                icon.getIntrinsicWidth(),
                                icon.getIntrinsicHeight());
                    }

                    xIndex++;
                }

                MPPointF.recycleInstance(iconsOffset);
            }
            MPPointF.recycleInstance(center);
            c.restore();
        }


    }

    private PieData getPieData(ArrayList<PieEntry> data) {
        PieDataSet pieDataSet;
        if (data.isEmpty()) {
            data.add(new PieEntry(100, "占位"));
            pieDataSet = new PieDataSet(data, "");
            pieDataSet.setColor(Color.parseColor("#B3BCD0"));
            pieDataSet.setDrawValues(false);
        } else {
            pieDataSet = new PieDataSet(data, "");
            if (colors.isEmpty()){
                pieDataSet.setColor(Color.parseColor("#B3BCD0"));
            }else {
                pieDataSet.setColors(colors);
            }
            pieDataSet.setDrawValues(true);
        }
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(14f);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setUsingSliceColorAsValueLineColor(true);
        PercentFormatter formatter = new PercentFormatter(vPieChart, false);
        formatter.mFormat = new DecimalFormat("###,###,##0.00");
        pieDataSet.setValueFormatter(formatter);
        pieDataSet.setValueLinePart1Length(0.4f);
        pieDataSet.setValueLinePart2Length(0.4f);
        return new PieData(pieDataSet);
    }
}
