package com.dycm.applib1.ui.detail

import android.os.Bundle
import com.dycm.applib1.R
import com.dycm.applib1.model.StockTopic
import com.dycm.applib1.model.StockTopicDataTypeEnum
import com.dycm.applib1.socket.SocketClient
import com.dycm.applib1.socket.response.StocksTopicMinuteKlineResponse
import com.dycm.base2app.rxbus.EventThread
import com.dycm.base2app.rxbus.RxSubscribe
import com.dycm.base2app.ui.fragment.AbsEventFragment
import com.github.mikephil.charting.stockChart.data.TimeDataManage
import kotlinx.android.synthetic.main.fragment_one_day.*
import org.json.JSONException
import org.json.JSONObject

/**
 * 分时页
 */
class ChartOneDayFragment : AbsEventFragment() {

    private var land: Boolean = false // 是否横屏
    private val kTimeData = TimeDataManage()
    private var `object`: JSONObject? = null
    private var stockTopic: StockTopic? = null

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

    override fun init() {
        land = arguments!!.getBoolean("landscape")

        chart!!.initChart(land)
        //测试数据
        try {
            `object` = JSONObject(ChartData.TIMEDATA)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        //上证指数代码000001.IDX.SH
        kTimeData.parseTimeData(`object`, "000001.IDX.SH", 0.0)
        chart!!.setDataToChart(kTimeData)

        // 发起自选股K线订阅
        stockTopic = StockTopic(StockTopicDataTypeEnum.kminute, "SZ", "000001", 1)
        SocketClient.getInstance().bindTopic(stockTopic)
    }

    /**
     * 推送订阅自选股的分时数据
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicMinuteKlineResponse(response: StocksTopicMinuteKlineResponse) {
        // TODO 展示、缓存K线数据到本地
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketClient.getInstance().bindTopic(stockTopic)
    }
}