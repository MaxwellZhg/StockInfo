package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.net.ISimulationTradeNet
import com.zhuorui.securities.market.net.request.GetPositionRequest
import com.zhuorui.securities.market.net.request.OrderListRequest
import com.zhuorui.securities.market.net.response.GetPositionResponse
import com.zhuorui.securities.market.net.response.OrderListResponse
import com.zhuorui.securities.market.ui.view.SimulationTradingOrdersView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingOrdersViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 16:03
 *    desc   :
 */
class SimulationTradingOrdersPresenter :
    AbsNetPresenter<SimulationTradingOrdersView, SimulationTradingOrdersViewModel>() {
    private var page: Int = 0
    private var sDate: String = ""
    private var eDate: String = ""

    fun getOrders(sDate: String, eDate: String) {
        this.sDate = sDate
        this.eDate = eDate
        page = 0
        getOrders(sDate, eDate, 1)
    }

    fun getOrdersMore() {
        getOrders(sDate, eDate, page + 1)
    }

    private fun getOrders(sDate: String, eDate: String, page: Int) {
        val request = OrderListRequest("", sDate, eDate, "", page, 20, transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.orderList(request)
            ?.enqueue(Network.IHCallBack<OrderListResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onOrderListResponse(response: OrderListResponse) {
        page++
    }


    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
        if (response.request is OrderListRequest) {
        }
    }


}