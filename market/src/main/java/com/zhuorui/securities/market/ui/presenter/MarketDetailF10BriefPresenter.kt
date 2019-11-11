package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.model.F10ManagerModel
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.F10BrieRequest
import com.zhuorui.securities.market.net.response.F10BrieResponse
import com.zhuorui.securities.market.ui.view.MarketDetailF10BriefView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailF10BriefViewModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/6 13:55
 *    desc   :
 */
class MarketDetailF10BriefPresenter : AbsNetPresenter<MarketDetailF10BriefView, MarketDetailF10BriefViewModel>() {

    fun loadData(ts: String, code: String) {
        val requset = F10BrieRequest(ts, code, transactions.createTransaction())
        Cache[IStockNet::class.java]?.getF10BrieInfo(requset)
            ?.enqueue(Network.IHCallBack<F10BrieResponse>(requset))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onF10BrieResponse(response: F10BrieResponse) {
        val data = response.data
        viewModel?.managers?.value = data.manager
        view?.updateBrieInfo(data.company, data.manager, data.shareHolderChange, data.dividend, data.repo)
    }

    fun getManagers(): ArrayList<F10ManagerModel>? {
        return viewModel?.managers?.value
    }
}