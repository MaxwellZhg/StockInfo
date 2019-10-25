package com.zhuorui.securities.market.ui.kline

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.market.customer.view.kline.dataManage.TimeDataManage
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentOneDayBinding
import com.zhuorui.securities.market.ui.kline.presenter.ChartOneDayPresenter
import com.zhuorui.securities.market.ui.kline.view.OneDayKlineView
import com.zhuorui.securities.market.ui.kline.viewmodel.OneDayKlineViewModel
import kotlinx.android.synthetic.main.fragment_one_day.*
import org.json.JSONException
import org.json.JSONObject

/**
 * 分时页
 */
class ChartOneDayFragment :
    AbsFragment<FragmentOneDayBinding, OneDayKlineViewModel, OneDayKlineView, ChartOneDayPresenter>(),
    OneDayKlineView {

    private var land: Boolean = false // 是否横屏

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
        land = arguments!!.getBoolean("landscape")
        chart!!.initChart(land)

//        presenter?.loadKNetlineMinuteData()

        //测试数据
        try {
            //上证指数代码000001.IDX.SH
            val kTimeData = TimeDataManage()
            kTimeData.parseTimeData(JSONObject(ChartData.TIMEDATA), "000001.IDX.SH", 0.0)
            chart.setDataToChart(kTimeData)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun setDataToChart(timeDataManage: TimeDataManage?) {
        chart?.setDataToChart(timeDataManage)
    }
}