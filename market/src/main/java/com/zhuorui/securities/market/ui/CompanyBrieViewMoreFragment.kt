package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuorui.commonwidget.LinearSpacingItemDecoration
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailF10BriefBinding
import com.zhuorui.securities.market.model.F10DividendModel
import com.zhuorui.securities.market.model.F10ManagerModel
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.net.response.F10ShareHolderListResponse
import com.zhuorui.securities.market.ui.adapter.CompanyBrieDividendAdapter
import com.zhuorui.securities.market.ui.adapter.CompanyBrieManagerAdapter
import com.zhuorui.securities.market.ui.adapter.CompanyBrieShareHolderChangeAdapter
import com.zhuorui.securities.market.ui.presenter.CompanyBrieViewMorePresenter
import com.zhuorui.securities.market.ui.view.CompanyBrieViewMoreView
import com.zhuorui.securities.market.ui.viewmodel.CompanyBrieViewMoreViewModel
import com.zhuorui.securities.market.util.MarketUtil
import kotlinx.android.synthetic.main.fragment_company_brie_view_more.*
import kotlinx.android.synthetic.main.layout_topic_stock_item.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/7 19:10
 *    desc   : 查看公司简介更多信息
 */
class CompanyBrieViewMoreFragment :
    AbsSwipeBackNetFragment<FragmentMarketDetailF10BriefBinding, CompanyBrieViewMoreViewModel, CompanyBrieViewMoreView, CompanyBrieViewMorePresenter>(),
    CompanyBrieViewMoreView {

    companion object {
        /**
         * @param type 1公司高管 2持股变动 3分红配送 4公司回购
         */
        fun newInstance(
            stock: SearchStockInfo,
            managers: ArrayList<F10ManagerModel>?,
            type: Int
        ): CompanyBrieViewMoreFragment {
            val fragment = CompanyBrieViewMoreFragment()
            val arguments = Bundle()
            arguments.putParcelable(SearchStockInfo::class.java.simpleName, stock)
            arguments.putInt("type", type)
            if (type == 1) {
                arguments.putParcelableArrayList("managers", managers)
            }
            fragment.arguments = arguments
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_company_brie_view_more

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: CompanyBrieViewMorePresenter
        get() = CompanyBrieViewMorePresenter()

    override val createViewModel: CompanyBrieViewMoreViewModel?
        get() = ViewModelProviders.of(this).get(CompanyBrieViewMoreViewModel::class.java)

    override val getView: CompanyBrieViewMoreView
        get() = this

    @Suppress("UNCHECKED_CAST")
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        val type = arguments?.getInt("type")
        initUi(type)

        val stock = arguments?.getParcelable<SearchStockInfo>(SearchStockInfo::class.java.simpleName)!!
        setStock(stock)

        if (type != 1) {
            if (type == 2) {
                refrsh_layout.setEnableLoadMore(true)
                refrsh_layout.setOnLoadMoreListener { presenter?.loadMoreData(stock.ts!!, stock.code!!, type!!) }
            }
            presenter?.loadData(stock.ts!!, stock.code!!, type!!)
        } else {
            val managers = arguments?.getParcelableArrayList<F10ManagerModel>("managers")
            val adapter = CompanyBrieManagerAdapter()
            recycler_view.adapter = adapter
            adapter.items = managers
        }
    }

    private fun setStock(stock: SearchStockInfo) {
        iv_ts_logo.setImageResource(MarketUtil.getStockTSIcon(stock.ts))
        tv_stock_name.text = stock.name
    }

    private fun initUi(type: Int?) {
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.addItemDecoration(
            LinearSpacingItemDecoration(
                ResUtil.getDimensionDp2Px(14.5f),
                ResUtil.getDimensionDp2Px(12.5f),
                false
            )
        )
        when (type) {
            1 -> {
                top_bar.setTitle(getString(R.string.company_manager))
                company_manager_bar.visibility = View.VISIBLE
            }
            2 -> {
                top_bar.setTitle(getString(R.string.shareholder_change))
                shareholder_change_bar.visibility = View.VISIBLE

                recycler_view.adapter = CompanyBrieShareHolderChangeAdapter()
            }
            3 -> {
                top_bar.setTitle(getString(R.string.company_dividend))
                company_dividend_bar.visibility = View.VISIBLE

                recycler_view.adapter = CompanyBrieDividendAdapter()
            }
            4 -> {
                top_bar.setTitle(getString(R.string.company_repo))
                company_repo_bar.visibility = View.VISIBLE
            }
        }
    }

    override fun updateShareHolderList(data: F10ShareHolderListResponse.Data?) {
        if (data != null) {
            val adapter = recycler_view.adapter as CompanyBrieShareHolderChangeAdapter
            if (adapter.items == null) {
                adapter.items = data.list
            } else {
                refrsh_layout.finishLoadMore()
                adapter.addItems(data.list)
            }
            if (data.list.isNullOrEmpty() || data.list.size < data.pageSize) {
                refrsh_layout.setNoMoreData(true)
            }
        }
    }

    override fun updateDividentList(data: List<F10DividendModel>?) {
        if (data != null) {
            val adapter = recycler_view.adapter as CompanyBrieDividendAdapter
            adapter.items = data
        }
    }
}