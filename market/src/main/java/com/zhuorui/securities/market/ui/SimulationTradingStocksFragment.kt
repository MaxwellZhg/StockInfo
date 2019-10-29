package com.zhuorui.securities.market.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.SimpleTextWatcher
import com.zhuorui.commonwidget.config.LocalSettingsConfig
import com.zhuorui.commonwidget.config.StocksThemeColor
import com.zhuorui.commonwidget.dialog.TitleMessageConfirmDialog
import com.zhuorui.securities.base2app.dialog.BaseDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.TradingStocksOrderDialog
import com.zhuorui.securities.market.databinding.FragmentSimulationTradingStocksBinding
import com.zhuorui.securities.market.model.PushStockTransData
import com.zhuorui.securities.market.model.STOrderData
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.ui.presenter.SimulationTradingStocksPresenter
import com.zhuorui.securities.market.ui.view.SimulationTradingStocksView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingStocksViewModel
import com.zhuorui.securities.market.util.MathUtil
import com.zhuorui.securities.personal.ui.MessageFragment
import kotlinx.android.synthetic.main.fragment_simulation_trading_stocks.*
import me.yokeyword.fragmentation.ISupportFragment
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/9 10:29
 *    desc   : 模拟炒股订单详情页面
 */
class SimulationTradingStocksFragment :
    AbsSwipeBackNetFragment<FragmentSimulationTradingStocksBinding, SimulationTradingStocksViewModel, SimulationTradingStocksView, SimulationTradingStocksPresenter>(),
    SimulationTradingStocksView, View.OnClickListener {

    private val SEARCH_STOCK_CODE = 1000
    private var upArrowResId = 0
    private var downArrowResId = 0

    companion object {
        const val TRAD_TYPE_KEY = "trad_type"
        const val TRAD_TYPE_DEFAULT = 1 // 已持仓买卖
        const val TRAD_TYPE_UPDATE_ORDER = 2 // 改单

        fun newInstance(): SimulationTradingStocksFragment {
            return SimulationTradingStocksFragment()
        }

        fun newInstance(tradType: Int, order: STOrderData): SimulationTradingStocksFragment {
            val fragment = SimulationTradingStocksFragment()
            val arguments = Bundle()
            arguments.putInt(TRAD_TYPE_KEY, tradType)
            arguments.putParcelable(STOrderData::class.java.simpleName, order)
            fragment.arguments = arguments
            return fragment
        }

        fun newInstance(stock: SearchStockInfo): SimulationTradingStocksFragment {
            val fragment = SimulationTradingStocksFragment()
            val arguments = Bundle()
            arguments.putParcelable(SearchStockInfo::class.java.simpleName, stock)
            fragment.arguments = arguments
            return fragment
        }
    }

    private val mPresenter = SimulationTradingStocksPresenter(this)

    override val layout: Int
        get() = R.layout.fragment_simulation_trading_stocks

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: SimulationTradingStocksPresenter
        get() = mPresenter

    override val createViewModel: SimulationTradingStocksViewModel?
        get() = ViewModelProviders.of(this).get(SimulationTradingStocksViewModel::class.java)

    override val getView: SimulationTradingStocksView
        get() = this

    override fun setAdditionalVariable(dataBinding: ViewDataBinding) {
        dataBinding.setVariable(BR.presenter, mPresenter)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        iv_chart.setOnClickListener(this)
        tv_code.setOnClickListener(this)
        top_bar.setRightClickListener {
            // 消息
            start(MessageFragment.newInstance())
        }
        top_bar.setRight2ClickListener {
            // 搜索自选股
            start(SearchInfoFragment.newInstance())
        }
        // 监听价格输入
        tv_buy_price.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                presenter?.onEditBuyPrice(s?.toString())
            }
        })
        // 监听数量输入
        tv_buy_count.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                presenter?.onEditBuyCount(s?.toString())
            }
        })
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        presenter?.setTradType()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            iv_chart -> {
                // 查看k线

            }
            tv_code -> {
                // 模拟炒股搜索
                startForResult(SimulationTradingSearchFragment.newInstance(), SEARCH_STOCK_CODE)
            }
        }
    }

    override fun changeTrustType(trustType: Int) {
        // 0默认购买 1买入改单 2卖出改单 3可买买卖
        tv_code.isClickable = false
        when (trustType) {
            0 -> {
                btn_buy.visibility = View.VISIBLE
                btn_sell.visibility = View.VISIBLE

                btn_cancel_change_order.visibility = View.GONE
                btn_confirm_change_order.visibility = View.GONE

                tv_code.isClickable = true
            }
            1 -> {
                btn_cancel_change_order.visibility = View.VISIBLE
                btn_confirm_change_order.visibility = View.VISIBLE

                btn_buy.visibility = View.INVISIBLE
                btn_sell.visibility = View.INVISIBLE
            }
            2 -> {
                btn_cancel_change_order.visibility = View.VISIBLE
                btn_sell.visibility = View.VISIBLE
                btn_sell.text = getString(R.string.str_confirm_change_order)

                btn_buy.visibility = View.INVISIBLE
                btn_confirm_change_order.visibility = View.GONE
            }
            3 -> {
                btn_buy.visibility = View.VISIBLE
                btn_sell.visibility = View.VISIBLE

                btn_cancel_change_order.visibility = View.GONE
                btn_confirm_change_order.visibility = View.GONE
            }
            4 -> {
                btn_buy.visibility = View.VISIBLE
                btn_sell.visibility = View.VISIBLE

                btn_cancel_change_order.visibility = View.GONE
                btn_confirm_change_order.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun updateStockPrice(price: BigDecimal, diffPrice: BigDecimal, diffRate: BigDecimal) {
        if (upArrowResId == 0 || downArrowResId == 0) {
            val stocksThemeColor = LocalSettingsConfig.read().stocksThemeColor
            if (stocksThemeColor == StocksThemeColor.redUpGreenDown) {
                upArrowResId = R.mipmap.ic_stock_up_arrow_red
                downArrowResId = R.mipmap.ic_stock_down_arrow_green
            } else {
                upArrowResId = R.mipmap.ic_stock_up_arrow_green
                downArrowResId = R.mipmap.ic_stock_down_arrow_red
            }
        }
        val diffValue = MathUtil.rounded(diffRate).toInt()
        val diffState: Int
        when {
            diffValue == 0 -> {
                diffState = 0
                tv_stock_price.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
            }
            diffValue > 0 -> {
                diffState = 1
                tv_stock_price.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, upArrowResId, 0)
            }
            else -> {
                diffState = 2
                tv_stock_price.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, downArrowResId, 0)
            }
        }
        tv_stock_price.setText(price.toString(), diffState)
        tv_diff_pirce.setText(diffPrice.toString(), diffState)
        tv_diff_rate.setText(diffRate.toString(), diffState)
    }

    override fun updateStockTrans(transData: PushStockTransData, buyRate: Double, sellRate: Double) {
        // 更新买入卖出交易量
        trans_buy1.setPriceText(MathUtil.rounded3(transData.bid1!!).toString())
        trans_buy1.setVolumeText(MathUtil.convertToUnitString(transData.bid1Volume!!))
        trans_buy2.setPriceText(MathUtil.rounded3(transData.bid2!!).toString())
        trans_buy2.setVolumeText(MathUtil.convertToUnitString(transData.bid2Volume!!))
        trans_buy3.setPriceText(MathUtil.rounded3(transData.bid3!!).toString())
        trans_buy3.setVolumeText(MathUtil.convertToUnitString(transData.bid3Volume!!))
        trans_buy4.setPriceText(MathUtil.rounded3(transData.bid4!!).toString())
        trans_buy4.setVolumeText(MathUtil.convertToUnitString(transData.bid4Volume!!))
        trans_buy5.setPriceText(MathUtil.rounded3(transData.bid5!!).toString())
        trans_buy5.setVolumeText(MathUtil.convertToUnitString(transData.bid5Volume!!))
        trans_sell1.setPriceText(MathUtil.rounded3(transData.ask1!!).toString())
        trans_sell1.setVolumeText(MathUtil.convertToUnitString(transData.ask1Volume!!))
        trans_sell2.setPriceText(MathUtil.rounded3(transData.ask2!!).toString())
        trans_sell2.setVolumeText(MathUtil.convertToUnitString(transData.ask2Volume!!))
        trans_sell3.setPriceText(MathUtil.rounded3(transData.ask3!!).toString())
        trans_sell3.setVolumeText(MathUtil.convertToUnitString(transData.ask3Volume!!))
        trans_sell4.setPriceText(MathUtil.rounded3(transData.ask4!!).toString())
        trans_sell4.setVolumeText(MathUtil.convertToUnitString(transData.ask4Volume!!))
        trans_sell5.setPriceText(MathUtil.rounded3(transData.ask5!!).toString())
        trans_sell5.setVolumeText(MathUtil.convertToUnitString(transData.ask5Volume!!))
        // 更新比例
        (trans_buy_rate.layoutParams as ConstraintLayout.LayoutParams).horizontalWeight = buyRate.toFloat()
        (trans_sell_rate.layoutParams as ConstraintLayout.LayoutParams).horizontalWeight = sellRate.toFloat()
    }

    @SuppressLint("SetTextI18n")
    override fun updateMaxBuyNum(maxBuyCount: Long?) {
        if (maxBuyCount == null) {
            tv_max_buy_num.text = getString(R.string.max_buy_num) + " --"
        } else {
            tv_max_buy_num.text =
                getSpannableString(R.string.max_buy_num, MathUtil.convertToString(maxBuyCount.toBigDecimal()))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun updateMaxBuySell(maxSellCount: Long?) {
        if (maxSellCount == null) {
            tv_max_sell_num.text = getString(R.string.max_sell_num) + " --"
        } else {
            tv_max_sell_num.text =
                getSpannableString(R.string.max_sell_num, MathUtil.convertToString(maxSellCount.toBigDecimal()))
        }
    }

    private fun getSpannableString(@StringRes strId: Int, formatStr: String): SpannableString {
        val spannableString =
            SpannableString(getString(strId) + " $formatStr")
        spannableString.setSpan(
            ForegroundColorSpan(ResUtil.getColor(R.color.color_ff1a6ed2)!!),
            spannableString.length - formatStr.length,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    override fun clearBuyCountFocus() {
        tv_buy_count.clearFocus()
        hideSoftInput()
    }

    override fun clearBuyPriceFocus() {
        tv_buy_price.clearFocus()
        hideSoftInput()
    }

    override fun showTradingStocksOrderDetail(
        accountId: String,
        chargeType: Int,
        stockName: String,
        tsCode: String,
        price: String,
        count: String,
        commission: Double,
        money: String
    ) {
        TradingStocksOrderDialog.createDialog(context!!, false, true)
            .setInfo(accountId, chargeType, stockName, tsCode, price, count, commission, money)
            .setCallBack(object : TradingStocksOrderDialog.CallBack {
                override fun onCancel() {

                }

                override fun onConfirm() {
                    presenter?.confirmTradingStocks(chargeType)
                }

            }).show()
    }

    override fun tradStocksSuccessful() =
        TitleMessageConfirmDialog.createWidth225Dialog(context!!, false, true).setTitleText(getString(R.string.tips))
            .setMsgText(getString(R.string.buy_stocks_successful)).setConfirmText(getString(R.string.see_details)).setLifeCycle(
                object : BaseDialog.DialogLifeCycle {
                    override fun onDialogDismiss(dialogName: String) {
                        pop()
                    }

                    override fun onDialogShow(dialogName: String) {

                    }
                }).show()

    override fun exit() {
        pop()
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        if (resultCode == ISupportFragment.RESULT_OK && requestCode == SEARCH_STOCK_CODE) {
            // 设置搜索返回的自选股
            data?.getParcelable<SearchStockInfo>(SearchStockInfo::class.java.simpleName)
                ?.let { presenter?.setStock(it) }
        }
    }
}
