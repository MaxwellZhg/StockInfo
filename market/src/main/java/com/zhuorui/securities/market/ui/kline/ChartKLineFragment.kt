package com.zhuorui.securities.market.ui.kline

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.market.customer.view.kline.dataManage.KLineDataManage
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentKlineBinding
import com.zhuorui.securities.market.ui.kline.presenter.ChartKLinePresenter
import com.zhuorui.securities.market.ui.kline.view.KlineView
import com.zhuorui.securities.market.ui.kline.viewmodel.KlineViewModel
import kotlinx.android.synthetic.main.fragment_kline.*

/**
 * K线
 */
class ChartKLineFragment : AbsFragment<FragmentKlineBinding, KlineViewModel, KlineView, ChartKLinePresenter>(),
    KlineView,IKLine {

    companion object {

        fun newInstance(ts: String, code: String, tsCode: String, type: Int, klinType:Int, land: Boolean): ChartKLineFragment {
            val fragment = ChartKLineFragment()
            val bundle = Bundle()
            bundle.putString("ts", ts)
            bundle.putString("code", code)
            bundle.putString("tsCode", tsCode)
            bundle.putInt("type", type)
            bundle.putInt("klinType", klinType)
            bundle.putBoolean("landscape", land)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_kline

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: ChartKLinePresenter
        get() = ChartKLinePresenter()

    override val createViewModel: KlineViewModel?
        get() = ViewModelProviders.of(this).get(KlineViewModel::class.java)

    override val getView: KlineView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        val ts = arguments?.getString("ts")
        val code = arguments?.getString("code")
        val tsCode = arguments?.getString("tsCode")
        val type = arguments?.getInt("type")
        val landscape = arguments?.getBoolean("landscape")!!

        combinedchart.initChart(landscape)

        presenter?.kType = arguments!!.getInt("klinType")
        presenter?.land = landscape

//        try {
//            if (mType == 1) {
//                `object` = JSONObject(ChartData.KLINEDATA)
//            } else if (mType == 7) {
//                `object` = JSONObject(ChartData.KLINEWEEKDATA)
//            } else if (mType == 30) {
//                `object` = JSONObject(ChartData.KLINEMONTHDATA)
//            }
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }

        combinedchart.getGestureListenerBar().setCoupleClick {
            if (landscape) {
                presenter?.loadIndexData()
            }
        }

        presenter?.loadKlineData()
    }

    override fun doBarChartSwitch(indexType: Int) {
        combinedchart.doBarChartSwitch(indexType)
    }

    override fun setDataToChart(kLineDataManage: KLineDataManage?) {
        combinedchart.setDataToChart(kLineDataManage)
    }

    override fun setHighlightListener(l: OnKlineHighlightListener?) {


    }
}