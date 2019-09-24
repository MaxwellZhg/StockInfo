package com.zhuorui.securities.market.ui.presenter

import android.text.TextUtils
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.config.LocalStocksConfig
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.StockSearchRequest
import com.zhuorui.securities.market.net.response.StockSearchResponse
import com.zhuorui.securities.market.ui.view.SimulationTradingRuleView
import com.zhuorui.securities.market.ui.view.SimulationTradingSearchView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingRuleViewModel
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingSearchViewModel
import me.jessyan.autosize.utils.LogUtils

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 16:03
 *    desc   :
 */
class SimulationTradingSearchPresenter :
    AbsNetPresenter<SimulationTradingSearchView, SimulationTradingSearchViewModel>() {

    fun clearSearchHistory() {

    }

    fun search(key: String) {
        if (TextUtils.isEmpty(key))return
        val requset = StockSearchRequest(key, 0, 10, transactions.createTransaction())
        Cache[IStockNet::class.java]?.search(requset)
            ?.enqueue(Network.IHCallBack<StockSearchResponse>(requset))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockSearchResponse(response: StockSearchResponse) {
        val datas = response.data?.datas
        if (datas.isNullOrEmpty()) return
        view?.setSearchData(datas)

    }

    /**
     * 获取自选股
     */
    fun getStocks(): MutableList<StockMarketInfo> {
        return LocalStocksConfig.read().getStocks()
    }

}