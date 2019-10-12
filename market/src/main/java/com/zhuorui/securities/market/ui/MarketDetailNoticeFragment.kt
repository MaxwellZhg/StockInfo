package com.zhuorui.securities.market.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailBinding
import com.zhuorui.securities.market.databinding.FragmentMarketDetailNoticeBinding
import com.zhuorui.securities.market.ui.presenter.MarketDetailCapitalPresenter
import com.zhuorui.securities.market.ui.presenter.MarketDetailNoticePresenter
import com.zhuorui.securities.market.ui.view.MarketDetailCapitalView
import com.zhuorui.securities.market.ui.view.MarketDetailNoticeView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailCapitalViewModel
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailNoticeViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:51
 *    desc   :
 */
class MarketDetailNoticeFragment :
    AbsSwipeBackNetFragment<FragmentMarketDetailNoticeBinding, MarketDetailNoticeViewModel, MarketDetailNoticeView, MarketDetailNoticePresenter>(),
    MarketDetailNoticeView {

    override val layout: Int
        get() = R.layout.fragment_market_detail_notice
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketDetailNoticePresenter
        get() = MarketDetailNoticePresenter()
    override val createViewModel: MarketDetailNoticeViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailNoticeViewModel::class.java)
    override val getView: MarketDetailNoticeView
        get() = this

    companion object {
        fun newInstance(): MarketDetailNoticeFragment {
            return MarketDetailNoticeFragment()
        }
    }
}