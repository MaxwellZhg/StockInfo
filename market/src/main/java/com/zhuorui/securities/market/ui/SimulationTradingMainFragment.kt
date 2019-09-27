package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zhuorui.commonwidget.dialog.ConfirmToCancelDialog
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.commonwidget.dialog.TitleMessageConfirmDialog
import com.zhuorui.securities.base2app.dialog.BaseDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.customer.view.SimulationTradingFundAccountView
import com.zhuorui.securities.market.databinding.FragmentSimulationTradingMainBinding
import com.zhuorui.securities.market.model.STFundAccountData
import com.zhuorui.securities.market.model.STOrderData
import com.zhuorui.securities.market.model.STPositionData
import com.zhuorui.securities.market.ui.adapter.HoldPositionsListAdapter
import com.zhuorui.securities.market.ui.adapter.SimulationTradingOrderAdapter
import com.zhuorui.securities.market.ui.presenter.SimulationTradingMainPresenter
import com.zhuorui.securities.market.ui.view.SimulationTradingMainView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingMainViewModel
import com.zhuorui.securities.personal.ui.MessageFragment
import kotlinx.android.synthetic.main.fragment_simulation_trading_main.*
import kotlinx.android.synthetic.main.layout_simulation_trading_main_top.*
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
    private var loading: ProgressDialog? = null
    private var fist: Boolean = true
    private var confirmDialog: BaseDialog? = null

    companion object {
        fun newInstance(): SimulationTradingMainFragment {
            return SimulationTradingMainFragment()
        }
    }

    override val layout: Int
        get() = com.zhuorui.securities.market.R.layout.fragment_simulation_trading_main

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

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
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
        presenter?.setLifecycleOwner(this)
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        if (fist) {
            fist = false
            presenter?.getFundAccount()
            onSelect(0)
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (!fist) {
            presenter?.getFundAccount()
        }
    }

    override fun onPause() {
        super.onPause()
        confirmDialog?.hide()
        hideLoading()
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
    override fun toBusiness(type: Int, data: STOrderData) {
        start(SimulationTradingStocksFragment.newInstance(type, data))
    }

    /**
     * 改单
     */
    override fun toChangeOrder(data: STOrderData) {
        start(SimulationTradingStocksFragment.newInstance(SimulationTradingStocksFragment.TRAD_TYPE_UPDATE_ORDER, data))
    }

    /**
     * 撤单
     */
    override fun toCancelOrder(data: STOrderData) {
        confirmDialog = ConfirmToCancelDialog.createWidth265Dialog(context!!, false, true)
            .setTitleText(ResUtil.getString(com.zhuorui.securities.market.R.string.str_tips)!!)
            .setMsgText(com.zhuorui.securities.market.R.string.str_confirm_withdrawal)
            .setConfirmText(ResUtil.getString(com.zhuorui.securities.market.R.string.str_confirm)!!)
            .setCancelText(ResUtil.getString(com.zhuorui.securities.market.R.string.str_cancel)!!)
            .setCallBack(object : ConfirmToCancelDialog.CallBack {
                override fun onCancel() {
                }

                override fun onConfirm() {
                    val trustId = data.id!!
                    when (data.trustType) {
                        1 -> {
                            presenter?.cancelBuyTrust(trustId)
                        }
                        2 -> {
                            presenter?.cancelSellTrust(trustId)
                        }
                    }

                }
            })
        confirmDialog?.show()
    }

    /**
     * 去行情
     */
    override fun toQuotation(code: String, ts: String) {
    }

    /**
     * 去创建资金账号
     */
    override fun toCreateFundAccount() {
        presenter?.createFundAccount()
    }

    override fun cancelTrustSuccess() {
        ToastUtil.instance.toast(com.zhuorui.securities.market.R.string.str_trust_success)
    }

    override fun cancelTrustError(msg: String?) {
        ToastUtil.instance.toast(msg.toString())
    }

    override fun createFundAccountSuccess() {
        fund_account?.createFundAccountSuccess()
        TitleMessageConfirmDialog.createWidth225Dialog(context!!, false, true)
            .setTitleText("")
            .setMsgText(com.zhuorui.securities.market.R.string.create_fund_account_success)
            .setConfirmText(com.zhuorui.securities.market.R.string.str_understood)
            .show()
    }

    override fun onUpData(
        positionDatas: List<STPositionData>?,
        orderDatas: List<STOrderData>?,
        fundAccount: STFundAccountData
    ) {
        val postiosAdapter = getHoldpositionsAdapter()
        postiosAdapter.setData(positionDatas)
        val orderAdapter = getMockStockOrderAdapter()
        orderAdapter.clear()
        orderAdapter.addDatas(orderDatas)
        if (mIndex == 0) {
            postiosAdapter.notifyDataSetChanged()
        } else if (mIndex == 1) {
            orderAdapter.notifyDataSetChanged()
        }
        notifyTitleTab()
        fund_account?.setData(fundAccount)
    }

    override fun onGetFundAccountError(code: String?, msg: String?) {
        confirmDialog = ConfirmToCancelDialog.createWidth265Dialog(context!!, false, true)
            .setTitleText(ResUtil.getString(com.zhuorui.securities.market.R.string.str_tips)!!)
            .setMsgText(msg.toString())
            .setConfirmText(ResUtil.getString(com.zhuorui.securities.market.R.string.str_retry)!!)
            .setCancelText(ResUtil.getString(com.zhuorui.securities.market.R.string.str_back)!!)
            .setCallBack(object : ConfirmToCancelDialog.CallBack {
                override fun onCancel() {
                    pop()
                }

                override fun onConfirm() {
                    presenter?.getFundAccount()
                }
            })
        confirmDialog?.show()
    }

    override fun onCreateFundAccountError(code: String, message: String?) {
        confirmDialog = ConfirmToCancelDialog.createWidth265Dialog(context!!, false, true)
            .setTitleText(ResUtil.getString(com.zhuorui.securities.market.R.string.str_tips)!!)
            .setMsgText(message.toString())
            .setConfirmText(ResUtil.getString(com.zhuorui.securities.market.R.string.str_retry)!!)
            .setCancelText(ResUtil.getString(com.zhuorui.securities.market.R.string.str_back)!!)
            .setCallBack(object : ConfirmToCancelDialog.CallBack {
                override fun onCancel() {
                }

                override fun onConfirm() {
                    presenter?.createFundAccount()
                }
            })
        confirmDialog?.show()
    }

    /**
     * 选择监听
     */
    override fun onItemClick(position: Int, selected: Boolean) {
        if (selected) {
            when (mIndex) {
                0 -> {
                    if (getHoldpositionsAdapter().itemCount - 1 == position)
                        recycler_view.post { scroll_view.fullScroll(ScrollView.FOCUS_DOWN) }
                }
                1 -> {
                    if (getMockStockOrderAdapter().itemCount - 1 == position)
                        recycler_view.post { scroll_view.fullScroll(ScrollView.FOCUS_DOWN) }
                }
            }
        }
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

    private fun getTabTitleData(): Array<String>? {
        val hpNum = getHoldpositionsAdapter().datas?.size
        val toNum = getMockStockOrderAdapter().datas?.size
        return arrayOf(
            ResUtil.getString(com.zhuorui.securities.market.R.string.str_hold_positions) + "($hpNum)",
            ResUtil.getString(com.zhuorui.securities.market.R.string.str_today_orders) + "($toNum)"
        )
    }

    private fun notifyTitleTab() {
        tabTitle = getTabTitleData()
        magic_indicator.navigator?.notifyDataSetChanged()
    }

    private fun getHoldpositionsAdapter(): HoldPositionsListAdapter {
        if (holdPositionsAdapter == null) {
            holdPositionsAdapter = context?.let { HoldPositionsListAdapter(it) }
            holdPositionsAdapter?.listener = this
        }
        return holdPositionsAdapter!!
    }

    private fun getMockStockOrderAdapter(): SimulationTradingOrderAdapter {
        if (todayOrderAdapter == null) {
            todayOrderAdapter = context?.let { SimulationTradingOrderAdapter(it) }
            todayOrderAdapter?.listener = this
        }
        return todayOrderAdapter!!
    }

    override fun showLoading() {
        if (loading == null) {
            loading = context?.let { ProgressDialog(it) }
        }
        if (!loading!!.isShowing) {
            loading?.show()
        }
    }

    override fun hideLoading() {
        if (loading != null && loading!!.isShowing)
            loading?.dismiss()
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
                colorTransitionPagerTitleView.normalColor =
                    ResUtil.getColor(com.zhuorui.securities.market.R.color.color_232323)!!
                colorTransitionPagerTitleView.selectedColor =
                    ResUtil.getColor(com.zhuorui.securities.market.R.color.tab_select)!!
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
                indicator.setColors(ResUtil.getColor(com.zhuorui.securities.market.R.color.tab_select))
                indicator.lineHeight = ResUtil.getDimensionDp2Px(2f).toFloat()
                indicator.lineWidth = ResUtil.getDimensionDp2Px(33f).toFloat()
                return indicator
            }
        }
        return commonNavigator
    }


}