package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailBinding
import com.zhuorui.securities.market.ui.presenter.MarketDetailCapitalPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailCapitalView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailCapitalViewModel
import kotlinx.android.synthetic.main.fragment_market_detail_capital.*

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

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        val outData = mutableListOf<Float>()
        outData.add(4322.9f)
        outData.add(7999.99f)
        outData.add(10307.56f)
        val inData = mutableListOf<Float>()
        inData.add(3400.60f)
        inData.add(9999.99f)
        inData.add(7028.76f)
        todayFundTransaction.setData(outData,inData)
    }
}