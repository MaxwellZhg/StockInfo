package com.dycm.applib1.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import com.dycm.applib1.R
import com.dycm.applib1.config.LocalStocksKlineDataConfig
import com.dycm.applib1.socket.request.StockMinuteKline
import com.dycm.applib1.model.StockTopic
import com.dycm.applib1.model.StockTopicDataTypeEnum
import com.dycm.applib1.socket.SocketClient
import com.dycm.applib1.socket.push.StocksTopicMinuteKlineResponse
import com.dycm.applib1.socket.response.StocksMinuteKlineResponse
import com.dycm.applib1.socket.vo.kline.MinuteKlineData
import com.dycm.applib1.stockChart.data.TimeDataManage
import com.dycm.base2app.infra.LogInfra
import com.dycm.base2app.rxbus.EventThread
import com.dycm.base2app.rxbus.RxSubscribe
import com.dycm.base2app.ui.fragment.AbsEventFragment
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_one_day.*
import java.util.*

/**
 * 分时页
 */
class ChartOneDayFragment : AbsEventFragment() {

    private var land: Boolean = false // 是否横屏
    private val kTimeData = TimeDataManage()
    private var stockTopic: StockTopic? = null
    private val disposables = LinkedList<Disposable>()
    private var requestIds = ArrayList<String>()

    companion object {

        fun newInstance(land: Boolean): ChartOneDayFragment {
            val fragment = ChartOneDayFragment()
            val bundle = Bundle()
            bundle.putBoolean("landscape", land)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_one_day

    @SuppressLint("CheckResult")
    override fun init() {
        land = arguments!!.getBoolean("landscape")
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        chart!!.initChart(land)

        // 测试数据
//        try {
//            val `object` = JSONObject(ChartData.TIMEDATA)
//            // 上证指数代码000001.IDX.SH
//            kTimeData.parseTimeData(`object`, "000001.IDX.SH", 0.0)
//            chart!!.setDataToChart(kTimeData)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }

        val disposable = Observable.create(ObservableOnSubscribe<ArrayList<MinuteKlineData>> { emitter ->
            LocalStocksKlineDataConfig.instance?.getKlineData("SZ", "000001")?.let { emitter.onNext(it) }
            emitter.onComplete()
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                LogInfra.Log.d(TAG, "Local kline cache data size : " + list?.size)
                kTimeData.parseTimeData(list, "000001.IDX.SZ", 0.0)
                chart!!.setDataToChart(kTimeData)
            }
        disposables.add(disposable)

        // 发起自选股K线拉取补偿数据
        val stockMinuteKline = StockMinuteKline("SZ", "000001", 1)
        requestIds.add(stockMinuteKline.uuid)
        SocketClient.getInstance().requestGetMinuteKline(stockMinuteKline)
    }

    /**
     * 拉取自选股补偿分时数据
     */
    @SuppressLint("CheckResult")
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onStocksTopicMinuteKlineResponse(response: StocksMinuteKlineResponse) {
        if (requestIds.remove(response.respId)) {
            val klineData = response.data

            LogInfra.Log.d(TAG, "onStocksTopicMinuteKlineResponse ... klineData size = " + klineData?.size)

            // 展示K线数据
            if (kTimeData.datas.isNullOrEmpty()) {
                kTimeData.parseTimeData(klineData, "000001.IDX.SZ", 0.0)
                val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                    chart!!.setDataToChart(kTimeData)
                    emitter.onNext(true)
                    emitter.onComplete()
                }).subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe()
                disposables.add(disposable)
            }

            // 更新本地数据
            val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                emitter.onNext(LocalStocksKlineDataConfig.instance?.replaceData("SZ", "000001", klineData)!!)
                emitter.onComplete()
            }).subscribeOn(Schedulers.io())
                .subscribe()
            disposables.add(disposable)

            // 订阅正常数据
            stockTopic = StockTopic(StockTopicDataTypeEnum.kminute, "SZ", "000001", 1)
            SocketClient.getInstance().bindTopic(stockTopic)
        }
    }

    /**
     * 推送自选股分时数据
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onStocksTopicMinuteKlineResponse(response: StocksTopicMinuteKlineResponse) {

        // 在子线程中整合新数据再更新到界面
//            kTimeData.parseTimeData(data, "000001.IDX.SZ", 0.0)
//            val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
//                chart!!.setDataToChart(kTimeData)
//                emitter.onNext(true)
//                emitter.onComplete()
//            }).subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe()
//            disposables.add(disposable)


        // 缓存K线数据到本地
//        val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
//            emitter.onNext(LocalStocksKlineDataConfig.instance?.add(klineData?.ts, klineData?.code, data)!!)
//            emitter.onComplete()
//        }).subscribeOn(Schedulers.io())
//            .subscribe()
//        disposables.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消补偿数据订阅
        SocketClient.getInstance().unBindTopic(stockTopic)
        // 释放disposable
        if (disposables.isNullOrEmpty()) return
        for (disposable in disposables) {
            disposable.dispose()
        }
        disposables.clear()
    }
}