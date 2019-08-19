//package com.zhuorui.securities.market.ui.detail
//
//import android.os.Bundle
//import com.zhuorui.securities.base2app.infra.LogInfra
//import com.zhuorui.securities.base2app.rxbus.EventThread
//import com.zhuorui.securities.base2app.rxbus.RxSubscribe
//import com.zhuorui.securities.base2app.ui.fragment.AbsEventFragment
//import com.zhuorui.securities.market.R
//import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
//import com.zhuorui.securities.market.model.StockTopic
//import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
//import com.zhuorui.securities.market.socket.SocketClient
//import com.zhuorui.securities.market.socket.request.StockKlineGetDaily
//import com.zhuorui.securities.market.socket.response.StocksFiveDayKlineResponse
//import com.zhuorui.securities.market.stockChart.data.KLineDataManage
//import com.zhuorui.securities.market.stockChart.data.TimeDataManage
//import io.reactivex.Observable
//import io.reactivex.ObservableOnSubscribe
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.disposables.Disposable
//import kotlinx.android.synthetic.main.fragment_five_day.*
//import java.util.*
//
///**
// * 五日分时页
// */
//class ChartFiveDayFragment : AbsEventFragment() {
//
//    private var land: Boolean = false // 是否横屏
//    private var kTimeData: TimeDataManage? = null
//    private var stockTopic: StockTopic? = null
//    private val disposables = LinkedList<Disposable>()
//    private var requestIds = ArrayList<String>()
//
//    companion object {
//
//        fun newInstance(land: Boolean): ChartFiveDayFragment {
//            val fragment = ChartFiveDayFragment()
//            val bundle = Bundle()
//            bundle.putBoolean("landscape", land)
//            fragment.arguments = bundle
//            return fragment
//        }
//    }
//
//    override val layout: Int
//        get() = R.layout.fragment_five_day
//
//    override fun init() {
//        land = arguments!!.getBoolean("landscape")
//    }
//
//    override fun onLazyInitView(savedInstanceState: Bundle?) {
//        super.onLazyInitView(savedInstanceState)
//        chart!!.initChart(land)
//
//        loadKlineFiveDayData()
//    }
//
//    private fun loadKlineFiveDayData() {
//        // 发起自选股K线拉取补偿数据
//        val klineGetDaily = StockKlineGetDaily("SZ", "000001", 0, 0, 0)
//        val reqId = SocketClient.getInstance().requestGetFiveDayKline(klineGetDaily)
//        requestIds.add(reqId)
//    }
//
//    /**
//     * 拉取日K数据
//     */
//    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
//    fun onStocksFiveDayKlineResponse(response: StocksFiveDayKlineResponse) {
//        if (requestIds.remove(response.respId)) {
//            val kLineData = response.data
//            LogInfra.Log.d(TAG, "onStocksFiveDayKlineResponse ... klineData size = " + kLineData?.size)
//
//            // 展示K线数据
//            kTimeData = TimeDataManage()
//            kTimeData!!.parseKlineData(kLineData, "000001.IDX.SZ", 0.0, true)
//            var disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
//                chart!!.setDataToChart(kTimeData)
//                emitter.onNext(true)
//                emitter.onComplete()
//            }).subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe()
//            disposables.add(disposable)
//
//            // 更新本地数据
////            disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
////                val result = LocalStocksKlineDataConfig.instance?.replaceData("SZ", "000001", kTimeData)
////                if (result != null) {
////                    emitter.onNext(result)
////                }
////                emitter.onComplete()
////            }).subscribeOn(Schedulers.io())
////                .subscribe()
////            disposables.add(disposable)
//
//            // 订阅正常数据
//            stockTopic = StockTopic(StockTopicDataTypeEnum.k5day, "SZ", "000001", 1)
//            SocketClient.getInstance().bindTopic(stockTopic)
//        }
//    }
//
//    @RxSubscribe(observeOnThread = EventThread.NEW)
//    fun onSocketAuthCompleteEvent(event: SocketAuthCompleteEvent) {
//        // 恢复订阅
//        if (stockTopic != null) {
//            LogInfra.Log.d(TAG, "onSocketAuthCompleteEvent 先再拉一次补偿书记，再恢复订阅")
//            loadKlineFiveDayData()
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//
//        // 取消补偿数据订阅
//        if (stockTopic != null) {
//            SocketClient.getInstance().unBindTopic(stockTopic)
//            stockTopic = null
//        }
//        // 释放disposable
//        if (disposables.isNullOrEmpty()) return
//        for (disposable in disposables) {
//            disposable.dispose()
//        }
//        disposables.clear()
//        kTimeData = null
//    }
//}