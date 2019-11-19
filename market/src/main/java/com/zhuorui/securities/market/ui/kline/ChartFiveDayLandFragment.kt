package com.zhuorui.securities.market.ui.kline

import android.os.Bundle
import me.jessyan.autosize.internal.CustomAdapt

/**
 * 横屏五日K线
 */
class ChartFiveDayLandFragment : ChartFiveDayFragment(), CustomAdapt {

    companion object {

        fun newInstance(ts: String, code: String, tsCode: String, type: Int): ChartFiveDayLandFragment {
            val fragment = ChartFiveDayLandFragment()
            val bundle = Bundle()
            bundle.putString("ts", ts)
            bundle.putString("code", code)
            bundle.putString("tsCode", tsCode)
            bundle.putInt("type", type)
            bundle.putBoolean("landscape", true)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return 375f
    }

}