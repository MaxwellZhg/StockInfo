package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.event.StockConsEvent
import com.zhuorui.securities.market.event.StockConsStateEvent
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.StockConsInfoRequest
import com.zhuorui.securities.market.net.response.StockConsInfoResponse
import com.zhuorui.securities.market.ui.adapter.MarketPointConsInfoAdapter
import com.zhuorui.securities.market.ui.adapter.MarketPointInfoAdapter
import com.zhuorui.securities.market.ui.view.MarketStockConsView
import com.zhuorui.securities.market.ui.viewmodel.MarketStockConsViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:
 */
class MarketStockConsPresenter :AbsNetPresenter<MarketStockConsView,MarketStockConsViewModel>(){
    override fun init() {
        super.init()
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.infos?.observe(it,
                androidx.lifecycle.Observer<List<StockConsInfoResponse.ListInfo>> { t ->
                    view?.addInfoToAdapter(t)
                })
        }
    }

    fun getMarketInfoAdapter(): MarketPointConsInfoAdapter {
        return MarketPointConsInfoAdapter()
    }



    /**
     * 获取topbar 显示股票状态信息
     * */
    fun getTopBarStockStatusInfo() {
        val h = Integer.valueOf(TimeZoneUtil.currentTime("HH"))
        var closingTimeMillis =
            if (h < 9 || h >= 16) TimeZoneUtil.currentTimeMillis() else 0
        /* view?.upTopBarInfo(
             MarketUtil.getStockStatusTxt(stocksInfo?.ts, closingTimeMillis, true),
             topbarTxtColor
         )*/
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockConsEventResponse(event: StockConsEvent) {
        if(event.list.isNotEmpty()) {
            viewModel?.infos?.value= event.list as MutableList<StockConsInfoResponse.ListInfo>
        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockConsStateEventResponse(event: StockConsStateEvent) {
            view?.showStateInfo(event.state)
    }

}