package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailF10BriefBinding
import com.zhuorui.securities.market.model.SearchStockInfo
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
        fun newInstance(stock: SearchStockInfo, type: Int): CompanyBrieViewMoreFragment {
            val fragment = CompanyBrieViewMoreFragment()
            val arguments = Bundle()
            arguments.putParcelable(SearchStockInfo::class.java.simpleName, stock)
            arguments.putInt("type", type)
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

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        val type = arguments?.getInt("type")
        setTitle(type)

        val stock = arguments?.getParcelable<SearchStockInfo>(SearchStockInfo::class.java.simpleName)!!
        setStock(stock)
    }

    private fun setStock(stock: SearchStockInfo) {
        iv_ts_logo.setImageResource(MarketUtil.getStockTSIcon(stock.ts))
        tv_stock_name.text = stock.name
    }

    private fun setTitle(type: Int?) {
        when (type) {
            1 -> {
                top_bar.setTitle(getString(R.string.company_manager))
                company_manager_bar.visibility = View.VISIBLE
            }
            2 -> {
                top_bar.setTitle(getString(R.string.hareholder_change))
                shareholder_change_bar.visibility = View.VISIBLE
            }
            3 -> {
                top_bar.setTitle(getString(R.string.company_dividend))
                company_dividend_bar.visibility = View.VISIBLE
            }
            4 -> {
                top_bar.setTitle(getString(R.string.company_repo))
                company_repo_bar.visibility = View.VISIBLE
            }
        }
    }
}