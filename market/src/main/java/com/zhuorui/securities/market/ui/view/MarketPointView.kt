package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.model.PushIndexHandicapData
import com.zhuorui.securities.market.net.response.StockConsInfoResponse
import com.zhuorui.securities.market.socket.push.StockTopicIndexHandicapResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/22
 * Desc:
 */
interface MarketPointView :AbsView{
    fun loadMoreSuccess()
    fun showAllCount(count:Int)
    fun refreshSuccess()
    fun showStateChangeEvent(state:Int)
    fun setLoadMoreState()
    fun loadConsStockFail()
    //设置指数推送数据
    fun getpushData(data: PushIndexHandicapData)
}