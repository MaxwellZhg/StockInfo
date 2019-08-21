package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsEventPresenter
import com.zhuorui.securities.market.event.SocketDisconnectEvent
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.ui.view.StockTabFragmentView
import com.zhuorui.securities.market.ui.viewmodel.StockTabViewModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/19 15:44
 *    desc   :
 */
class StockTabFragmentPresenter : AbsEventPresenter<StockTabFragmentView, StockTabViewModel>() {

    override fun init() {
        super.init()
        viewModel?.mfragment?.let { view?.init(it) }

        // 启动长链接
        SocketClient.getInstance()?.connect()
    }

    @RxSubscribe(observeOnThread = EventThread.SINGLE)
    fun onSocketDisconnectEvent(event: SocketDisconnectEvent) {
        LogInfra.Log.d(TAG, "onSocketDisconnectEvent()")
        Thread.sleep(1000)
        SocketClient.getInstance()?.connect()
    }

    override fun destroy() {
        super.destroy()

        // 关闭长链接
        SocketClient.getInstance()?.destroy()
    }

    fun toggleStockTab() {
        viewModel?.toggleStockTab?.value = !viewModel?.toggleStockTab?.value!!
    }
}