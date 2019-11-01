package com.zhuorui.securities.market.ui.kline

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.market.customer.view.kline.dataManage.TimeDataManage
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentFiveDayBinding
import com.zhuorui.securities.market.ui.kline.presenter.ChartFiveDayPresenter
import com.zhuorui.securities.market.ui.kline.view.FiveDayKlineView
import com.zhuorui.securities.market.ui.kline.viewmodel.FiveDayKlineViewModel
import kotlinx.android.synthetic.main.fragment_five_day.*

/**
 * 五日分时页
 */
class ChartFiveDayFragment :
    AbsFragment<FragmentFiveDayBinding, FiveDayKlineViewModel, FiveDayKlineView, ChartFiveDayPresenter>(),
    FiveDayKlineView {

    companion object {

        fun newInstance(ts: String, code: String, tsCode: String, type: Int, land: Boolean): ChartFiveDayFragment {
            val fragment = ChartFiveDayFragment()
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
        get() = R.layout.fragment_five_day


    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: ChartFiveDayPresenter
        get() = ChartFiveDayPresenter()

    override val createViewModel: FiveDayKlineViewModel?
        get() = ViewModelProviders.of(this).get(FiveDayKlineViewModel::class.java)

    override val getView: FiveDayKlineView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        val ts = arguments?.getString("ts")
        val code = arguments?.getString("code")
        val tsCode = arguments?.getString("tsCode")
        val type = arguments?.getInt("type")
        val landscape = arguments?.getBoolean("landscape")!!

        chart!!.initChart(landscape)

        presenter?.loadKlineFiveDayData()
    }

    override fun setDataToChart(timeDataManage: TimeDataManage?) {
        chart?.setDataToChart(timeDataManage)
    }
}