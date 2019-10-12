package com.zhuorui.securities.market.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailBinding
import com.zhuorui.securities.market.databinding.FragmentMarketDetailF10Binding
import com.zhuorui.securities.market.databinding.FragmentMarketDetailNoticeBinding
import com.zhuorui.securities.market.ui.presenter.MarketDetailCapitalPresenter
import com.zhuorui.securities.market.ui.presenter.MarketDetailF10Presenter
import com.zhuorui.securities.market.ui.presenter.MarketDetailNoticePresenter
import com.zhuorui.securities.market.ui.view.MarketDetailCapitalView
import com.zhuorui.securities.market.ui.view.MarketDetailF10View
import com.zhuorui.securities.market.ui.view.MarketDetailNoticeView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailCapitalViewModel
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailF10ViewModel
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailNoticeViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:51
 *    desc   :
 */
class MarketDetailF10Fragment :
    AbsSwipeBackNetFragment<FragmentMarketDetailF10Binding, MarketDetailF10ViewModel, MarketDetailF10View, MarketDetailF10Presenter>(),
    MarketDetailF10View {

    override val layout: Int
        get() = R.layout.fragment_market_detail_f10
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketDetailF10Presenter
        get() = MarketDetailF10Presenter()
    override val createViewModel: MarketDetailF10ViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailF10ViewModel::class.java)
    override val getView: MarketDetailF10View
        get() = this

    companion object {
        fun newInstance(): MarketDetailF10Fragment {
            return MarketDetailF10Fragment()
        }
    }
}