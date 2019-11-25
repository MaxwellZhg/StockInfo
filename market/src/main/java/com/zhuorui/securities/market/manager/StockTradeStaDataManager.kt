package com.zhuorui.securities.market.manager

import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.socket.SocketApi
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicTradeStaResponse
import com.zhuorui.securities.market.socket.request.GetStockDataByTsCodeRequestBody
import com.zhuorui.securities.market.socket.response.GetStockTradeStaResponse
import com.zhuorui.securities.market.socket.vo.StockTradeStaData
import com.zhuorui.securities.market.util.MathUtil
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.ConcurrentHashMap

/**
 * 用于存放股票成交统计数据
 */
class StockTradeStaDataManager private constructor(val ts: String, val code: String, val type: Int) :
    BaseDataManager() {

    // 成交统计数据
    var tradeDatas: MutableList<StockTradeStaData>? = null
    // 最大缓存数量
    private val maxDataSize = 20
    // 当前数据集合最大的百分比
    var maxPercent = BigDecimal.ZERO!!

    private var stockTopic: StockTopic? = null

    companion object {
        /**
         * 为每支股票创建一个缓存管理类对象，缓存在map中，ConcurrentHashMap为线程安全集合
         * key 为股票的tsCode
         * value 为数据缓存类
         */
        private var instanceMap: ConcurrentHashMap<String, StockTradeStaDataManager>? = null

        fun getInstance(ts: String, code: String, type: Int): StockTradeStaDataManager {
            var instance: StockTradeStaDataManager? = null
            if (instanceMap == null) {
                synchronized(StockTradeStaDataManager::class.java) {
                    if (instanceMap == null) {
                        instanceMap = ConcurrentHashMap()
                        instance = StockTradeStaDataManager(ts, code, type)
                        instanceMap!![getTsCode(ts, code)] = instance!!
                        LogInfra.Log.d(instance!!.TAG, "当前缓存:$instanceMap")
                    }
                }
            } else {
                instance = instanceMap!![getTsCode(ts, code)]
                if (instance == null) {
                    instance = StockTradeStaDataManager(ts, code, type)
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
        // 加载成交统计
        val requestId =
            SocketClient.getInstance()
                .postRequest(GetStockDataByTsCodeRequestBody(ts, code), SocketApi.GET_STOCK_TRADESTA)
        requestIds.add(requestId)
    }

    /**
     * 查询选股逐笔成交统计数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetStockTradeStaResponse(response: GetStockTradeStaResponse) {
        if (requestIds.remove(response.respId)) {
            tradeDatas = response.data
            if (tradeDatas?.size!! > maxDataSize) {
                tradeDatas = tradeDatas?.subList(0, 20)
            }
            maxPercent = calculateMaxPercent().toBigDecimal()
            LogInfra.Log.d(TAG, "maxPercent = $maxPercent")
            notifyAllObservers()
            // 发起订阅
            stockTopic = StockTopic(StockTopicDataTypeEnum.STOCK_TRADE_STA, ts, code, type)
            SocketClient.getInstance().bindTopic(stockTopic)
        }
    }

    /**
     * 订阅自选股逐笔成交数据推送回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicTradeStaResponse(response: StocksTopicTradeStaResponse) {
        if (response.body != null && tradeDatas?.add(response.body!!)!!) {
            if (tradeDatas?.size!! > maxDataSize) {
                tradeDatas = tradeDatas?.subList(0, 20)
            }
            maxPercent = calculateMaxPercent().toBigDecimal()
            LogInfra.Log.d(TAG, "maxPercent = $maxPercent")
            notifyAllObservers()
        }
    }

    private fun calculateMaxPercent(): Double {
        var max = 0.00
        tradeDatas?.forEach {
            // 该价总成交百分比=该价当天成交量/该股票当天总成交量
            max = max(it.todayQty!!, it.todayTotalQty!!)
        }
        return max
    }

    private fun max(todayQty: BigDecimal, todayTotalQty: BigDecimal): Double {
        return MathUtil.multiply2(
            todayQty.divide(todayTotalQty, 4, RoundingMode.DOWN)!!,
            100.toBigDecimal()
        ).toDouble().coerceAtLeast(maxPercent.toDouble())
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

        instanceMap?.remove(getTsCode(ts, code))
        if (instanceMap?.isEmpty()!!) {
            instanceMap = null
        }

        LogInfra.Log.d(TAG, "当前缓存:${instanceMap}")
    }
}