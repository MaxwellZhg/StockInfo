package com.zhuorui.securities.market.ui.kline

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.view.kline.BaseChart
import com.zhuorui.securities.market.customer.view.kline.markerView.KLineDayHighlightView
import com.zhuorui.securities.market.customer.view.kline.dataManage.KLineDataManage
import com.zhuorui.securities.market.customer.view.kline.dataManage.TimeDataManage
import com.zhuorui.securities.market.customer.view.kline.model.TimeDataModel
import com.zhuorui.securities.market.databinding.FragmentOneDayBinding
import com.zhuorui.securities.market.socket.vo.kline.MinuteKlineData
import com.zhuorui.securities.market.ui.kline.presenter.ChartOneDayPresenter
import com.zhuorui.securities.market.ui.kline.view.OneDayKlineView
import com.zhuorui.securities.market.ui.kline.viewmodel.OneDayKlineViewModel
import kotlinx.android.synthetic.main.fragment_one_day.*
import org.json.JSONException
import org.json.JSONObject

/**
 * 分时K线
 */
open class ChartOneDayFragment :
    AbsFragment<FragmentOneDayBinding, OneDayKlineViewModel, OneDayKlineView, ChartOneDayPresenter>(),
    OneDayKlineView, IKLine, BaseChart.OnHighlightValueSelectedListener {

    private var mHighlightListener: OnKlineHighlightListener? = null
    private var mHighlightShow = false

    companion object {

        /**
         * @param model 0模拟炒股 1个股详情 2个股详情横屏 3指数-简单样式
         */
        fun newInstance(ts: String, code: String, tsCode: String, type: Int, model: Int): ChartOneDayFragment {
            val fragment = ChartOneDayFragment()
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
        val model = arguments?.getInt("model")!!

        chart!!.initChart(model)
        chart.setHighlightValueSelectedListener(this)
//        presenter?.init(ts, code, tsCode, type)
//        //测试数据
//        try {
//            //上证指数代码000001.IDX.SH
//            val kTimeData = TimeDataManage()
//            kTimeData.parseTimeData(JSONObject(ChartData.TIMEDATA), "000001.IDX.SH", 0.0)
//            chart.setDataToChart(kTimeData)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
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

    override fun onDayHighlightValueListener(mData: TimeDataManage?, index: Int, isSelect: Boolean) {
        val datas = mData?.realTimeData
        val data = if (isSelect && datas != null && datas.size > index && index > -1) datas[index] else TimeDataModel()
        if (isSelect && data != null) {
            if (mHighlightShow != isSelect) {
                mHighlightListener?.onShowHighlightView(getHighlightView(data))
            } else {
                mHighlightListener?.onUpHighlightData(data)
            }
            mHighlightShow = isSelect
        } else if (mHighlightShow != isSelect) {
            mHighlightShow = false
            mHighlightListener?.onHideHighlightView()
        }

    }

    private fun getHighlightView(data: TimeDataModel): KLineDayHighlightView {
        val v = KLineDayHighlightView(context)
        v.setData(data)
        return v
    }

    override fun onKHighlightValueListener(data: KLineDataManage?, index: Int, isSelect: Boolean) {


    }

    override fun setHighlightListener(l: OnKlineHighlightListener?) {
        mHighlightListener = l
    }
}