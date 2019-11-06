package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.ui.presenter.MarketDetailF10FinancialPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailF10FinancialView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailF10FinancialViewModel
import kotlinx.android.synthetic.main.fragment_market_detail_capital.*
import kotlinx.android.synthetic.main.fragment_market_detail_f10_financial.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/6 13:35
 *    desc   : F10财报页面
 */
class MarketDetailF10FinancialFragment : AbsSwipeBackNetFragment<com.zhuorui.securities.market.databinding.FragmentMarketDetailF10FinancialBinding,MarketDetailF10FinancialViewModel,MarketDetailF10FinancialView,MarketDetailF10FinancialPresenter>(),MarketDetailF10FinancialView {
    override val layout: Int
        get() = R.layout.fragment_market_detail_f10_financial
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketDetailF10FinancialPresenter
        get() = MarketDetailF10FinancialPresenter()
    override val createViewModel: MarketDetailF10FinancialViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailF10FinancialViewModel::class.java)
    override val getView: MarketDetailF10FinancialView
        get() = this

    companion object {
        fun newInstance(): MarketDetailF10FinancialFragment {
            return MarketDetailF10FinancialFragment()

        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        val outData = mutableListOf<Float>()
        outData.add(4158.15f)
        outData.add(1249.04f)
        outData.add(1149.74f)
        outData.add(345.53f)
        financial_view.setData(outData)
    }
}