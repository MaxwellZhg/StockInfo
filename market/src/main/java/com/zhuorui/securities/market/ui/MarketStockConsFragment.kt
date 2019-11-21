package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.ui.presenter.MarketStockConsPresenter
import com.zhuorui.securities.market.ui.view.MarketStockConsView
import com.zhuorui.securities.market.ui.viewmodel.MarketStockConsViewModel
import com.zhuorui.securities.market.databinding.FragmentMarketStockConsBinding
import com.zhuorui.securities.market.net.response.StockConsInfoResponse
import com.zhuorui.securities.market.ui.adapter.MarketPointConsInfoAdapter
import kotlinx.android.synthetic.main.fragment_market_detail_information.*
import kotlinx.android.synthetic.main.fragment_market_stock_cons.*
import kotlinx.android.synthetic.main.fragment_market_stock_cons.srl_layout

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:
 */
class MarketStockConsFragment :AbsSwipeBackNetFragment<FragmentMarketStockConsBinding,MarketStockConsViewModel,MarketStockConsView,MarketStockConsPresenter>(),
    MarketStockConsView,MarketPointConsInfoAdapter.OnCombineInfoClickListener, OnRefreshLoadMoreListener {
    private var infoadapter: MarketPointConsInfoAdapter? = null
    override val layout: Int
        get() = R.layout.fragment_market_stock_cons
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketStockConsPresenter
        get() = MarketStockConsPresenter()
    override val createViewModel: MarketStockConsViewModel?
        get() = ViewModelProviders.of(this).get(MarketStockConsViewModel::class.java)
    override val getView: MarketStockConsView
        get() = this

    companion object {
        fun newInstance(): MarketStockConsFragment {
            return MarketStockConsFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        presenter?.getStockConsInfo()
        srl_layout.setEnableLoadMore(true)
        srl_layout.setOnRefreshLoadMoreListener(this)
        presenter?.setLifecycleOwner(this)
        infoadapter = presenter?.getMarketInfoAdapter()
        infoadapter?.onCombineInfoClickListener=this
        rv_point_stock.adapter = infoadapter
        //解决数据加载不完的问题
        rv_point_stock.isFocusable = false
        infoadapter?.notifyDataSetChanged()
    }

    override fun addInfoToAdapter(list: List<StockConsInfoResponse.ListInfo>) {
        infoadapter?.clearItems()
        if (infoadapter?.items == null) {
            infoadapter?.items = ArrayList()
        }
        infoadapter?.addItems(list)
        srl_layout.finishLoadMore(true)
    }
    override fun onCombineClick() {
        ToastUtil.instance.toastCenter("成分股")
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

    }


}