package com.zhuorui.securities.market.ui.kline

import android.os.Bundle
import me.jessyan.autosize.internal.CustomAdapt

/**
 * 横屏日、周、月、年K线
 */
class ChartKLineLandFragment : ChartKLineFragment(), CustomAdapt {

    companion object {

        fun newInstance(ts: String, code: String, tsCode: String, type: Int, klinType: Int): ChartKLineLandFragment {
            val fragment = ChartKLineLandFragment()
            val bundle = Bundle()
            bundle.putString("ts", ts)
            bundle.putString("code", code)
            bundle.putString("tsCode", tsCode)
            bundle.putInt("type", type)
            bundle.putInt("klinType", klinType)
            bundle.putBoolean("landscape", true)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun isBaseOnWidth(): Boolean {
        return true
    }

    override fun getSizeInDp(): Float {
        return 667f
    }
}