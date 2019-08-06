package com.dycm.applib1.ui.detail

import android.os.Bundle
import com.dycm.applib1.R
import com.dycm.base2app.ui.fragment.AbsFragment
import com.github.mikephil.charting.stockChart.data.TimeDataManage
import kotlinx.android.synthetic.main.fragment_five_day.*
import org.json.JSONException
import org.json.JSONObject

/**
 * 分时页
 */
class ChartFiveDayFragment : AbsFragment() {

    private var land: Boolean = false//是否横屏
    private val kTimeData = TimeDataManage()
    private var `object`: JSONObject? = null

    override val layout: Int
        get() = R.layout.fragment_five_day

    override fun init() {
        land = arguments!!.getBoolean("landscape")

        chart!!.initChart(land)

        //测试数据
        try {
            `object` = JSONObject(ChartData.FiveTIMEDATA)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        //上证指数代码000001.IDX.SH
        kTimeData.parseTimeData(`object`, "000001.IDX.SH", 0.0)
        chart!!.setDataToChart(kTimeData)
    }

    companion object {

        fun newInstance(land: Boolean): ChartFiveDayFragment {
            val fragment = ChartFiveDayFragment()
            val bundle = Bundle()
            bundle.putBoolean("landscape", land)
            fragment.arguments = bundle
            return fragment
        }
    }
}