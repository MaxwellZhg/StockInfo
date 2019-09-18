package com.zhuorui.securities.market.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.TitleMessageConfirmDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.custom.TradingStocksOrderDialog
import com.zhuorui.securities.market.databinding.FragmentSimulationTradingStocksBinding
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

    val SEARCH_STOCK_CODE = 1000

    companion object {
        fun newInstance(): SimulationTradingStocksFragment {
            return SimulationTradingStocksFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_simulation_trading_stocks

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: SimulationTradingStocksPresenter
        get() = SimulationTradingStocksPresenter()

    override val createViewModel: SimulationTradingStocksViewModel?
        get() = ViewModelProviders.of(this).get(SimulationTradingStocksViewModel::class.java)

    override val getView: SimulationTradingStocksView
        get() = this


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

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        if (resultCode == ISupportFragment.RESULT_OK && requestCode == SEARCH_STOCK_CODE) {
            // 设置搜索返回的自选股
            data?.getParcelable<SearchStockInfo>(SearchStockInfo::class.java.simpleName)
                ?.let { presenter?.setStock(it) }
        }
    }
}