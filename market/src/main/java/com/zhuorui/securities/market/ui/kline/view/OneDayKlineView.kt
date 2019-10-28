package com.zhuorui.securities.market.ui.kline.view

import com.zhuorui.securities.market.customer.view.kline.dataManage.TimeDataManage
import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.socket.vo.kline.MinuteKlineData

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 9:50
 *    desc   :
 */
interface OneDayKlineView : AbsView {

    fun setDataToChart(timeDataManage: TimeDataManage?)

    /**
     * 动态添加一个点
     */
    fun dynamicsAddOne(data: MinuteKlineData)

    /**
     * 动态更新一个点
     */
    fun dynamicsUpdateOne(data: MinuteKlineData)
}