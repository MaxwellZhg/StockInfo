package com.dycm.applib1.ui.detail

import android.os.Bundle
import com.dycm.applib1.R
import com.dycm.applib1.socket.SocketClient
import com.dycm.applib1.socket.request.StockKlineGetDaily
import com.dycm.applib1.socket.response.StocksDayKlineResponse
import com.dycm.applib1.stockChart.data.KLineDataManage
import com.dycm.base2app.infra.LogInfra
import com.dycm.base2app.rxbus.EventThread
import com.dycm.base2app.rxbus.RxSubscribe
import com.dycm.base2app.ui.fragment.AbsEventFragment
import kotlinx.android.synthetic.main.fragment_kline.*
import org.json.JSONException
import org.json.JSONObject

/**
 * K线
 */
class ChartKLineFragment : AbsEventFragment() {

    private var mType: Int = 0//日K：1；周K：7；月K：30
    private var land: Boolean = false//是否横屏
    private var kLineData: KLineDataManage? = null
    private var `object`: JSONObject? = null
    private var indexType = 1
    private var requestIds = ArrayList<String>()

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
        try {
            if (mType == 1) {
                `object` = JSONObject(ChartData.KLINEDATA)
            } else if (mType == 7) {
                `object` = JSONObject(ChartData.KLINEWEEKDATA)
            } else if (mType == 30) {
                `object` = JSONObject(ChartData.KLINEMONTHDATA)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        //上证指数代码000001.IDX.SH
        kLineData!!.parseKlineData(`object`, "000001.IDX.SH", land)
        combinedchart!!.setDataToChart(kLineData)

        combinedchart!!.getGestureListenerBar().setCoupleClick {
            if (land) {
                loadIndexData(if (indexType < 5) ++indexType else 1)
            }
        }

        // TODO 测试代码
        if (mType == 1) {
            val klineGetDaily = StockKlineGetDaily("SZ", "000001", 0, 0, 0)
            requestIds.add(klineGetDaily.uuid)
            SocketClient.getInstance().requestGetDailyKline(klineGetDaily)
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

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicDayKlineResponse(response: StocksDayKlineResponse) {
        if (requestIds.remove(response.respId)) {
            LogInfra.Log.d(TAG, "onStocksTopicDayKlineResponse ...")

        }
    }

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
}