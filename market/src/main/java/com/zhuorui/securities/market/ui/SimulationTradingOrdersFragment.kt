package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuorui.commonwidget.dialog.DatePickerDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentSimulationTradingOrdersBinding
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
    SimulationTradingOrdersView, View.OnClickListener, SimulationTradingOrderAdapter.MockStockOrderListener {

    val timeFormat = "yyyy-MM-dd"
    var todayOrderAdapter: SimulationTradingOrderAdapter? = null
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

    /**
     * 改单
     */
    override fun toChangeOrder(id: String) {
    }

    /**
     * 撤单
     */
    override fun toCancelOrder(id: String) {
    }

    /**
     * 去买卖
     */
    override fun toBusiness(id: String) {
        start(SimulationTradingStocksFragment.newInstance())
    }

    /**
     * 去行情
     */
    override fun toQuotation(id: String) {
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        datePicker = context?.let { DatePickerDialog(it) }
        start_date.setOnClickListener(this)
        end_date.setOnClickListener(this)
        tabTitle = ResUtil.getStringArray(R.array.order_screen_time)
        magic_indicator.navigator = getNavigator()
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = getMockStockOrderAdapter()
        onSelect(0)
    }

    private fun onSelect(index: Int) {
        val staTime = TimeZoneUtil.currentTime(timeFormat)
        var endTime: String = staTime
        when (index) {
            0 -> {
                endTime = TimeZoneUtil.timeFormat(TimeZoneUtil.addDayByCurrentTime(7), timeFormat)
            }
            1 -> {
                endTime = TimeZoneUtil.timeFormat(TimeZoneUtil.addMonthByCurrentTime(1), timeFormat)
            }
            2 -> {
                endTime = TimeZoneUtil.timeFormat(TimeZoneUtil.addMonthByCurrentTime(3), timeFormat)
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
                datePicker?.setCurrentData(start_date.text.toString(), "yyyy-MM-dd")
                datePicker?.show()
            }
            end_date -> {
                datePicker?.setOnDateSelectedListener(object : DatePickerDialog.OnDateSelectedListener {
                    override fun onDateSelected(date: String) {
                        end_date?.text = date
                        presenter?.getOrders(start_date?.text.toString(), date)
                    }
                })
                datePicker?.setCurrentData(end_date.text.toString(), "yyyy-MM-dd")
                datePicker?.show()

            }

        }

    }

    fun getMockStockOrderAdapter(): SimulationTradingOrderAdapter {
        if (todayOrderAdapter == null) {
            todayOrderAdapter = context?.let { SimulationTradingOrderAdapter(it) }
            todayOrderAdapter?.listener = this
        }
        todayOrderAdapter?.setEmptyMassge(ResUtil.getString(R.string.str_no_record_historical_transactions)!!)
        return todayOrderAdapter!!
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
                colorTransitionPagerTitleView.normalColor = ResUtil.getColor(R.color.color_232323)!!
                colorTransitionPagerTitleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
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