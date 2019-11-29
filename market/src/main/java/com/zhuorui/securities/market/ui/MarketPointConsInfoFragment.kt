package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketPointConsInfoBinding
import com.zhuorui.securities.market.net.response.MarketNewsListResponse
import com.zhuorui.securities.market.ui.adapter.MarketPointInfoAdapter
import com.zhuorui.securities.market.ui.presenter.MarketPointConsInfoPresenter
import com.zhuorui.securities.market.ui.view.MarketPointConsInfoView
import com.zhuorui.securities.market.ui.viewmodel.MarketPointConsInfoViewModel
import kotlinx.android.synthetic.main.fragment_market_point_cons_info.*
import kotlinx.android.synthetic.main.fragment_market_point_cons_info.srl_layout

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:
 */
class MarketPointConsInfoFragment :AbsSwipeBackNetFragment<FragmentMarketPointConsInfoBinding,MarketPointConsInfoViewModel, MarketPointConsInfoView,MarketPointConsInfoPresenter>(),
    MarketPointConsInfoView, OnRefreshLoadMoreListener {
    private var currentPage: Int = 1
    private var stockCode: String? = null
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
        stockCode = arguments?.getString("code")
        presenter?.setLifecycleOwner(this)
        srl_layout.setEnableLoadMore(true)
        srl_layout.setOnRefreshLoadMoreListener(this)
        consInfoadapter = presenter?.getMarketPointInfoAdapter()
        stockCode?.let { presenter?.getNewsListData(it, currentPage) }
        rv_cons_stock_info.adapter = consInfoadapter
        //解决数据加载不完的问题
 /*       rv_cons_stock_info.isFocusable = false
        rv_cons_stock_info.isNestedScrollingEnabled=false
        rv_cons_stock_info.setHasFixedSize(true)*/
        consInfoadapter?.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(code:String): MarketPointConsInfoFragment {
            val fragment = MarketPointConsInfoFragment()
            if (code != null) {
                val bundle = Bundle()
                bundle.putString("code", code)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        currentPage++
        stockCode?.let { presenter?.getNewsListData(it, currentPage) }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

    }

    override fun addIntoInfoData(list: List<MarketNewsListResponse.DataList>) {
        if (currentPage == 1) {
            empty_info_view.visibility = View.INVISIBLE
            consInfoadapter?.clearItems()
        }
        if (consInfoadapter?.items == null) {
            consInfoadapter?.items = ArrayList()
        }
        srl_layout.finishLoadMore(true)
        consInfoadapter?.addItems(list)
    }

    override fun noMoreData() {
        when (currentPage) {
            1 -> {
                empty_info_view.visibility = View.VISIBLE
            }
            else -> {
                srl_layout.finishLoadMore(true)//结束加载（加载失败）
                srl_layout.finishLoadMoreWithNoMoreData()
                srl_layout.setNoMoreData(true)
            }
        }

    }

    override fun loadFailData() {
        srl_layout.finishLoadMore(false)
    }


}