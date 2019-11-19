package com.zhuorui.securities.market.ui.kline

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.market.customer.view.kline.dataManage.TimeDataManage
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.view.kline.BaseChart
import com.zhuorui.securities.market.customer.view.kline.markerView.KLineDayHighlightView
import com.zhuorui.securities.market.customer.view.kline.dataManage.KLineDataManage
import com.zhuorui.securities.market.customer.view.kline.model.TimeDataModel
import com.zhuorui.securities.market.databinding.FragmentKlineFiveDayBinding
import com.zhuorui.securities.market.ui.kline.presenter.ChartFiveDayPresenter
import com.zhuorui.securities.market.ui.kline.view.FiveDayKlineView
import com.zhuorui.securities.market.ui.kline.viewmodel.FiveDayKlineViewModel
import kotlinx.android.synthetic.main.fragment_kline_five_day.*
import me.jessyan.autosize.internal.CustomAdapt

/**
 * 五日K线
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
        return true
    }

    override fun getSizeInDp(): Float {
        return 667f
    }
}