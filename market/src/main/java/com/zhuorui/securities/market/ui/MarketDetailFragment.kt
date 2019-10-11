package com.zhuorui.securities.market.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailBinding
import com.zhuorui.securities.market.ui.presenter.MarketDetailPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailViewModel


/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-11 15:46
 *    desc   : 股票行情页面
 */
class MarketDetailFragment :
    AbsSwipeBackNetFragment<FragmentMarketDetailBinding, MarketDetailViewModel, MarketDetailView, MarketDetailPresenter>(),
    MarketDetailView {

    override val layout: Int
        get() = R.layout.fragment_market_detail
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketDetailPresenter
        get() = MarketDetailPresenter()
    override val createViewModel: MarketDetailViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailViewModel::class.java)
    override val getView: MarketDetailView
        get() = this
}