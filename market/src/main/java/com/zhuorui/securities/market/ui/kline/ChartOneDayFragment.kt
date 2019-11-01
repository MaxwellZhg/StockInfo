package com.zhuorui.securities.market.ui.kline

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.view.kline.dataManage.TimeDataManage
import com.zhuorui.securities.market.customer.view.kline.model.TimeDataModel
import com.zhuorui.securities.market.databinding.FragmentOneDayBinding
import com.zhuorui.securities.market.socket.vo.kline.MinuteKlineData
import com.zhuorui.securities.market.ui.kline.presenter.ChartOneDayPresenter
import com.zhuorui.securities.market.ui.kline.view.OneDayKlineView
import com.zhuorui.securities.market.ui.kline.viewmodel.OneDayKlineViewModel
import kotlinx.android.synthetic.main.fragment_one_day.*

/**
 * 分时页
 */
class ChartOneDayFragment :
    AbsFragment<FragmentOneDayBinding, OneDayKlineViewModel, OneDayKlineView, ChartOneDayPresenter>(),
    OneDayKlineView {

    companion object {
        fun newInstance(ts: String, code: String, tsCode: String, type: Int, land: Boolean): ChartOneDayFragment {
            val fragment = ChartOneDayFragment()
            val bundle = Bundle()
            bundle.putString("ts", ts)
            bundle.putString("code", code)
            bundle.putString("tsCode", tsCode)
            bundle.putInt("type", type)
            bundle.putBoolean("landscape", land)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_one_day

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: ChartOneDayPresenter
        get() = ChartOneDayPresenter()

    override val createViewModel: OneDayKlineViewModel?
        get() = ViewModelProviders.of(this).get(OneDayKlineViewModel::class.java)

    override val getView: OneDayKlineView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        val ts = arguments?.getString("ts")
        val code = arguments?.getString("code")
        val tsCode = arguments?.getString("tsCode")
        val type = arguments?.getInt("type")
        val landscape = arguments?.getBoolean("landscape")!!

        chart!!.initChart()
        presenter?.init(ts, code, tsCode, type)
    }

    override fun setDataToChart(timeDataManage: TimeDataManage?) {
        chart?.setDataToChart(timeDataManage)
    }

    override fun dynamicsUpdateOne(data: MinuteKlineData) {
        val timeData = TimeDataModel()
        timeData.nowPrice = data.price
        timeData.averagePrice = data.avgPrice
        timeData.volume = data.vol.toInt()
        timeData.timeMills = data.dateTime
        timeData.open = data.openPrice
        chart.dynamicsUpdateOne(timeData)
    }

    override fun dynamicsAddOne(data: MinuteKlineData) {
        val timeData = TimeDataModel()
        timeData.nowPrice = data.price
        timeData.averagePrice = data.avgPrice
        timeData.volume = data.vol.toInt()
        timeData.timeMills = data.dateTime
        timeData.open = data.openPrice
        chart.dynamicsAddOne(timeData)
    }
}