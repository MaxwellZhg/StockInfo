package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentSearchResultInfoBinding
import com.zhuorui.securities.market.model.OnNotifyObserver
import com.zhuorui.securities.market.model.SearchStokcInfoEnum
import com.zhuorui.securities.market.ui.adapter.SeachAllofInfoAdapter
import com.zhuorui.securities.market.ui.adapter.StockAdapter
import com.zhuorui.securities.market.ui.adapter.StockInfoAdapter
import com.zhuorui.securities.market.ui.presenter.SearchResultInfoPresenter
import com.zhuorui.securities.market.ui.view.SearchResultInfoView
import com.zhuorui.securities.market.ui.viewmodel.SearchResultInfoViewModel
import kotlinx.android.synthetic.main.fragment_search_result_info.*
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/20
 * Desc:
 */
class SearchResultInfoFragment :
    AbsFragment<FragmentSearchResultInfoBinding, SearchResultInfoViewModel, SearchResultInfoView, SearchResultInfoPresenter>(),SearchResultInfoView {

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
        adapter = presenter?.getAdapter()
        stockadapter = presenter?.getStockAdapter()
        infoadapter = presenter?.getStockInfoAdapter()
        presenter?.setType(type)
         if (presenter?.ts!=null) {
             initRv(presenter?.ts)
          }else{
             initRv(SearchStokcInfoEnum.All)
        }
    }

    override fun detailInfo(str: String) {
        rv_serach_all.adapter = adapter
        presenter?.getData(type, str)
        adapter?.notifyDataSetChanged()
    }

    override fun detailStock(str: String) {
        presenter?.getStockData(str)
        rv_serach_all.adapter = stockadapter
        stockadapter?.notifyDataSetChanged()
    }

    override fun detailStockInfo(str: String) {
        presenter?.getStockInfoData()
        rv_serach_all.adapter = infoadapter
        infoadapter?.notifyDataSetChanged()
    }

    fun initRv(enum: SearchStokcInfoEnum?) {
        when (enum) {
            SearchStokcInfoEnum.All -> {
                sm_refrsh.setEnableRefresh(false)
                sm_refrsh.setEnableLoadMore(false)
            }
            SearchStokcInfoEnum.Stock -> {
                sm_refrsh.setEnableRefresh(false)
                sm_refrsh.setEnableLoadMore(true)
            }
            SearchStokcInfoEnum.Info -> {
                sm_refrsh.setEnableRefresh(false)
                sm_refrsh.setEnableLoadMore(true)
            }
        }

    }
    override fun initonlazy() {
         init()
    }
}

