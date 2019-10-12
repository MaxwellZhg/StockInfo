package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.StatusBarUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailBinding
import com.zhuorui.securities.market.ui.presenter.MarketDetailPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailViewModel
import kotlinx.android.synthetic.main.fragment_market_detail.*
import kotlinx.android.synthetic.main.layout_market_detail_topbar.*


/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-11 15:46
 *    desc   : 股票行情页面
 */
class MarketDetailFragment :
    AbsSwipeBackNetFragment<FragmentMarketDetailBinding, MarketDetailViewModel, MarketDetailView, MarketDetailPresenter>(),
    MarketDetailView, View.OnClickListener {

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

    companion object {
        fun newInstance(): MarketDetailFragment {
            return MarketDetailFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initView()
    }

    override fun onClick(v: View?) {
        when(v){
            iv_back->{
                pop()
            }
            iv_search->{
                start(SearchInfoFragment.newInstance())
            }
        }

    }

    private fun initView() {
        top_bar.setPadding(0, StatusBarUtil.getStatusBarHeight(context),0,0)
        iv_back.setOnClickListener(this)
        iv_search.setOnClickListener(this)

    }
}