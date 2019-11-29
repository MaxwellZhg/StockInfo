package com.zhuorui.securities.market.manager

import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.socket.SocketApi
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicHandicapResponse
import com.zhuorui.securities.market.socket.request.GetStockDataByTsCodeRequestBody
import com.zhuorui.securities.market.socket.response.GetStockHandicapResponse
import com.zhuorui.securities.market.socket.vo.StockHandicapData
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/29
 * Desc:
 */
class StockIndexhandicapConsDataManager private constructor(val code: String,val ts: String, val type: Int):BaseDataManager(){
    //推送股价信息
    var stockConsIndexData: StockHandicapData?=null
    private var stockTopic: StockTopic? = null

    companion object {

        /**
         * 为每支股票创建一个缓存管理类对象，缓存在map中，ConcurrentHashMap为线程安全集合
         * key 为股票的tsCode
         * value 为数据缓存类
         */
        private val instanceMap = ConcurrentHashMap<String, StockIndexhandicapConsDataManager>()

        fun getInstance( code: String,ts: String, type: Int): StockIndexhandicapConsDataManager {
            var instance = instanceMap[getTsCode(code, ts)]
            if (instance == null) {
                synchronized(instanceMap){
                    instance = instanceMap[getTsCode(code, ts)]
                    if(instance==null) {
                        instance = StockIndexhandicapConsDataManager(code, ts, type)
                        instanceMap[getTsCode(code, ts)] = instance!!
                        LogInfra.Log.d(instance!!.TAG, "当前缓存:$instanceMap")
                    }
                }
            }
            return instance!!
        }

        private fun getTsCode(code: String, ts: String): String {
            return "$code.$ts"
        }
    }

    init {
        queryStockCons()
    }

    private fun queryStockCons() {
        // 查询成分股盘口
        val requestBody = GetStockDataByTsCodeRequestBody(code,ts)
        val requestId = SocketClient.getInstance().postRequest(requestBody, SocketApi.GET_STOCK_HANDICAP)
        requestIds.add(requestId)
    }

    /**
     * 返回成分股信息
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetIndexHandicapResponse(response: GetStockHandicapResponse) {
        if (requestIds.remove(response.respId)) {
            // 拿到查询到的成分股信息
            stockConsIndexData = response.data?.get(0)
            if (stockConsIndexData != null) {
                notifyAllObservers()
            }
            // 订阅成分股信息
            stockTopic = StockTopic(StockTopicDataTypeEnum.HANDICAP, ts, code, type)
            SocketClient.getInstance().bindTopic(stockTopic)
        }
    }


    /**
     * 订阅返回成分股推送
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicPriceResponse(response: StocksTopicHandicapResponse) {
        if (response.body != null) {
            // 更新股价信息
            stockConsIndexData = response.body?.get(0)
            if (stockConsIndexData != null) {
                LogInfra.Log.d(
                    TAG,
                    "StocksTopicHandicapResponse notifyAllObservers ... " + code + "." + ts + " " + response.body
                )
                notifyAllObservers()
            }
        }
    }

    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onSocketAuthCompleteEvent(event: SocketAuthCompleteEvent) {
        // 恢复订阅
        if (stockTopic != null) {
            SocketClient.getInstance().bindTopic(stockTopic)
        }
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

        instanceMap.remove(getTsCode(code, ts))
        LogInfra.Log.d(TAG, "当前缓存:${instanceMap}")
    }
}