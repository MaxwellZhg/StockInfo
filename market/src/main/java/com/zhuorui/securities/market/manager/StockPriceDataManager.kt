package com.zhuorui.securities.market.manager

import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
import com.zhuorui.securities.market.model.PushStockPriceData
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.socket.SocketApi
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicPriceResponse
import com.zhuorui.securities.market.socket.request.GetStockPriceRequestBody
import com.zhuorui.securities.market.socket.response.GetStockPriceResponse
import java.util.concurrent.ConcurrentHashMap

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/22 15:02
 *    desc   : 用于存放股票价格数据
 */
class StockPriceDataManager private constructor(val ts: String, val code: String, val type: Int) : BaseDataManager() {

    // 价格信息
    var priceData: PushStockPriceData? = null
    private var stockTopic: StockTopic? = null

    companion object {

        /**
         * 为每支股票创建一个缓存管理类对象，缓存在map中，ConcurrentHashMap为线程安全集合
         * key 为股票的tsCode
         * value 为数据缓存类
         */
        private val instanceMap = ConcurrentHashMap<String, StockPriceDataManager>()

        fun getInstance(ts: String, code: String, type: Int): StockPriceDataManager {
            var instance = instanceMap[getTsCode(ts, code)]
            if (instance == null) {
                synchronized(instanceMap) {
                    instance = instanceMap[getTsCode(ts, code)]
                    if (instance == null) {
                        instance = StockPriceDataManager(ts, code, type)
                        instanceMap[getTsCode(ts, code)] = instance!!
                        LogInfra.Log.d(instance!!.TAG, "当前缓存:$instanceMap")
                    }
                }
            }
            return instance!!
        }

        private fun getTsCode(ts: String, code: String): String {
            return "$code.$ts"
        }
    }

    init {
        queryPrice()
    }

    private fun queryPrice() {
        // 查询价格
        val requestBody = GetStockPriceRequestBody(GetStockPriceRequestBody.StockVo(ts, code, type))
        val requestId = SocketClient.getInstance().postRequest(requestBody, SocketApi.GET_STOCK_PRICE)
        requestIds.add(requestId)
    }

    /**
     * 返回查询股价信息
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onGetStockPriceResponse(response: GetStockPriceResponse) {
        if (requestIds.remove(response.respId)) {
            // 拿到查询到的股价信息
            priceData = response.data?.get(0)
            if (priceData != null) {
                notifyAllObservers()
            }
            // 当不为空时代表恢复订阅
            if (stockTopic == null) {
                // 订阅股价
                stockTopic = StockTopic(StockTopicDataTypeEnum.STOCK_PRICE, ts, code, type)
            }
            SocketClient.getInstance().bindTopic(stockTopic)
        }
    }

    /**
     * 订阅返回股价波动
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onStocksTopicPriceResponse(response: StocksTopicPriceResponse) {
        if (response.body != null && response.body.ts.equals(ts) && response.body.code.equals(code)) {
            // 更新股价信息
            priceData = response.body
            if (priceData != null) {
                LogInfra.Log.d(
                    TAG,
                    "onStocksTopicPriceResponse notifyAllObservers ... " + code + "." + ts + " " + response.body
                )
                notifyAllObservers()
            }
        }
    }

    /**
     * 重新连接tcp
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onSocketAuthCompleteEvent(event: SocketAuthCompleteEvent) {
        // 重新查询一次价格
        queryPrice()
    }

    override fun toString(): String {
        return super.toString().replace(this.javaClass.`package`!!.name + ".", "")
    }

    override fun destroy() {
        super.destroy()

        // 取消订阅
        if (stockTopic != null) {
            SocketClient.getInstance().unBindTopic(stockTopic)
        }

        instanceMap.remove(getTsCode(ts, code))
        LogInfra.Log.d(TAG, "当前缓存:${instanceMap}")
    }
}