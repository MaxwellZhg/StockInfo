package com.zhuorui.securities.market.manager

import com.zhuorui.commonwidget.model.Observer
import com.zhuorui.commonwidget.model.Subject
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.socket.SocketApi
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicTradeStaResponse
import com.zhuorui.securities.market.socket.request.GetStockTradeRequestBody
import com.zhuorui.securities.market.socket.response.GetStockTradeStaResponse
import com.zhuorui.securities.market.socket.vo.StockTradeStaData
import com.zhuorui.securities.market.util.MathUtil
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

/**
 * 用于存放股票成交统计数据
 */
class StockTradeStaDataManager private constructor(val ts: String, val code: String, val type: Int) :
    Subject<Observer> {

    private val TAG = "StockTradeStaDataManager"

    // 存储订阅者
    private val observerList = ArrayList<Observer>()
    // 成交统计数据
    var tradeDatas: MutableList<StockTradeStaData>? = null
    // 当前数据集合最大的百分比
    var maxPercent = BigDecimal.ZERO!!

    private var stockTopic: StockTopic? = null

    private var requestIds = ArrayList<String>()

    companion object {
        private var instance: StockTradeStaDataManager? = null

        fun getInstance(ts: String, code: String, type: Int): StockTradeStaDataManager {
            if (instance == null) {
                synchronized(StockTradeStaDataManager::class.java) {
                    if (instance == null) {
                        instance = StockTradeStaDataManager(ts, code, type)
                    }
                }
            }
            return instance!!
        }
    }

    init {
        RxBus.getDefault().register(this)

        // 加载成交统计
        val requestId =
            SocketClient.getInstance().postRequest(GetStockTradeRequestBody(ts, code), SocketApi.GET_STOCK_TRADESTA)
        requestIds.add(requestId)
    }

    override fun registerObserver(obs: Observer) {
        observerList.add(obs)
    }

    override fun removeObserver(obs: Observer) {
        observerList.remove(obs)
        // 当没有观察者了，清除当前实例
        if (observerList.isNullOrEmpty()) {
            destroy()
        }
    }

    /**
     * 查询选股逐笔成交统计数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetStockTradeStaResponse(response: GetStockTradeStaResponse) {
        if (requestIds.remove(response.respId)) {
            tradeDatas = response.data
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
            maxPercent = max(response.body!!.todayQty!!, response.body!!.todayTotalQty!!).toBigDecimal()
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

    override fun notifyAllObservers() {
        for (obs in observerList) {
            // 更新每一个观察者中的信息
            obs.update(this)
        }
    }

    private fun destroy() {
        // 取消订阅
        if (stockTopic != null) {
            SocketClient.getInstance().unBindTopic(stockTopic)
        }

        if (RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().unregister(this)
        }
        instance = null
    }
}