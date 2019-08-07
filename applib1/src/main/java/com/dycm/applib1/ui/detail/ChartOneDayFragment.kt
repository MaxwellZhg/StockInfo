package com.dycm.applib1.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import com.dycm.applib1.R
import com.dycm.applib1.config.LocalStocksKlineDataConfig
import com.dycm.applib1.model.StockKlineTopic
import com.dycm.applib1.model.StockTopic
import com.dycm.applib1.model.StockTopicDataTypeEnum
import com.dycm.applib1.socket.SocketClient
import com.dycm.applib1.socket.response.StocksTopicMinuteKlineResponse
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
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * 分时页
 */
class ChartOneDayFragment : AbsEventFragment() {

    private var land: Boolean = false // 是否横屏
    private val kTimeData = TimeDataManage()
    private var stockTopic: StockTopic? = null
    private val disposables = LinkedList<Disposable>()

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

        chart!!.initChart(land)

//        // 测试数据
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
//                kTimeData.parseTimeData(list, "000001.IDX.SZ", 0.0)
//                chart!!.setDataToChart(kTimeData)
            }
        disposables.add(disposable)

        // 发起自选股K线订阅
        stockTopic = StockKlineTopic(StockTopicDataTypeEnum.kminute, "SZ", "000001", 1, 0)
        SocketClient.getInstance().bindTopic(stockTopic)
    }

    /**
     * 推送订阅自选股的分时数据
     */
    @SuppressLint("CheckResult")
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicMinuteKlineResponse(response: StocksTopicMinuteKlineResponse) {
        // TODO 展示、缓存K线数据到本地
        val klineData = response.body?.klineData
        val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
            emitter.onNext(LocalStocksKlineDataConfig.instance?.add(klineData?.ts, klineData?.code, klineData?.data)!!)
            emitter.onComplete()
        }).subscribeOn(Schedulers.io())
            .subscribe()
        disposables.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()

        // 取消订阅分时数据
        SocketClient.getInstance().bindTopic(stockTopic)

        // 释放disposable
        if (disposables.isNullOrEmpty()) return
        for (disposable in disposables) {
            disposable.dispose()
        }
        disposables.clear()
    }
}