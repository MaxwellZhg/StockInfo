package com.zhuorui.securities.market.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuorui.commonwidget.LinearSpacingItemDecoration
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailF10BriefBinding
import com.zhuorui.securities.market.model.*
import com.zhuorui.securities.market.net.response.F10BrieResponse
import com.zhuorui.securities.market.ui.adapter.CompanyBrieDividendAdapter
import com.zhuorui.securities.market.ui.adapter.CompanyBrieManagerAdapter
import com.zhuorui.securities.market.ui.adapter.CompanyBrieRepoAdapter
import com.zhuorui.securities.market.ui.adapter.CompanyBrieShareHolderChangeAdapter
import com.zhuorui.securities.market.ui.presenter.MarketDetailF10BriefPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailF10BriefView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailF10BriefViewModel
import com.zhuorui.securities.market.util.DateUtil
import com.zhuorui.securities.market.util.MathUtil
import kotlinx.android.synthetic.main.layout_market_detail_company.*
import kotlinx.android.synthetic.main.layout_market_detail_company_dividend.*
import kotlinx.android.synthetic.main.layout_market_detail_company_repo.*
import kotlinx.android.synthetic.main.layout_market_detail_manager.*
import kotlinx.android.synthetic.main.layout_market_detail_shareholderchange.*
import me.yokeyword.fragmentation.SupportFragment

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/6 13:36
 *    desc   : F10简况页面
 */
class MarketDetailF10BriefFragment :
    AbsFragment<FragmentMarketDetailF10BriefBinding, MarketDetailF10BriefViewModel, MarketDetailF10BriefView, MarketDetailF10BriefPresenter>(),
    MarketDetailF10BriefView, View.OnClickListener {

    private var mStock: SearchStockInfo? = null

    companion object {
        fun newInstance(stock: SearchStockInfo): MarketDetailF10BriefFragment {
            val fragment = MarketDetailF10BriefFragment()
            val arguments = Bundle()
            arguments.putParcelable(SearchStockInfo::class.java.simpleName, stock)
            fragment.arguments = arguments
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_market_detail_f10_brief

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: MarketDetailF10BriefPresenter
        get() = MarketDetailF10BriefPresenter()

    override val createViewModel: MarketDetailF10BriefViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailF10BriefViewModel::class.java)

    override val getView: MarketDetailF10BriefView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mStock = arguments?.getParcelable(SearchStockInfo::class.java.simpleName)!!

        iv_company_manager_more.setOnClickListener(this)
        tv_company_business_more.setOnClickListener(this)
        iv_hareholder_change_more.setOnClickListener(this)
        iv_company_dividend_more.setOnClickListener(this)
        iv_company_repo_more.setOnClickListener(this)

        presenter?.loadData(mStock?.ts!!, mStock?.code!!)
    }

    override fun onClick(v: View?) {
        var type = 0

        when (v) {
            iv_company_manager_more -> {
                // 更多高管信息
                type = 1
            }
            tv_company_business_more -> {
                // 查看完整公司业务
                toggleViewCompanyBusiness()
            }
            iv_hareholder_change_more -> {
                // 更多股东信息
                type = 2
            }
            iv_company_dividend_more -> {
                // 更多分红信息
                type = 3
            }
            iv_company_repo_more -> {
                // 更多回购信息
                type = 4
            }
        }

        if (type != 0 && mStock != null) {
            val managers = presenter?.getManagers()
            (parentFragment as SupportFragment).start(CompanyBrieViewMoreFragment.newInstance(mStock!!, managers, type))
        }
    }

    private fun toggleViewCompanyBusiness() {
        // 获取省略的字符数，大于0表示存在省略
        val ellipsisCount = tv_company_business.layout.getEllipsisCount(tv_company_business.lineCount - 1)
        if (ellipsisCount > 0) {
            tv_company_business.maxHeight = resources.displayMetrics.heightPixels
            tv_company_business_more.text = ResUtil.getString(R.string.pack_up)
        } else {
            tv_company_business.maxLines = 4
            tv_company_business_more.text = ResUtil.getString(R.string.more)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun updateBrieInfo(
        company: F10BrieResponse.Company?,
        manager: List<F10ManagerModel>?,
        shareHolderChange: List<F10ShareHolderModel>?,
        dividend: List<F10DividendModel>?,
        repo: List<F10RepoModel>?
    ) {
        // 公司简介
        if (company != null) {
            company.name?.let { tv_company_name.text = it }
            company.industry?.let { tv_company_industry.text = it }
            company.chairman?.let { tv_company_chairman.text = it }
            company.listingDate?.let { tv_company_mdate.text = DateUtil.formatDate(it) }
            company.issuePrice?.let { tv_offering_price.text = it.toString() }
            company.issueNumber?.let { tv_issue_number.text = MathUtil.convertToUnitString(it, 2) }
            company.totalCapitalStock?.let { tv_total_capital_stock.text = MathUtil.convertToUnitString(it, 2) }
            company.equityHK?.let { tv_equity_hk.text = MathUtil.convertToUnitString(it, 2) }
            company.business?.let {
                tv_company_business.text = it + it + it
                // 判断公司业务是否需要显示更多
                val ellipsisCount = tv_company_business.layout.getEllipsisCount(tv_company_business.lineCount - 1)
                if (ellipsisCount > 0) {
                    tv_company_business_more.visibility = View.VISIBLE
                }
            }
        }

        // 高管信息
        if (!manager.isNullOrEmpty()) {
            ll_manager_list.layoutManager = LinearLayoutManager(context)
            ll_manager_list.addItemDecoration(LinearSpacingItemDecoration(ResUtil.getDimensionDp2Px(14.5f), 0, false))
            val adapter = CompanyBrieManagerAdapter()
            adapter.items = manager
            ll_manager_list.adapter = adapter
        }

        // 股东变动
        if (!shareHolderChange.isNullOrEmpty()) {
            ll_shareholder_list.layoutManager = LinearLayoutManager(context)
            ll_shareholder_list.addItemDecoration(LinearSpacingItemDecoration(ResUtil.getDimensionDp2Px(14.5f), 0, false))
            val adapter = CompanyBrieShareHolderChangeAdapter()
            adapter.items = shareHolderChange
            ll_shareholder_list.adapter = adapter
        }

        // 分红送配
        if (!dividend.isNullOrEmpty()) {
            ll_dividend_list.layoutManager = LinearLayoutManager(context)
            ll_dividend_list.addItemDecoration(LinearSpacingItemDecoration(ResUtil.getDimensionDp2Px(14.5f), 0, false))
            val adapter = CompanyBrieDividendAdapter()
            adapter.items = dividend
            ll_dividend_list.adapter = adapter
        }

        // 公司回购
        if (!repo.isNullOrEmpty()) {
            ll_repo_list.layoutManager = LinearLayoutManager(context)
            ll_repo_list.addItemDecoration(LinearSpacingItemDecoration(ResUtil.getDimensionDp2Px(14.5f), 0, false))
            val adapter = CompanyBrieRepoAdapter()
            adapter.items = repo
            ll_repo_list.adapter = adapter
        }
    }
}