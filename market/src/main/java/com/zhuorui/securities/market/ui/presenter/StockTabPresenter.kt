package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsEventPresenter
import com.zhuorui.securities.base2app.util.ThreadPoolUtil
import com.zhuorui.securities.market.event.NotifyStockCountEvent
import com.zhuorui.securities.market.event.SocketConnectEvent
import com.zhuorui.securities.market.model.StockTsEnum
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.ui.view.StockTabView
import com.zhuorui.securities.market.ui.viewmodel.StockTabViewModel
import com.zhuorui.securities.personal.event.JumpToSimulationTradingStocksEvent
import java.lang.Exception

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/19 15:44
 *    desc   :
 */
class StockTabPresenter : AbsEventPresenter<StockTabView, StockTabViewModel>() {

    override fun init() {
        super.init()
        viewModel?.mfragment?.let { view?.init(it) }

        // 启动长链接
        SocketClient.getInstance()?.connect()
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSocketDisconnectEvent(event: SocketConnectEvent) {
        LogInfra.Log.d(TAG, "onSocketDisconnectEvent()")

        view?.updateNetworkState(event.connected)
        if (!event.connected) {
            ThreadPoolUtil.getThreadPool().execute {
                try {
                    Thread.sleep(1000)
                    SocketClient.getInstance()?.connect()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun toggleStockTab() {
        viewModel?.toggleStockTab?.value = !viewModel?.toggleStockTab?.value!!
        view?.toggleStockTab(viewModel?.toggleStockTab?.value!!)
    }

    override fun destroy() {
        super.destroy()

        // 关闭长链接
        SocketClient.getInstance()?.destroy()
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onNotifyStockCountEvent(event: NotifyStockCountEvent) {
        when (event.ts) {
            null -> viewModel?.allNum?.value = event.count
            StockTsEnum.HK -> viewModel?.hkNum?.value = event.count
            else -> viewModel?.hsNum?.value = event.count
        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onJumpToSimulationTradingStocksEvent(event: JumpToSimulationTradingStocksEvent) {
        view?.onJumpToSimulationTradingStocksPage()
    }
}