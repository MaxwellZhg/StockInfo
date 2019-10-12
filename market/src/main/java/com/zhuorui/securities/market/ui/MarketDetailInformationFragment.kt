package com.zhuorui.securities.market.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailBinding
import com.zhuorui.securities.market.ui.presenter.MarketDetailCapitalPresenter
import com.zhuorui.securities.market.ui.presenter.MarketDetailInformationPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailCapitalView
import com.zhuorui.securities.market.ui.view.MarketDetailInformationView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailCapitalViewModel
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailInformationViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:51
 *    desc   :
 */
class MarketDetailInformationFragment :
    AbsSwipeBackNetFragment<FragmentMarketDetailBinding, MarketDetailInformationViewModel, MarketDetailInformationView, MarketDetailInformationPresenter>(),
    MarketDetailInformationView {

    override val layout: Int
        get() = R.layout.fragment_market_detail_information
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketDetailInformationPresenter
        get() = MarketDetailInformationPresenter()
    override val createViewModel: MarketDetailInformationViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailInformationViewModel::class.java)
    override val getView: MarketDetailInformationView
        get() = this

    companion object {
        fun newInstance(): MarketDetailInformationFragment {
            return MarketDetailInformationFragment()
        }
    }
}