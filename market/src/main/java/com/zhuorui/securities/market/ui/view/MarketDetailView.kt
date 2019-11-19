package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.event.MarketDetailInfoEvent
import com.zhuorui.securities.market.customer.view.StockDetailView
import com.zhuorui.securities.market.socket.vo.OrderData

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

    /**
     * 更新股票数据
     */
    fun upData(data: StockDetailView.IStockDatailData?)

    /**
     * 更新topBar状态
     */
    fun upTopBarInfo(info: String, color: Int)


    /**
     * 更新买卖十挡数据
     */
    fun upBuyingSellingFilesData(asklist: List<OrderData.AskBidModel>, bidlist: List<OrderData.AskBidModel>)

    /**
     * 更新买卖经纪数据
     */
    fun upOrderBrokerData(buyData: MutableList<String>, sellData: MutableList<String>)

    /**
     * 更新关注状态
     */
    fun upFollow(collected: Boolean)


    /**
     * 长连接状态更新
     */
    fun updateNetworkState(connected: Boolean)
}