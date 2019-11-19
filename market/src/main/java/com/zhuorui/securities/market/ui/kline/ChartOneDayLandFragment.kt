package com.zhuorui.securities.market.ui.kline

import android.os.Bundle
import me.jessyan.autosize.internal.CustomAdapt

/**
 * 横屏分时K线
 */
class ChartOneDayLandFragment :
    ChartOneDayFragment(), CustomAdapt {

    companion object {

        /**
         * @param model 0模拟炒股 1个股详情 2个股详情横屏 3指数-简单样式
         */
        fun newInstance(ts: String, code: String, tsCode: String, type: Int, model: Int): ChartOneDayLandFragment {
            val fragment = ChartOneDayLandFragment()
            val bundle = Bundle()
            bundle.putString("ts", ts)
            bundle.putString("code", code)
            bundle.putString("tsCode", tsCode)
            bundle.putInt("type", type)
            bundle.putInt("model", model)
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