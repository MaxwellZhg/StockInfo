package com.zhuorui.securities.applib1.ui.detail

import android.os.Bundle
import com.zhuorui.securities.applib1.R
import com.zhuorui.securities.applib1.event.SocketAuthCompleteEvent
import com.zhuorui.securities.applib1.model.StockTopic
import com.zhuorui.securities.applib1.model.StockTopicDataTypeEnum
import com.zhuorui.securities.applib1.socket.SocketClient
import com.zhuorui.securities.applib1.socket.request.StockKlineGetDaily
import com.zhuorui.securities.applib1.socket.response.StocksDayKlineResponse
import com.zhuorui.securities.applib1.stockChart.data.KLineDataManage
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsEventFragment
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_kline.*
import java.util.*

/**
 * K线
 */
class ChartKLineFragment : AbsEventFragment() {

    private var mType: Int = 0//日K：1；周K：7；月K：30
    private var land: Boolean = false//是否横屏
    private var kLineData: KLineDataManage? = null
    //    private var `object`: JSONObject? = null
    private var indexType = 1

    private var stockTopic: StockTopic? = null
    private val disposables = LinkedList<Disposable>()
    private var requestIds = java.util.ArrayList<String>()

    companion object {

        fun newInstance(type: Int, land: Boolean): ChartKLineFragment {
            val fragment = ChartKLineFragment()
            val bundle = Bundle()
            bundle.putInt("type", type)
            bundle.putBoolean("landscape", land)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_kline

    override fun init() {
        mType = arguments!!.getInt("type")
        land = arguments!!.getBoolean("landscape")
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        kLineData = KLineDataManage(activity)
        combinedchart!!.initChart(land)
//        try {
//            if (mType == 1) {
//                `object` = JSONObject(ChartData.KLINEDATA)
//            } else if (mType == 7) {
//                `object` = JSONObject(ChartData.KLINEWEEKDATA)
//            } else if (mType == 30) {
//                `object` = JSONObject(ChartData.KLINEMONTHDATA)
//            }
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }

        combinedchart!!.getGestureListenerBar().setCoupleClick {
            if (land) {
                loadIndexData(if (indexType < 5) ++indexType else 1)
            }
        }
    }

    /**
     * 拉取补偿数据
     */
    private fun loadKlineData() {
        // TODO 测试代码
        if (mType == 1) {
            val klineGetDaily = StockKlineGetDaily("SZ", "000001", 0, 0, 0)
            val reqId = SocketClient.getInstance().requestGetDailyKline(klineGetDaily)
            requestIds.add(reqId)
        }
    }

    private fun loadIndexData(type: Int) {
        indexType = type
        when (indexType) {
            1//成交量
            -> combinedchart!!.doBarChartSwitch(indexType)
            2//请求MACD
            -> {
                kLineData!!.initMACD()
                combinedchart!!.doBarChartSwitch(indexType)
            }
            3//请求KDJ
            -> {
                kLineData!!.initKDJ()
                combinedchart!!.doBarChartSwitch(indexType)
            }
            4//请求BOLL
            -> {
                kLineData!!.initBOLL()
                combinedchart!!.doBarChartSwitch(indexType)
            }
            5//请求RSI
            -> {
                kLineData!!.initRSI()
                combinedchart!!.doBarChartSwitch(indexType)
            }
            else -> {
            }
        }
    }

    /**
     * 拉取日K数据
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onStocksTopicDayKlineResponse(response: StocksDayKlineResponse) {
        if (requestIds.remove(response.respId)) {
            val kTimeData = response.data
            LogInfra.Log.d(TAG, "onStocksTopicDayKlineResponse ... klineData size = " + kTimeData?.size)

            // 展示K线数据
            kLineData!!.parseKlineData(kTimeData, "000001.IDX.SZ", land)
            var disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                combinedchart!!.setDataToChart(kLineData)
                emitter.onNext(true)
                emitter.onComplete()
            }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe()
            disposables.add(disposable)

            // 更新本地数据
//            disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
//                val result = LocalStocksKlineDataConfig.instance?.replaceData("SZ", "000001", kTimeData)
//                if (result != null) {
//                    emitter.onNext(result)
//                }
//                emitter.onComplete()
//            }).subscribeOn(Schedulers.io())
//                .subscribe()
//            disposables.add(disposable)

            // 订阅正常数据
            stockTopic = StockTopic(StockTopicDataTypeEnum.kminute, "SZ", "000001", 1)
            SocketClient.getInstance().bindTopic(stockTopic)
        }
    }

    @RxSubscribe(observeOnThread = EventThread.NEW)
    fun onSocketAuthCompleteEvent(event: SocketAuthCompleteEvent) {
        // 恢复订阅
        if (stockTopic != null) {
            LogInfra.Log.d(TAG, "onSocketAuthCompleteEvent 先再拉一次补偿书记，再恢复订阅")
            loadKlineData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消补偿数据订阅
        if (stockTopic != null) {
            SocketClient.getInstance().unBindTopic(stockTopic)
            stockTopic = null
        }
        // 释放disposable
        if (disposables.isNullOrEmpty()) return
        for (disposable in disposables) {
            disposable.dispose()
        }
        disposables.clear()
    }
}