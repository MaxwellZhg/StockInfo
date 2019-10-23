package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.ui.presenter.MarketPointPresenter
import com.zhuorui.securities.market.ui.view.MarketPointView
import com.zhuorui.securities.market.ui.viewmodel.MarketPointViewModel
import com.zhuorui.securities.market.databinding.FragmentMarketPointBinding
import com.zhuorui.securities.market.generated.callback.OnClickListener
import kotlinx.android.synthetic.main.layout_market_point_topbar.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/22
 * Desc:
 */
class MarketPointFragment :AbsSwipeBackNetFragment<FragmentMarketPointBinding,MarketPointViewModel,MarketPointView,MarketPointPresenter>(),MarketPointView,View.OnClickListener{
    override val layout: Int
        get() = R.layout.fragment_market_point
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketPointPresenter
        get() = MarketPointPresenter()
    override val createViewModel: MarketPointViewModel?
        get() = ViewModelProviders.of(this).get(MarketPointViewModel::class.java)
    override val getView: MarketPointView
        get() = this
    companion object {
        fun newInstance(): MarketPointFragment {
            return MarketPointFragment()
        }
    }

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        iv_back.setOnClickListener(this)
        iv_search.setOnClickListener(this)
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

}