package com.zhuorui.securities.market.ui.kline.view

import com.zhuorui.securities.market.customer.view.kline.dataManage.TimeDataManage
import com.zhuorui.securities.base2app.ui.fragment.AbsView

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 9:50
 *    desc   :
 */
interface OneDayKlineView : AbsView {

    fun setDataToChart(timeDataManage: TimeDataManage?)
}