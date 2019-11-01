package com.zhuorui.securities.market.ui.kline.presenter

import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsEventPresenter
import com.zhuorui.securities.market.customer.view.kline.dataManage.TimeDataManage
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicMinuteKlineResponse
import com.zhuorui.securities.market.socket.request.StockMinuteKline
import com.zhuorui.securities.market.socket.response.GetStocksMinuteKlineResponse
import com.zhuorui.securities.market.socket.vo.kline.StockMinuteVo
import com.zhuorui.securities.market.ui.kline.view.OneDayKlineView
import com.zhuorui.securities.market.ui.kline.viewmodel.OneDayKlineViewModel
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 9:52
 *    desc   :
 */
class ChartOneDayPresenter : AbsEventPresenter<OneDayKlineView, OneDayKlineViewModel>() {

    private var requestIds = ArrayList<String>()
    private val disposables = LinkedList<Disposable>()
    private var ts: String? = null
    private var code: String? = null
    private var tsCode: String? = null
    private var type: Int? = null
    private var stockTopic: StockTopic? = null

    fun init(ts: String?, code: String?, tsCode: String?, type: Int?) {
        this.ts = ts
        this.code = code
        this.tsCode = tsCode
        this.type = type

        loadKNetlineMinuteData()
    }

    /**
     * 加载网络数据
     */
    private fun loadKNetlineMinuteData() {
        if (!ts.isNullOrEmpty() && !code.isNullOrEmpty() && type != null) {
            // 发起自选股K线拉取补偿数据
            val stockMinuteKline = StockMinuteKline(ts!!, code!!, type!!)
            val reqId = SocketClient.getInstance().requestGetMinuteKline(stockMinuteKline)
            requestIds.add(reqId)
        }
    }


    /**
     * 拉取自选股补偿分时数据
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onGetStocksMinuteKlineResponse(response: GetStocksMinuteKlineResponse) {
        if (requestIds.remove(response.respId)) {
            val klineData = response.data

            LogInfra.Log.d(TAG, "onGetStocksMinuteKlineResponse ... klineData size = " + klineData?.size)

            // 展示K线数据
            val kDataManager = TimeDataManage()
            kDataManager.parseTimeData(klineData, tsCode, 0.0, true)
            val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                view?.setDataToChart(kDataManager)
                emitter.onNext(true)
                emitter.onComplete()
            }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe()
            disposables.add(disposable)

            if (!ts.isNullOrEmpty() && !code.isNullOrEmpty() && type != null) {
                // 订阅正常数据
                stockTopic = StockTopic(StockTopicDataTypeEnum.MINUTE, ts!!, code!!, type!!)
                SocketClient.getInstance().bindTopic(stockTopic)
            }
        }
    }

    /**
     * 推送自选股分时数据
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicMinuteKlineResponse(response: StocksTopicMinuteKlineResponse) {
        val klineData = (response.body?.klineData as StockMinuteVo).data
        if (!klineData.isNullOrEmpty()) {
            LogInfra.Log.d(TAG, "onStocksTopicMinuteKlineResponse ... " + klineData[0])
            view?.dynamicsAddOne(klineData[0])
        }
    }

    @RxSubscribe(observeOnThread = EventThread.NEW)
    fun onSocketAuthCompleteEvent(event: SocketAuthCompleteEvent) {
        // 恢复订阅
        if (stockTopic != null) {
            LogInfra.Log.d(TAG, "onSocketAuthCompleteEvent 先再拉一次补偿书记，再恢复订阅")
            loadKNetlineMinuteData()
        }
    }

    override fun destroy() {
        super.destroy()
        // 取消数据订阅
        if (stockTopic != null) {
            SocketClient.getInstance().unBindTopic(stockTopic)
        }
        // 释放disposable
        if (disposables.isNullOrEmpty()) return
        for (disposable in disposables) {
            disposable.dispose()
        }
        disposables.clear()
    }
}