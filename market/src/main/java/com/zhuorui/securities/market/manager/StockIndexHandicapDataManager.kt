package com.zhuorui.securities.market.manager

import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
import com.zhuorui.securities.market.model.PushIndexHandicapData
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.socket.SocketApi
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StockTopicIndexHandicapResponse
import com.zhuorui.securities.market.socket.push.StocksTopicPriceResponse
import com.zhuorui.securities.market.socket.request.GetIndexPointInfoRequestBody
import com.zhuorui.securities.market.socket.response.GetIndexHandicapResponse
import com.zhuorui.securities.market.socket.vo.IndexPonitHandicapData
import java.util.concurrent.ConcurrentHashMap

/**`
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/26
 * Desc:指数推送管理类
 */
class StockIndexHandicapDataManager  private constructor(val code: String,val ts: String, val type: Int):BaseDataManager(){
    // 查询指数信息
    var indexData: IndexPonitHandicapData? = null
    //推送股价信息
   var pushIndexData: PushIndexHandicapData?=null
    private var stockTopic: StockTopic? = null

    companion object {

        /**
         * 为每支股票创建一个缓存管理类对象，缓存在map中，ConcurrentHashMap为线程安全集合
         * key 为股票的tsCode
         * value 为数据缓存类
         */
        private val instanceMap = ConcurrentHashMap<String, StockIndexHandicapDataManager>()

        fun getInstance( code: String,ts: String, type: Int): StockIndexHandicapDataManager {
            var instance = instanceMap[getTsCode(code, ts)]
            if (instance == null) {
                synchronized(instanceMap){
                    instance = instanceMap[getTsCode(code, ts)]
                    if(instance==null) {
                        instance = StockIndexHandicapDataManager(code, ts, type)
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
        queryPrice()
    }

    private fun queryPrice() {
        // 查询指数数据
        val requestBody = GetIndexPointInfoRequestBody(code,ts)
        val requestId = SocketClient.getInstance().postRequest(requestBody, SocketApi.GET_INDEX_HANDICAP)
        requestIds.add(requestId)
    }

    /**
     * 返回查询指数信息
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetIndexHandicapResponse(response: GetIndexHandicapResponse) {
        if (requestIds.remove(response.respId)) {
            // 拿到查询到的股价信息
            indexData = response.data?.get(0)
            if (indexData != null) {
                notifyAllObservers()
            }
            // 订阅股价
            stockTopic = StockTopic(StockTopicDataTypeEnum.HANDICAP, ts, code, type)
            SocketClient.getInstance().bindTopic(stockTopic)
        }
    }

    /**
     * 订阅返回指数推送
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicPriceResponse(response: StockTopicIndexHandicapResponse) {
        if (response.body != null && response.body?.ts.equals(ts) && response.body?.code.equals(code)) {
            // 更新股价信息
            pushIndexData = response.body
            if (pushIndexData != null) {
                LogInfra.Log.d(
                    TAG,
                    "onStocksTopicPriceResponse notifyAllObservers ... " + code + "." + ts + " " + response.body
                )
                notifyAllObservers()
            }
        }
    }

    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onSocketAuthCompleteEvent(event: SocketAuthCompleteEvent) {

        if (indexData == null) {
            queryPrice()
        }

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