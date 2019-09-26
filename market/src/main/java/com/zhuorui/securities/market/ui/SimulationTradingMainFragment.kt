package com.zhuorui.securities.market.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuorui.commonwidget.dialog.ConfirmToCancelDialog
import com.zhuorui.commonwidget.dialog.GetPicturesModeDialog
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.commonwidget.dialog.TitleMessageConfirmDialog
import com.zhuorui.securities.alioss.service.OssService
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.GetPhotoFromAlbumUtil
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
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
    override fun toBusiness(data: STOrderData) {
        start(SimulationTradingStocksFragment.newInstance(data))
    }

    /**
     * 改单
     */
    override fun toChangeOrder(data: STOrderData) {
        start(SimulationTradingStocksFragment.newInstance(data))
    }

    /**
     * 撤单
     */
    override fun toCancelOrder(data: STOrderData) {
        ConfirmToCancelDialog.createWidth265Dialog(context!!, false, true)
            .setTitleText(ResUtil.getString(R.string.str_tips)!!)
            .setMsgText(R.string.str_confirm_withdrawal)
            .setConfirmText(ResUtil.getString(R.string.str_confirm)!!)
            .setCancelText(ResUtil.getString(R.string.str_cancel)!!)
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
            .show()
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

    override fun cancelTrustSuccess() {
        presenter?.getFundAccount()
    }

    override fun cancelTrustError(msg: String?) {
        ToastUtil.instance.toast(msg.toString())
    }

    override fun createFundAccountSuccess() {
        fund_account?.createFundAccountSuccess()
        TitleMessageConfirmDialog.createWidth225Dialog(context!!, false, true)
            .setTitleText("")
            .setMsgText(R.string.create_fund_account_success)
            .setConfirmText(R.string.str_understood)
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
        ConfirmToCancelDialog.createWidth265Dialog(context!!, false, true)
            .setTitleText(ResUtil.getString(R.string.str_tips)!!)
            .setMsgText(msg.toString())
            .setConfirmText(ResUtil.getString(R.string.str_retry)!!)
            .setCancelText(ResUtil.getString(R.string.str_back)!!)
            .setCallBack(object : ConfirmToCancelDialog.CallBack {
                override fun onCancel() {
                    pop()
                }

                override fun onConfirm() {
                    presenter?.getFundAccount()
                }
            })
            .show()
    }

    override fun onCreateFundAccountError(code: String, message: String?) {
        ConfirmToCancelDialog.createWidth265Dialog(context!!, false, true)
            .setTitleText(ResUtil.getString(R.string.str_tips)!!)
            .setMsgText(message.toString())
            .setConfirmText(ResUtil.getString(R.string.str_retry)!!)
            .setCancelText(ResUtil.getString(R.string.str_back)!!)
            .setCallBack(object : ConfirmToCancelDialog.CallBack {
                override fun onCancel() {
                }

                override fun onConfirm() {
                    presenter?.createFundAccount()
                }
            })
            .show()
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
            ResUtil.getString(R.string.str_hold_positions) + "($hpNum)",
            ResUtil.getString(R.string.str_today_orders) + "($toNum)"
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
        loading?.show()
    }

    override fun hideLoading() {
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