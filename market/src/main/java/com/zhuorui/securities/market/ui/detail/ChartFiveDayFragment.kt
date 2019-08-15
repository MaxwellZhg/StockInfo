package com.zhuorui.securities.market.ui.detail

import android.os.Bundle
import com.zhuorui.securities.base2app.ui.fragment.AbsEventFragment
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.stockChart.data.TimeDataManage
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import kotlinx.android.synthetic.main.fragment_five_day.*
import org.json.JSONException
import org.json.JSONObject

/**
 * 分时页
 */
class ChartFiveDayFragment : AbsEventFragment() {

    private var land: Boolean = false//是否横屏
    private val kTimeData = TimeDataManage()

    companion object {

        fun newInstance(land: Boolean): ChartFiveDayFragment {
            val fragment = ChartFiveDayFragment()
            val bundle = Bundle()
            bundle.putBoolean("landscape", land)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_five_day

    override fun init() {
        land = arguments!!.getBoolean("landscape")
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        chart!!.initChart(land)

    }



}