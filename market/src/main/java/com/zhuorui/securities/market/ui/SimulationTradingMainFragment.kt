package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuorui.securities.base2app.BaseApplication.Companion.context
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.view.SimulationTradingFundAccountView
import com.zhuorui.securities.market.databinding.FragmentSimulationTradingMainBinding
import com.zhuorui.securities.market.ui.presenter.SimulationTradingMainPresenter
import com.zhuorui.securities.market.ui.view.SimulationTradingMainView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingMainViewModel
import com.zhuorui.securities.personal.ui.MessageFragment
import kotlinx.android.synthetic.main.fragment_simulation_trading_main.*
import kotlinx.android.synthetic.main.fragment_simulation_trading_main.top_bar
import kotlinx.android.synthetic.main.fragment_simulation_trading_stocks.*
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

    var vfundAccount: SimulationTradingFundAccountView? = null
    var holdPositionsAdapter: HoldPositionsListAdapter? = null
    var todayOrderAdapter: SimulationTradingOrderAdapter? = null
    var magicIndicator: MagicIndicator? = null
    var vHeader: View? = null
    var vRule: View? = null
    var tabTitle: Array<String>? = null

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
            vRule -> {
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
        LogInfra.Log.i("lw", id)
    }

    /**
     * 撤单
     */
    override fun toCancelOrder(id: String) {
        LogInfra.Log.i("lw", id)
    }

    /**
     * 去行情
     */
    override fun toQuotation(id: String) {
        LogInfra.Log.i("lw", id)
    }

    /**
     * 去创建资金账号
     */
    override fun toCreateFundAccount() {
        presenter?.createFundAccount()
    }

    override fun createFundAccountSuccess() {
        vfundAccount?.createFundAccountSuccess()
    }

    private fun onSelect(index: Int) {
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
        top_bar.setRightClickListener {
            // 消息
            start(MessageFragment.newInstance())
        }
        top_bar.setRight2ClickListener {
            // 搜索
            start(SimulationTradingSearchFragment.newInstance())
        }
        recycler_view.layoutManager = LinearLayoutManager(context)
        tabTitle = getTabTitleData()
        getHeaderView()
        vfundAccount?.setData { false }
        onSelect(0)
    }

    private fun getTabTitleData(): Array<String>? {
        val hpNum = 0
        val toNum = 0
        return arrayOf(
            ResUtil.getString(R.string.str_hold_positions) + "($hpNum)",
            ResUtil.getString(R.string.str_today_orders) + "($toNum)"
        )
    }

    private fun notifyTitleTab() {
        tabTitle = getTabTitleData()
        magicIndicator?.navigator?.notifyDataSetChanged()
    }

    fun getHoldpositionsAdapter(): HoldPositionsListAdapter {
        if (holdPositionsAdapter == null) {
            holdPositionsAdapter = context?.let { HoldPositionsListAdapter(it) }
            holdPositionsAdapter?.listener = this
        }
        todayOrderAdapter?.setHeaderView(null)
        holdPositionsAdapter?.setHeaderView(getHeaderView())
        return holdPositionsAdapter!!
    }

    fun getMockStockOrderAdapter(): SimulationTradingOrderAdapter {
        if (todayOrderAdapter == null) {
            todayOrderAdapter = context?.let { SimulationTradingOrderAdapter(it) }
            todayOrderAdapter?.listener = this
        }
        holdPositionsAdapter?.setHeaderView(null)
        todayOrderAdapter?.setHeaderView(getHeaderView())
        return todayOrderAdapter!!
    }

    fun getHeaderView(): View {
        if (vHeader == null) {
            vHeader = LayoutInflater.from(context).inflate(R.layout.layout_simulation_trading_main_top, null)
            vRule = vHeader?.findViewById(R.id.zr_rule)
            vRule?.setOnClickListener(this)
            vfundAccount = vHeader?.findViewById(R.id.fund_account)
            vfundAccount?.setOnMockStockFundAccountListener(this)
            magicIndicator = vHeader?.findViewById(R.id.magic_indicator)
            magicIndicator?.navigator = getNavigator()
        }
        val vp: ViewParent? = vHeader?.parent
        if (vp != null) {
            (vp as ViewGroup).removeView(vHeader)
        }
        return vHeader!!
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
                    magicIndicator?.onPageSelected(index)
                    magicIndicator?.onPageScrolled(index, 0.0F, 0)
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