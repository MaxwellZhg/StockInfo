package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailBinding
import com.zhuorui.securities.market.socket.vo.CapitalData
import com.zhuorui.securities.market.ui.presenter.MarketDetailCapitalPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailCapitalView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailCapitalViewModel
import kotlinx.android.synthetic.main.fragment_market_detail_capital.*

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:51
 *    desc   : 个股详情资金页面
 */
class MarketDetailCapitalFragment :
    AbsFragment<FragmentMarketDetailBinding, MarketDetailCapitalViewModel, MarketDetailCapitalView, MarketDetailCapitalPresenter>(),
    MarketDetailCapitalView {

    private var mTs: String = ""
    private var mCode: String = ""

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
        fun newInstance(ts: String, code: String): MarketDetailCapitalFragment {
            val b = Bundle()
            b.putString("ts", ts)
            b.putString("code", code)
            val fragment = MarketDetailCapitalFragment()
            fragment.arguments = b
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTs = arguments?.getString("ts") ?: mTs
        mCode = arguments?.getString("code") ?: mCode

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        todatCapitalFlowTrend.setData("HK", mutableListOf<Float>())
        getData()
    }

    override fun onTodayFundTransactionData(data: CapitalData?) {
        todayFundTransaction.setData(data)
    }

    fun getData() {
        presenter?.getData(mTs, mCode)
    }
}