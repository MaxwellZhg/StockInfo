package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
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

    /*fun getHSIPointInfo(code:String,ts:String){
        // 拉取指数数据
        val requestId =
            SocketClient.getInstance().postRequest(GetIndexPointInfoRequestBody(code, ts), SocketApi.GET_INDEX_HANDICAP)
        requestIds.add(requestId)
    }

    *//**
     * 推送指数盘口数据回调
     *//*
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onPushIndexPointHandicap(response: StockTopicIndexHandicapResponse) {
        response.body?.let { view?.detailPushData(it) }
    }

    *//**
     * 长链接连接状态发生改变
     *//*
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onSocketAuthCompleteEvent(event: SocketAuthCompleteEvent) {
        // 恢复订阅
        if (indexPointOneTopic != null) {
            SocketClient.getInstance().bindTopic(indexPointOneTopic)
        }
    }

    *//**
     * 获取指数盘口数据回调
     *//*
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetIndexPointHandicap(response: GetIndexHandicapResponse) {
         if(requestIds.remove(response.respId)) {
            *//* val listType = object : TypeToken<List<IndexPonitHandicapData>>() {}.type
              val datalist: List<IndexPonitHandicapData> = JsonUtil.fromJson(response.data.toString(), listType)*//*
             response.data?.let { view?.setHsiIndexData(it) }
             if(response.data?.get(0)?.code =="HSI") {
                 indexPointOneTopic = StockTopic(StockTopicDataTypeEnum.HANDICAP, "HK", "HSI", 1)
                 SocketClient.getInstance().bindTopic(indexPointOneTopic)
             }else if(response.data?.get(0)?.code =="HSCEI"){
                 indexPointTwoTopic = StockTopic(StockTopicDataTypeEnum.HANDICAP, "HK", "HSCEI", 1)
                 SocketClient.getInstance().bindTopic(indexPointTwoTopic)
             }else if(response.data?.get(0)?.code =="HSCCI"){
                 indexPointThreeTopic = StockTopic(StockTopicDataTypeEnum.HANDICAP, "HK", "HSCCI", 1)
                 SocketClient.getInstance().bindTopic(indexPointThreeTopic)
             }
         }
    }


    override fun destroy() {
        super.destroy()
        if (indexPointOneTopic != null)
            SocketClient.getInstance().unBindTopic(indexPointOneTopic)
        if (indexPointTwoTopic != null)
            SocketClient.getInstance().unBindTopic(indexPointOneTopic)
        if (indexPointThreeTopic != null)
            SocketClient.getInstance().unBindTopic(indexPointOneTopic)
    }*/
}