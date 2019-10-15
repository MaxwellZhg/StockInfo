package com.zhuorui.securities.market.ui.kline.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.stockChart.data.KLineDataManage

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 9:50
 *    desc   :
 */
interface KlineView : AbsView {

    fun setDataToChart(kLineDataManage: KLineDataManage?)

    fun doBarChartSwitch(indexType: Int)
}