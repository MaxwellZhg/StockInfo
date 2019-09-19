package com.zhuorui.securities.market.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.TitleMessageConfirmDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.custom.TradingStocksOrderDialog
import com.zhuorui.securities.market.databinding.FragmentSimulationTradingStocksBinding
import com.zhuorui.securities.market.model.PushStockTransData
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

    companion object {
        fun newInstance(): SimulationTradingStocksFragment {
            return SimulationTradingStocksFragment()
        }
    }

    private val mPresenter = SimulationTradingStocksPresenter()

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
        btn_buy.setOnClickListener(this)
        btn_sell.setOnClickListener(this)
        tv_code.setOnClickListener(this)
        top_bar.setRightClickListener {
            // 消息
            start(MessageFragment.newInstance())
        }
        top_bar.setRight2ClickListener {
            // 搜索自选股
            start(StockSearchFragment.newInstance())
        }
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
            btn_buy -> {
                // 买入
                TitleMessageConfirmDialog.createWidth225Dialog(context!!, false, true).setTitleText("提示")
                    .setMsgText("下单成功").setConfirmText("查看详情").show()
            }
            btn_sell -> {
                // 卖出
                TradingStocksOrderDialog.createDialog(context!!, false, true).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun updateStockPrice(price: BigDecimal, diffPrice: BigDecimal, diffRate: BigDecimal) {
        tv_stock_price.text = price.toString()
        val diffValue = MathUtil.rounded(diffPrice).toInt()
        if (diffValue == 0 || diffValue > 0) {
            ResUtil.getColor(R.color.color_ffce0019)?.let {
                tv_stock_price.setTextColor(it)
                tv_diff_pirce.setTextColor(it)
                tv_diff_rate.setTextColor(it)
            }

            tv_stock_price.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.ic_stock_up_arrow, 0)
            tv_diff_pirce.text = "+$diffPrice"
            tv_diff_rate.text = "+$diffRate%"
        } else {
            ResUtil.getColor(R.color.color_FF23803A)?.let {
                tv_stock_price.setTextColor(it)
                tv_diff_pirce.setTextColor(it)
                tv_diff_rate.setTextColor(it)
            }

            tv_stock_price.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.ic_stock_down_arrow, 0)
            tv_diff_pirce.text = diffPrice.toString()
            tv_diff_rate.text = "$diffRate%"
        }
    }

    override fun updateStockTrans(transData: PushStockTransData, buyRate: Double, sellRate: Double) {
        // 更新买入卖出交易量
        trans_buy1.setPriceText(transData.bid1.toString())
        trans_buy1.setVolumeText(transData.bid1Volume.toString())
        trans_buy2.setPriceText(transData.bid2.toString())
        trans_buy2.setVolumeText(transData.bid2Volume.toString())
        trans_buy3.setPriceText(transData.bid3.toString())
        trans_buy3.setVolumeText(transData.bid3Volume.toString())
        trans_buy4.setPriceText(transData.bid4.toString())
        trans_buy4.setVolumeText(transData.bid4Volume.toString())
        trans_buy5.setPriceText(transData.bid5.toString())
        trans_buy5.setVolumeText(transData.bid5Volume.toString())
        trans_sell1.setPriceText(transData.ask1.toString())
        trans_sell1.setVolumeText(transData.ask1Volume.toString())
        trans_sell2.setPriceText(transData.ask2.toString())
        trans_sell2.setVolumeText(transData.ask2Volume.toString())
        trans_sell3.setPriceText(transData.ask3.toString())
        trans_sell3.setVolumeText(transData.ask3Volume.toString())
        trans_sell4.setPriceText(transData.ask4.toString())
        trans_sell4.setVolumeText(transData.ask4Volume.toString())
        trans_sell5.setPriceText(transData.ask5.toString())
        trans_sell5.setVolumeText(transData.ask5Volume.toString())
        // 更新比例
        (trans_buy_rate.layoutParams as ConstraintLayout.LayoutParams).horizontalWeight = buyRate.toFloat()
        (trans_sell_rate.layoutParams as ConstraintLayout.LayoutParams).horizontalWeight = sellRate.toFloat()
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