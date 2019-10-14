package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentSearchResultInfoBinding
import com.zhuorui.securities.market.model.SearchDeafaultData
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.SearchStokcInfoEnum
import com.zhuorui.securities.market.ui.adapter.SeachAllofInfoAdapter
import com.zhuorui.securities.market.ui.adapter.StockAdapter
import com.zhuorui.securities.market.ui.adapter.StockInfoAdapter
import com.zhuorui.securities.market.ui.presenter.SearchResultInfoPresenter
import com.zhuorui.securities.market.ui.view.SearchResultInfoView
import com.zhuorui.securities.market.ui.viewmodel.SearchResultInfoViewModel
import kotlinx.android.synthetic.main.fragment_search_result_info.*
import kotlinx.android.synthetic.main.fragment_simulation_trading_orders.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/20
 * Desc:
 */
class SearchResultInfoFragment :
    AbsFragment<FragmentSearchResultInfoBinding, SearchResultInfoViewModel, SearchResultInfoView, SearchResultInfoPresenter>(),
    SearchResultInfoView, SeachAllofInfoAdapter.OnTopicStockInfoListenner,StockAdapter.OnStockColollectListenner,
    OnRefreshLoadMoreListener {

    var currentPage :Int = 0
    var totalPage:Int =0
    var countNum:Int =0
    private var strInfo:String?=null
    private var adapter: SeachAllofInfoAdapter? = null
    private var stockadapter: StockAdapter? = null
    private var infoadapter: StockInfoAdapter? = null
    private var type: SearchStokcInfoEnum? = null
    override val layout: Int
        get() = R.layout.fragment_search_result_info
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: SearchResultInfoPresenter
        get() = SearchResultInfoPresenter()
    override val createViewModel: SearchResultInfoViewModel?
        get() = ViewModelProviders.of(this).get(SearchResultInfoViewModel::class.java)
    override val getView: SearchResultInfoView
        get() = this
    companion object {
        fun newInstance(type: SearchStokcInfoEnum?): SearchResultInfoFragment {
            val fragment = SearchResultInfoFragment()
            if (type != null) {
                val bundle = Bundle()
                bundle.putSerializable("type", type)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun init() {
        type = arguments?.getSerializable("type") as SearchStokcInfoEnum?
        presenter?.setType(type)
        presenter?.setLifecycleOwner(this)
        if (presenter?.ts != null) {
            initRv(presenter?.ts)
        } else {
            initRv(SearchStokcInfoEnum.All)
        }
    }

    override fun detailInfo(str: String) {
        strInfo=str
        currentPage=0
        totalPage=0
        countNum=0
        rv_serach_all.adapter = adapter
        adapter?.setkeywords(str)
        presenter?.getData(type, str)
        adapter?.notifyDataSetChanged()
    }

    override fun detailStock(str: String) {
        strInfo=str
        currentPage=0
        //totalPage=0
        countNum=0
        sm_refrsh.setNoMoreData(false)
        presenter?.getStockData(str,currentPage)
        stockadapter?.setkeywords(str)
        rv_serach_all.adapter = stockadapter
        stockadapter?.notifyDataSetChanged()
    }

    override fun detailStockInfo(str: String) {
        strInfo=str
        currentPage=0
        countNum=0
       // totalPage=0
        sm_refrsh.setNoMoreData(false)
        presenter?.getStockInfoData()
        infoadapter?.setkeywords(str)
        rv_serach_all.adapter = infoadapter
        infoadapter?.notifyDataSetChanged()
    }

    fun initRv(enum: SearchStokcInfoEnum?) {
        when (enum) {
            SearchStokcInfoEnum.All -> {
                adapter = presenter?.getAdapter()
                adapter?.onTopicStockInfoListenner = this
                sm_refrsh.setEnableRefresh(false)
                sm_refrsh.setEnableLoadMore(false)
            }
            SearchStokcInfoEnum.Stock -> {
                stockadapter = presenter?.getStockAdapter()
                stockadapter?.onStockColollectListenner=this
                sm_refrsh.setEnableRefresh(false)
                sm_refrsh.setEnableLoadMore(true)
                sm_refrsh.setOnRefreshLoadMoreListener(this)
            }
            SearchStokcInfoEnum.Info -> {
                infoadapter = presenter?.getStockInfoAdapter()
                sm_refrsh.setEnableRefresh(false)
                sm_refrsh.setEnableLoadMore(true)
                sm_refrsh.setOnRefreshLoadMoreListener(this)
            }
        }

    }

    override fun initonlazy() {
        init()
    }

    override fun addInfoToAdapter(list: List<Int>?,totalPage: Int) {
        if (presenter?.ts == SearchStokcInfoEnum.Info) {
            infoadapter?.clearItems()
            if (infoadapter?.items == null) {
                infoadapter?.items = ArrayList()
            }
            infoadapter?.addItems(list)
        }
    }

    override fun addStockToAdapter(list: List<SearchStockInfo>?,totalPage: Int) {
        if (presenter?.ts == SearchStokcInfoEnum.Stock) {
            this.totalPage=totalPage
            if(currentPage==0) {
                 stockadapter?.clearItems()
            }
           if (stockadapter?.items == null) {
                stockadapter?.items = ArrayList()
            }
            if(!list?.let { stockadapter?.items?.containsAll(it) }!!) {
                sm_refrsh.finishLoadMore(true)
                stockadapter?.addItems(list)
            }
        }

    }

    override fun addAllToAdapter(list: List<SearchDeafaultData>?) {
        if (presenter?.ts == SearchStokcInfoEnum.All) {
            adapter?.clearItems()
            if (adapter?.items == null) {
                adapter?.items = ArrayList()
            }
            adapter?.addItems(list)
        }
    }

    override fun onClickCollectionStock(stockInfo: SearchStockInfo) {
        presenter?.collectionStock(stockInfo, stockInfo.collect)
    }
    override fun onStockCollectionStock(stockInfo: SearchStockInfo) {
        presenter?.collectionStock(stockInfo, stockInfo.collect)
    }
    override fun notifyAdapter() {
        when(presenter?.ts){
            SearchStokcInfoEnum.All -> {
               adapter?.notifyDataSetChanged()
            }
            SearchStokcInfoEnum.Stock -> {
                stockadapter?.notifyDataSetChanged()
            }
        }
    }
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        when(presenter?.ts){
            SearchStokcInfoEnum.Stock->{
                currentPage++
                strInfo?.let { presenter?.getStockData(it,currentPage) }
            }
        }

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

    }

    override fun showEmpty() {
        sm_refrsh.finishLoadMore(false)
        //empty_view.visibility=View.VISIBLE
    }
    override fun hideEmpty() {
        empty_view.visibility=View.INVISIBLE
    }
    override fun showloadMoreFail() {
        sm_refrsh.finishLoadMore(true)//结束加载（加载失败）
       sm_refrsh.finishLoadMoreWithNoMoreData()
        sm_refrsh.setNoMoreData(true)
    }
}

