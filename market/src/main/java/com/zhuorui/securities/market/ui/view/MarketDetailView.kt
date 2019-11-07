package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.event.MarketDetailInfoEvent
import com.zhuorui.securities.market.customer.view.StockDetailView

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-11 16:10
 *    desc   :
 */
interface MarketDetailView : AbsView {
    val Suspension: Int
        get() = 1
    val Delisting: Int
        get() = 2
    val Opening_zero: Int
        get() = 2

    fun upTopBarInfo(info: String, color: Int)
    fun upBuyingSellingFilesData(buy: Float, sell: Float, buyData: MutableList<Int>, sellData: MutableList<Int>)
    fun upOrderBrokerData(buyData: MutableList<String>, sellData: MutableList<String>)
    fun upFollow(collected: Boolean)
    fun upData(data: StockDetailView.IStockDatailData)

    fun changeInfoTypeData(event: MarketDetailInfoEvent)
}