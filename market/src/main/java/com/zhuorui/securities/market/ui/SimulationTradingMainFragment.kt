package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuorui.commonwidget.dialog.ConfirmToCancelDialog
import com.zhuorui.commonwidget.dialog.TitleMessageConfirmDialog
import com.zhuorui.securities.base2app.BaseApplication.Companion.context
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.view.SimulationTradingFundAccountView
import com.zhuorui.securities.market.databinding.FragmentSimulationTradingMainBinding
import com.zhuorui.securities.market.model.STFundAccountData
import com.zhuorui.securities.market.model.STOrderData
import com.zhuorui.securities.market.model.STPositionData
import com.zhuorui.securities.market.ui.presenter.SimulationTradingMainPresenter
import com.zhuorui.securities.market.ui.view.SimulationTradingMainView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingMainViewModel
import com.zhuorui.securities.personal.ui.MessageFragment
import kotlinx.android.synthetic.main.fragment_simulation_trading_main.*
import kotlinx.android.synthetic.main.fragment_simulation_trading_main.top_bar
import kotlinx.android.synthetic.main.fragment_simulation_trading_stocks.*
import kotlinx.android.synthetic.main.layout_simulation_trading_main_top.*
import net.lucode.hackware.magicindicator.MagicIndicator
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
 *    desc   : 模拟炒股首页
 */
class SimulationTradingMainFragment :
    AbsSwipeBackNetFragment<FragmentSimulationTradingMainBinding, SimulationTradingMainViewModel, SimulationTradingMainView, SimulationTradingMainPresenter>(),
    SimulationTradingMainView, SimulationTradingFundAccountView.OnMockStockFundAccountListener,
    SimulationTradingOrderAdapter.MockStockOrderListener, View.OnClickListener {

    private var holdPositionsAdapter: HoldPositionsListAdapter? = null
    private var todayOrderAdapter: SimulationTradingOrderAdapter? = null
    private var tabTitle: Array<String>? = null
    private var mIndex: Int = 0

    companion object {
        fun newInstance(): SimulationTradingMainFragment {
            return SimulationTradingMainFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_simulation_trading_main

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: SimulationTradingMainPresenter
        get() = SimulationTradingMainPresenter()

    override val createViewModel: SimulationTradingMainViewModel?
        get() = ViewModelProviders.of(this).get(SimulationTradingMainViewModel::class.java)

    override val getView: SimulationTradingMainView
        get() = this

    override fun onClick(p0: View?) {
        when (p0) {
            zr_rule -> {
                start(SimulationTradingRuleFragment.newInstance())
            }

        }
    }

    /**
     * 去买卖
     */
    override fun toBusiness() {
        start(SimulationTradingStocksFragment.newInstance())
    }

    /**
     * 去订单
     */
    override fun toOrder() {
        start(SimulationTradingOrdersFragment.newInstance())
    }

    /**
     * 去买卖
     */
    override fun toBusiness(id: String) {
        start(SimulationTradingStocksFragment.newInstance())
    }

    /**
     * 改单
     */
    override fun toChangeOrder(id: String) {
        start(SimulationTradingStocksFragment.newInstance())
    }

    /**
     * 撤单
     */
    override fun toCancelOrder(id: String) {
    }

    /**
     * 去行情
     */
    override fun toQuotation(id: String) {
    }

    /**
     * 去创建资金账号
     */
    override fun toCreateFundAccount() {
        presenter?.createFundAccount()
    }

    override fun createFundAccountSuccess() {
        fund_account?.createFundAccountSuccess()
        TitleMessageConfirmDialog.createWidth225Dialog(context!!, false, true)
            .setTitleText("")
            .setMsgText("恭喜您已获得100万港币模拟资金开始模拟炒股吧！")
            .setConfirmText("已了解")
            .show()
    }

    override fun onFundAccountData(data: STFundAccountData) {
        fund_account?.setData(data)
    }

    override fun onPositionDatas(list: List<STPositionData>) {
        val adapter = getHoldpositionsAdapter()
        adapter.setData(list)
        if (mIndex == 0) {
            adapter.notifyDataSetChanged()
        }
        notifyTitleTab()
    }

    override fun onOrderDatas(list: List<STOrderData>) {
        val adapter = getMockStockOrderAdapter()
        adapter.clear()
        adapter.addDatas(list)
        if (mIndex == 0) {
            adapter.notifyDataSetChanged()
        }
        notifyTitleTab()
    }

    private fun onSelect(index: Int) {
        mIndex = index
        when (index) {
            0 -> {
                recycler_view.adapter = getHoldpositionsAdapter()
            }
            1 -> {
                recycler_view.adapter = getMockStockOrderAdapter()
            }

        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        presenter?.setLifecycleOwner(this)
        top_bar.setRightClickListener {
            // 消息
            start(MessageFragment.newInstance())
        }
        top_bar.setRight2ClickListener {
            // 搜索自选股
            start(StockSearchFragment.newInstance())
        }
        recycler_view.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        //解决数据加载不完的问题
        recycler_view.isNestedScrollingEnabled = false
        recycler_view.setHasFixedSize(true)
        //解决数据加载完成后, 没有停留在顶部的问题
        recycler_view.isFocusable = false
        tabTitle = getTabTitleData()
        zr_rule.setOnClickListener(this)
        fund_account.setOnMockStockFundAccountListener(this)
        magic_indicator.navigator = getNavigator()
        presenter?.getFundAccount()
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        onSelect(0)
        fund_account?.setData(STFundAccountData())
    }

    private fun getTabTitleData(): Array<String>? {
        val hpNum = getHoldpositionsAdapter().datas?.size
        val toNum = getMockStockOrderAdapter().datas?.size
        return arrayOf(
            ResUtil.getString(R.string.str_hold_positions) + "($hpNum)",
            ResUtil.getString(R.string.str_today_orders) + "($toNum)"
        )
    }

    private fun notifyTitleTab() {
        tabTitle = getTabTitleData()
        magic_indicator.navigator?.notifyDataSetChanged()
    }

    fun getHoldpositionsAdapter(): HoldPositionsListAdapter {
        if (holdPositionsAdapter == null) {
            holdPositionsAdapter = context?.let { HoldPositionsListAdapter(it) }
            holdPositionsAdapter?.listener = this
        }
        return holdPositionsAdapter!!
    }

    fun getMockStockOrderAdapter(): SimulationTradingOrderAdapter {
        if (todayOrderAdapter == null) {
            todayOrderAdapter = context?.let { SimulationTradingOrderAdapter(it) }
            todayOrderAdapter?.listener = this
        }
        return todayOrderAdapter!!
    }

    /**
     * 获取指示器适配器
     */
    private fun getNavigator(): IPagerNavigator? {
        // 设置viewpager指示器
        val commonNavigator = CommonNavigator(requireContext())
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return tabTitle?.size!!
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = ResUtil.getColor(R.color.color_232323)!!
                colorTransitionPagerTitleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.textSize = 18f
                colorTransitionPagerTitleView.text = tabTitle!![index]
                colorTransitionPagerTitleView.setOnClickListener {
                    magic_indicator.onPageSelected(index)
                    magic_indicator.onPageScrolled(index, 0.0F, 0)
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