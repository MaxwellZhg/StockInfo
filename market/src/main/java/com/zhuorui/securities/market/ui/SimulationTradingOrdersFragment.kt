package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.zhuorui.commonwidget.dialog.DatePickerDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentSimulationTradingOrdersBinding
import com.zhuorui.securities.market.model.STOrderData
import com.zhuorui.securities.market.ui.adapter.SimulationTradingOrderAdapter
import com.zhuorui.securities.market.ui.presenter.SimulationTradingOrdersPresenter
import com.zhuorui.securities.market.ui.view.SimulationTradingOrdersView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingOrdersViewModel
import kotlinx.android.synthetic.main.fragment_simulation_trading_orders.*
import kotlinx.android.synthetic.main.fragment_simulation_trading_main.recycler_view
import net.lucode.hackware.magicindicator.abs.IPagerNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView


/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 14:05
 *    desc   : 模拟炒股历史订单
 */
class SimulationTradingOrdersFragment :
    AbsSwipeBackNetFragment<FragmentSimulationTradingOrdersBinding, SimulationTradingOrdersViewModel, SimulationTradingOrdersView, SimulationTradingOrdersPresenter>(),
    SimulationTradingOrdersView, View.OnClickListener, OnRefreshListener, OnLoadMoreListener {

    private val timeFormat = "yyyy-MM-dd"
    var orderAdapter: SimulationTradingOrderAdapter? = null
    var tabTitle: Array<String>? = null
    var datePicker: DatePickerDialog? = null

    companion object {
        fun newInstance(): SimulationTradingOrdersFragment {
            return SimulationTradingOrdersFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_simulation_trading_orders

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: SimulationTradingOrdersPresenter
        get() = SimulationTradingOrdersPresenter()

    override val createViewModel: SimulationTradingOrdersViewModel?
        get() = ViewModelProviders.of(this).get(SimulationTradingOrdersViewModel::class.java)

    override val getView: SimulationTradingOrdersView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        datePicker = context?.let { DatePickerDialog(it) }
        start_date.setOnClickListener(this)
        end_date.setOnClickListener(this)
        smart_refresh.setOnRefreshListener(this)
        smart_refresh.setOnLoadMoreListener(this)
        tabTitle = ResUtil.getStringArray(com.zhuorui.securities.market.R.array.order_screen_time)
        magic_indicator.navigator = getNavigator()
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = getMockStockOrderAdapter()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        presenter?.getOrdersMore()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        smart_refresh.setEnableLoadMore(false)
        presenter?.refresh()
    }

    override fun onFinishRefresh() {
        smart_refresh.finishRefresh()
    }

    override fun onFinishLoadMore() {
        smart_refresh.finishLoadMore()
    }

    override fun addData(total: Int, list: List<STOrderData>?) {
        orderAdapter?.addDatas(list)
        orderAdapter?.notifyDataSetChanged()
        val dataSize = orderAdapter!!.datas!!.size
        smart_refresh.setEnableLoadMore(dataSize < total)
        orderAdapter?.setEmptyMassge(ResUtil.getString(R.string.str_no_record_historical_transactions)!!)
    }

    override fun onRefreshData() {
        orderAdapter?.clear()
        orderAdapter?.setEmptyMassge(ResUtil.getString(R.string.loading_data)!!)
        orderAdapter?.notifyDataSetChanged()
        smart_refresh.setEnableLoadMore(false)
    }

    override fun getDataError(msg: String) {
        orderAdapter?.setEmptyMassge(msg)
        smart_refresh
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        onSelect(0)
    }

    private fun onSelect(index: Int) {
        val endTime = TimeZoneUtil.currentTime(timeFormat)
        var staTime: String = endTime
        when (index) {
            0 -> {
                staTime = TimeZoneUtil.timeFormat(TimeZoneUtil.addDayByCurrentTime(-7), timeFormat)
            }
            1 -> {
                staTime = TimeZoneUtil.timeFormat(TimeZoneUtil.addMonthByCurrentTime(-1), timeFormat)
            }
            2 -> {
                staTime = TimeZoneUtil.timeFormat(TimeZoneUtil.addMonthByCurrentTime(-3), timeFormat)
            }
        }
        start_date?.text = staTime
        end_date?.text = endTime
        presenter?.getOrders(staTime, endTime)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            start_date -> {
                datePicker?.setOnDateSelectedListener(object : DatePickerDialog.OnDateSelectedListener {
                    override fun onDateSelected(date: String) {
                        start_date?.text = date
                        presenter?.getOrders(date, end_date?.text.toString())
                    }
                })
                datePicker?.setCurrentData(start_date.text.toString(), timeFormat)
                datePicker?.show()
            }
            end_date -> {
                datePicker?.setOnDateSelectedListener(object : DatePickerDialog.OnDateSelectedListener {
                    override fun onDateSelected(date: String) {
                        end_date?.text = date
                        presenter?.getOrders(start_date?.text.toString(), date)
                    }
                })
                datePicker?.setCurrentData(end_date.text.toString(), timeFormat)
                datePicker?.show()

            }

        }

    }

    fun getMockStockOrderAdapter(): SimulationTradingOrderAdapter {
        if (orderAdapter == null) {
            orderAdapter = context?.let { SimulationTradingOrderAdapter(it) }
            orderAdapter?.canClick = false
        }
        return orderAdapter!!
    }


    /**
     * 获取指示器适配器
     */
    private fun getNavigator(): IPagerNavigator? {
        // 设置viewpager指示器
        val commonNavigator = CommonNavigator(requireContext())
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return tabTitle?.size!!
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor =
                    ResUtil.getColor(R.color.color_232323)!!
                colorTransitionPagerTitleView.selectedColor =
                    ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.textSize = 14f
                colorTransitionPagerTitleView.text = tabTitle!![index]
                colorTransitionPagerTitleView.setOnClickListener {
                    magic_indicator?.onPageSelected(index)
                    magic_indicator?.onPageScrolled(index, 0.0F, 0)
                    onSelect(index)
                }
                return colorTransitionPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                indicator.setColors(ResUtil.getColor(R.color.tab_select))
                indicator.lineHeight = ResUtil.getDimensionDp2Px(2f).toFloat()
                indicator.lineWidth = ResUtil.getDimensionDp2Px(33f).toFloat()
                return indicator
            }
        }
        return commonNavigator
    }


}


