package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.TitleMessageConfirmDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.custom.TradingStocksOrderDialog
import com.zhuorui.securities.market.databinding.FragmentSimulationTradingStocksBinding
import com.zhuorui.securities.market.ui.presenter.SimulationTradingStocksPresenter
import com.zhuorui.securities.market.ui.view.SimulationTradingStocksView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingStocksViewModel
import kotlinx.android.synthetic.main.fragment_simulation_trading_stocks.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/9 10:29
 *    desc   : 模拟炒股订单详情页面
 */
class SimulationTradingStocksFragment :
    AbsSwipeBackNetFragment<FragmentSimulationTradingStocksBinding, SimulationTradingStocksViewModel, SimulationTradingStocksView, SimulationTradingStocksPresenter>(),
    SimulationTradingStocksView, View.OnClickListener {

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
    }

    override fun onClick(p0: View?) {
        when (p0) {
            iv_chart -> {
                // 查看k线
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
}