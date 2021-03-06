package com.zhuorui.securities.market.manager

import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.socket.SocketApi
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicTradeResponse
import com.zhuorui.securities.market.socket.request.GetStockDataByTsCodeRequestBody
import com.zhuorui.securities.market.socket.response.GetStockTradeResponse
import com.zhuorui.securities.market.socket.vo.StockTradeDetailData
import java.util.concurrent.ConcurrentHashMap

/**
 * 用于存放股票成交明细数据
 */
class StockTradeDetailDataManager private constructor(val ts: String, val code: String, val type: Int) :
    BaseDataManager() {

    // 成交明细数据
    var tradeDatas: MutableList<StockTradeDetailData>? = null
    private var stockTopic: StockTopic? = null

    companion object {

        /**
         * 为每支股票创建一个缓存管理类对象，缓存在map中，ConcurrentHashMap为线程安全集合
         * key 为股票的tsCode
         * value 为数据缓存类
         */
        private var instanceMap: ConcurrentHashMap<String, StockTradeDetailDataManager>? = null

        fun getInstance(ts: String, code: String, type: Int): StockTradeDetailDataManager {
            var instance: StockTradeDetailDataManager? = null
            if (instanceMap == null) {
                synchronized(StockTradeDetailDataManager::class.java) {
                    if (instanceMap == null) {
                        instanceMap = ConcurrentHashMap()
                        instance = StockTradeDetailDataManager(ts, code, type)
                        instanceMap!![getTsCode(ts, code)] = instance!!
                        LogInfra.Log.d(instance!!.TAG, "当前缓存:$instanceMap")
                    }
                }
            } else {
                instance = instanceMap!![getTsCode(ts, code)]
                if (instance == null) {
                    instance = StockTradeDetailDataManager(ts, code, type)
                    instanceMap!![getTsCode(ts, code)] = instance!!
                    LogInfra.Log.d(instance!!.TAG, "当前缓存:$instanceMap")
                }
            }
            return instance!!
        }

        private fun getTsCode(ts: String, code: String): String {
            return "$code.$ts"
        }
    }


    init {
        queryTrade()
    }

    private fun queryTrade() {
        // 加载成交明细
        val requestId =
            SocketClient.getInstance().postRequest(GetStockDataByTsCodeRequestBody(ts, code), SocketApi.GET_STOCK_TRADE)
        requestIds.add(requestId)
    }

    /**
     * 查询选股逐笔成交明细数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetStockTradeResponse(response: GetStockTradeResponse) {
        if (requestIds.remove(response.respId)) {
            tradeDatas = response.data
            notifyAllObservers()
            // 发起订阅
            stockTopic = StockTopic(StockTopicDataTypeEnum.STOCK_TRADE, ts, code, type)
            SocketClient.getInstance().bindTopic(stockTopic)
        }
    }

    /**
     * 订阅自选股逐笔成交数据推送回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicTradeResponse(response: StocksTopicTradeResponse) {
        if (response.body != null && tradeDatas?.add(response.body!!)!!) {
            notifyAllObservers()
        }
    }

    @RxSubscribe(observeOnThread = EventThread.NEW)
    fun onSocketAuthCompleteEvent(event: SocketAuthCompleteEvent) {
        // 恢复订阅
        if (stockTopic != null) {
            SocketClient.getInstance().bindTopic(stockTopic)
        }
    }

    override fun destroy() {
        super.destroy()

        // 取消订阅
        if (stockTopic != null) {
            SocketClient.getInstance().unBindTopic(stockTopic)
        }

        instanceMap?.remove(getTsCode(ts, code))
        if (instanceMap?.isEmpty()!!) {
            instanceMap = null
        }

        LogInfra.Log.d(TAG, "当前缓存:$instanceMap")
    }
}