package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.StockConsInfoRequest
import com.zhuorui.securities.market.net.request.StockSearchRequest
import com.zhuorui.securities.market.net.response.StockConsInfoResponse
import com.zhuorui.securities.market.net.response.StockSearchResponse
import com.zhuorui.securities.market.ui.adapter.MarketPartInfoAdapter
import com.zhuorui.securities.market.ui.adapter.MarketPointConsInfoAdapter
import com.zhuorui.securities.market.ui.adapter.MarketPointInfoAdapter
import com.zhuorui.securities.market.ui.view.MarketPointView
import com.zhuorui.securities.market.ui.viewmodel.MarketPointViewModel
import com.zhuorui.securities.market.util.MarketUtil

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/22
 * Desc:
 */
class MarketPointPresenter :AbsNetPresenter<MarketPointView,MarketPointViewModel>(){
    var history = ArrayList<Int>()
    var info =ArrayList<Int>()
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
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.pointInfos?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addPointInfoAdapter(t)
                })
        }
    }
/*    fun getData(){
        history.clear()
        for (i in 0..19) {
            history.add(i)
        }
        viewModel?.infos?.value = history
    }*/

    fun getInfoData(){
        info.clear()
        for (i in 0..19) {
            info.add(i)
        }
        viewModel?.pointInfos?.value = info
    }
    fun getMarketInfoAdapter(): MarketPointConsInfoAdapter {
        return MarketPointConsInfoAdapter()
    }


    fun getMarketPointInfoAdapter():MarketPointInfoAdapter{
        return  MarketPointInfoAdapter()
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

    fun getStockConsInfo(){
        val requset = StockConsInfoRequest("HSI", 5,1,1,"HK" ,transactions.createTransaction())
        Cache[IStockNet::class.java]?.getStockConsInfo(requset)
            ?.enqueue(Network.IHCallBack<StockConsInfoResponse>(requset))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockConsInfoResponse(response: StockConsInfoResponse) {
        if (!transactions.isMyTransaction(response)) return
        val datas = response.data
        if(datas.list.isNotEmpty()){
            viewModel?.infos?.value=datas.list
        }
    }

}