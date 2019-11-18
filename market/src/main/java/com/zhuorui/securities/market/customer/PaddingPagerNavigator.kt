package com.zhuorui.securities.market.customer

import android.content.Context
import android.graphics.Color
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

    val mTitles: Array<String> = titles
    var mViewPager: ViewPager? = null
    var mListener: OnCommonNavigatorSelectListener? = null
    var mTextSizeSp: Float = 14f
    var mTotalWidthDp: Int = 0
    var mPaddingPx: Int = 0
    var mPaddingDp: Int = 0

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
        for (i in 0 until mTitles.size) {
            textWidth += mTextSizeSp * mTitles[i].length
        }
        mPaddingDp = ((mTotalWidthDp - textWidth) / (mTitles.size - 1) * 0.5f).toInt()
        mPaddingPx = ResUtil.getDimensionDp2Px(mPaddingDp.toFloat())
    }

    override fun getCount(): Int {
        return mTitles.size
    }

    override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
        val titleView = PaddinTitleView(context!!)
        titleView.normalColor = Color.parseColor("#C3CDE3")
        titleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
        titleView.textSize = mTextSizeSp
        titleView.text = mTitles[index]
        titleView.setOnClickListener {
            mViewPager?.currentItem = index
            mListener?.onSelected(index)
        }
        when (index) {
            0 -> titleView.setPadding(0, 0, mPaddingPx, 0)
            mTitles.size - 1 -> titleView.setPadding(mPaddingPx, 0, 0, 0)
            else -> titleView.setPadding(mPaddingPx, 0, mPaddingPx, 0)
        }
        return titleView
    }

    override fun getTitleWeight(context: Context?, index: Int): Float {
//        commonNavigator.titleContainer.weightSum = totalWidth
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
        indicator.setColors(ResUtil.getColor(R.color.tab_select)!!)
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