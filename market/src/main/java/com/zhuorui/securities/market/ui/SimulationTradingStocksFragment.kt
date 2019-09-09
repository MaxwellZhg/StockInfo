package com.zhuorui.securities.market.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.databinding.FragmentSimulationTradingStocksBinding
import com.zhuorui.securities.market.ui.presenter.SimulationTradingStocksPresenter
import com.zhuorui.securities.market.ui.view.SimulationTradingStocksView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingStocksViewModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/9 10:29
 *    desc   : 模拟炒股订单详情页面
 */
class SimulationTradingStocksFragment :
    AbsSwipeBackNetFragment<FragmentSimulationTradingStocksBinding, SimulationTradingStocksViewModel, SimulationTradingStocksView, SimulationTradingStocksPresenter>(),
    SimulationTradingStocksView {

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

}