package com.zhuorui.securities.market.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailBinding
import com.zhuorui.securities.market.ui.presenter.MarketDetailCapitalPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailCapitalView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailCapitalViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:51
 *    desc   :
 */
class MarketDetailCapitalFragment :
    AbsSwipeBackNetFragment<FragmentMarketDetailBinding, MarketDetailCapitalViewModel, MarketDetailCapitalView, MarketDetailCapitalPresenter>(),
    MarketDetailCapitalView {

    override val layout: Int
        get() = R.layout.fragment_market_detail_capital
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketDetailCapitalPresenter
        get() = MarketDetailCapitalPresenter()
    override val createViewModel: MarketDetailCapitalViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailCapitalViewModel::class.java)
    override val getView: MarketDetailCapitalView
        get() = this

    companion object {
        fun newInstance(): MarketDetailCapitalFragment {
            return MarketDetailCapitalFragment()
        }
    }
}