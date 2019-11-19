package com.zhuorui.securities.market.customer.view

import android.content.Context
import android.graphics.Rect
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * 当左右设置padding不相等时，计算Indicator的位置信息
 */
class PaddinTitleView(context: Context) : ColorTransitionPagerTitleView(context) {

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