package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.manager.STInfoManager
import com.zhuorui.securities.market.net.ISimulationTradeNet
import com.zhuorui.securities.market.net.request.GetPositionRequest
import com.zhuorui.securities.market.net.request.OrderListRequest
import com.zhuorui.securities.market.net.response.GetPositionResponse
import com.zhuorui.securities.market.net.response.OrderListResponse
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.ui.view.SimulationTradingOrdersView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingOrdersViewModel
import com.zhuorui.securities.personal.config.LocalAccountConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 16:03
 *    desc   :
 */
class SimulationTradingOrdersPresenter :
    AbsNetPresenter<SimulationTradingOrdersView, SimulationTradingOrdersViewModel>() {
    private var mPage: Int = 1
    private var sDate: String = ""
    private var eDate: String = ""
    private var disposable: Disposable? = null

    fun getOrders(sDate: String, eDate: String) {
        this.sDate = sDate
        this.eDate = eDate
        view?.onRefreshData()
        getOrders(sDate, eDate, 1, false)
    }

    fun refresh() {
        getOrders(sDate, eDate, 1, true)
    }

    fun getOrdersMore() {
        getOrders(sDate, eDate, mPage, false)
    }

    private fun getOrders(sDate: String, eDate: String, page: Int, refresh: Boolean) {
        val accountInfo = LocalAccountConfig.read().getAccountInfo()
        val request = OrderListRequest(
            STInfoManager.getInstance().getSTFundAccountData().accountId,
            sDate,
            eDate,
            accountInfo.token!!,
            page,
            20,
            transactions.createTransaction()
        )
        if (disposable != null && !disposable!!.isDisposed) disposable!!.dispose()
        disposable = Cache[ISimulationTradeNet::class.java]?.orderList(request)
            ?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(io.reactivex.functions.Consumer {
                if (page > 1) {
                    view?.onFinishLoadMore()
                } else {
                    view?.onFinishRefresh()
                }
                if (it.isSuccess()) {
                    mPage += page
                    if (refresh) view?.onRefreshData()
                    view?.addData(it.data.total!!, it.data.list)
                } else {
                    view?.getDataError(it.msg!!)
                }
            }, io.reactivex.functions.Consumer {
                view?.getDataError(it.message!!)
                if (page > 1) {
                    view?.onFinishLoadMore()
                } else {
                    view?.onFinishRefresh()
                }
            })
    }

}