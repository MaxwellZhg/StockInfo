package com.zhuorui.securities.market.customer

import android.content.Context
import android.graphics.Rect
import androidx.viewpager.widget.ViewPager
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-11-18 17:39
 *    desc   : 两端对齐 CommonNavigatorAdapter
 */
class PaddingCommonNavigatorAdapter(titles: Array<String>) : CommonNavigatorAdapter() {

    /*如已显示在界面，再设置相关属性，需要调用view的更新方法*/

    private val mTitles: Array<String> = titles
    private var mViewPager: ViewPager? = null
    private var mListener: OnCommonNavigatorSelectListener? = null
    private var mTextSizeSp: Float = 14f
    private var mTotalWidthDp: Int = 0
    private var mPaddingPx: Int = 0
    private var mPaddingDp: Int = 0
    private var selectedColor = 0
    private var normalColor = 0

    init {
        selectedColor = ResUtil.getColor(R.color.tab_select)!!
        normalColor = ResUtil.getColor(R.color.color_FFFFFFFF)!!
    }

    fun setNormalColor(color: Int) {
        normalColor = color
    }

    fun setSelectedColor(color: Int) {
        selectedColor = color
    }

    fun setTotalWidthPx(widthPx: Float) {
        mTotalWidthDp = ResUtil.getDimensionPx2Dp(widthPx)
        calculation()
    }

    fun setTextSizeDp(textSizeDp: Float) {
        mTextSizeSp = textSizeDp
        calculation()
    }

    private fun calculation() {
        var textWidth = 0f
        for (element in mTitles) {
            textWidth += mTextSizeSp * element.length
        }
        mPaddingDp = ((mTotalWidthDp - textWidth) / (mTitles.size - 1) * 0.5f).toInt()
        mPaddingPx = ResUtil.getDimensionDp2Px(mPaddingDp.toFloat())
    }

    override fun getCount(): Int {
        return mTitles.size
    }

    override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
        val titleView = PaddinTitleView(context!!)
        titleView.normalColor = normalColor
        titleView.selectedColor = selectedColor
        titleView.textSize = mTextSizeSp
        titleView.text = mTitles[index]
        titleView.setOnClickListener {
            mViewPager?.currentItem = index
            mListener?.onSelected(index)
        }
        if (mTotalWidthDp > 0) {
            when (index) {
                0 -> titleView.setPadding(0, 0, mPaddingPx, 0)
                mTitles.size - 1 -> titleView.setPadding(mPaddingPx, 0, 0, 0)
                else -> titleView.setPadding(mPaddingPx, 0, mPaddingPx, 0)
            }
        }
        return titleView
    }

    override fun getTitleWeight(context: Context?, index: Int): Float {
//        commonNavigator.titleContainer.weightSum = totalWidth
        if (mTotalWidthDp == 0) {
            return super.getTitleWeight(context, index)
        }
        val textWidth = mTextSizeSp * mTitles[index].length
        return when (index) {
            0 -> textWidth + mPaddingDp
            mTitles.size - 1 -> textWidth + mPaddingDp
            else -> textWidth + mPaddingDp + mPaddingDp
        }
    }

    override fun getIndicator(context: Context?): IPagerIndicator {
        val indicator = LinePagerIndicator(context)
        indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
        indicator.setColors(selectedColor)
        indicator.lineHeight = ResUtil.getDimensionDp2Px(2f).toFloat()
        return indicator
    }

    fun bindViewPager(viewPager: ViewPager) {
        mViewPager = viewPager
    }

    fun bindListener(l: OnCommonNavigatorSelectListener) {
        mListener = l
    }

    /**
     * 当左右设置padding不相等时，计算Indicator的位置信息
     */
    inner class PaddinTitleView(context: Context) : ColorTransitionPagerTitleView(context) {

        override fun getContentLeft(): Int {
            val bound = Rect()
            paint.getTextBounds(text.toString(), 0, text.length, bound)
            val contentWidth = bound.width()
            return paddingLeft + left + (width - paddingLeft - paddingRight) / 2 - contentWidth / 2
        }

        override fun getContentRight(): Int {
            val bound = Rect()
            paint.getTextBounds(text.toString(), 0, text.length, bound)
            val contentWidth = bound.width()
            return contentLeft + contentWidth
        }
    }

    interface OnCommonNavigatorSelectListener {
        fun onSelected(index: Int)
    }

}