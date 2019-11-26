package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.GetCapitalFlowTimeRequest
import com.zhuorui.securities.market.net.request.MarketStatisticsRequest
import com.zhuorui.securities.market.net.response.MarketStatisticsResponse
import com.zhuorui.securities.market.socket.SocketApi
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StockTopicIndexHandicapResponse
import com.zhuorui.securities.market.socket.push.StocksTopicHandicapResponse
import com.zhuorui.securities.market.socket.request.GetIndexPointInfoRequestBody
import com.zhuorui.securities.market.socket.response.GetIndexHandicapResponse
import com.zhuorui.securities.market.socket.vo.IndexPonitHandicapData
import com.zhuorui.securities.market.ui.adapter.MarketPartInfoAdapter
import com.zhuorui.securities.market.ui.view.HkStockDetailView
import com.zhuorui.securities.market.ui.viewmodel.HkStockDetailViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/17
 * Desc:
 */
class HkStockDetailPresenter :AbsNetPresenter<HkStockDetailView,HkStockDetailViewModel>(){
    var history = ArrayList<Int>()
    private var requestIds = java.util.ArrayList<String>()
    private var indexPointOneTopic: StockTopic? = null
    private var indexPointTwoTopic: StockTopic? = null
    private var indexPointThreeTopic: StockTopic? = null
    override fun init() {
        super.init()
    }
    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.infos?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addInfoToAdapter(t)
                })
         /*   viewModel?.mIndexHandicapData?.observe(it,
                androidx.lifecycle.Observer<List<IndexPonitHandicapData?>> { t ->
                    view?.setHsiIndexData(t)
                })*/
        }



    }

    fun getData(){
        history.clear()
        for (i in 0..4) {
            history.add(i)
        }
        viewModel?.infos?.value = history
    }

    fun getMarketInfoAdapter(state:Int): MarketPartInfoAdapter {
          return MarketPartInfoAdapter(1)
    }

    fun getMarketStatisticsInfo(ts:String){
        val requset = MarketStatisticsRequest(ts, transactions.createTransaction())
        Cache[IStockNet::class.java]?.getMarketStatistics(requset)
            ?.enqueue(Network.IHCallBack<MarketStatisticsResponse>(requset))
    }
}