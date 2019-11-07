package com.zhuorui.securities.market.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailF10BriefBinding
import com.zhuorui.securities.market.ui.presenter.MarketDetailF10BriefPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailF10BriefView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailF10BriefViewModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/6 13:36
 *    desc   : F10简况页面
 */
class MarketDetailF10BriefFragment :
    AbsFragment<FragmentMarketDetailF10BriefBinding, MarketDetailF10BriefViewModel, MarketDetailF10BriefView, MarketDetailF10BriefPresenter>(),
    MarketDetailF10BriefView {

    companion object {
        fun newInstance(): MarketDetailF10BriefFragment {
            return MarketDetailF10BriefFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_market_detail_f10_brief

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: MarketDetailF10BriefPresenter
        get() = MarketDetailF10BriefPresenter()

    override val createViewModel: MarketDetailF10BriefViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailF10BriefViewModel::class.java)

    override val getView: MarketDetailF10BriefView
        get() = this
}