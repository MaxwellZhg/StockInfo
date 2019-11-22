package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketPointConsInfoBinding
import com.zhuorui.securities.market.ui.adapter.MarketPointInfoAdapter
import com.zhuorui.securities.market.ui.presenter.MarketPointConsInfoPresenter
import com.zhuorui.securities.market.ui.view.MarketPointConsInfoView
import com.zhuorui.securities.market.ui.viewmodel.MarketPointConsInfoViewModel
import kotlinx.android.synthetic.main.fragment_market_point_cons_info.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:
 */
class MarketPointConsInfoFragment :AbsSwipeBackNetFragment<FragmentMarketPointConsInfoBinding,MarketPointConsInfoViewModel, MarketPointConsInfoView,MarketPointConsInfoPresenter>(),
    MarketPointConsInfoView, OnRefreshLoadMoreListener {
    private var consInfoadapter: MarketPointInfoAdapter? = null
    override val layout: Int
        get() = R.layout.fragment_market_point_cons_info
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketPointConsInfoPresenter
        get() = MarketPointConsInfoPresenter()
    override val createViewModel: MarketPointConsInfoViewModel?
        get() = ViewModelProviders.of(this).get(MarketPointConsInfoViewModel::class.java)
    override val getView: MarketPointConsInfoView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        presenter?.setLifecycleOwner(this)
        consInfoadapter = presenter?.getMarketPointInfoAdapter()
        presenter?.getInfoData()
        srl_layout.setEnableLoadMore(true)
        srl_layout.setOnRefreshLoadMoreListener(this)
        rv_cons_stock_info.adapter = consInfoadapter
        //解决数据加载不完的问题
        rv_cons_stock_info.isFocusable = false
        consInfoadapter?.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): MarketPointConsInfoFragment {
            return MarketPointConsInfoFragment()
        }
    }
    override fun addPointInfoAdapter(list: List<Int>) {
        consInfoadapter?.clearItems()
        if (consInfoadapter?.items == null) {
            consInfoadapter?.items = ArrayList()
        }
        consInfoadapter?.addItems(list)
        srl_layout.finishLoadMore(true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

    }


}