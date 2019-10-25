package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-11 16:10
 *    desc   :
 */
interface MarketDetailView : AbsView {
    fun upTopBarInfo(info: String, color: Int)
    fun upBuyingSellingFilesData(buy: Float, sell: Float, buyData: MutableList<Int>, sellData: MutableList<Int>)
    fun upOrderBrokerData(buyData: MutableList<String>, sellData: MutableList<String>)
}