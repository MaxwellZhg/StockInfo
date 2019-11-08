package com.zhuorui.securities.market.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.ZRStockTextView
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailF10BriefBinding
import com.zhuorui.securities.market.net.response.F10BrieResponse
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


/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/6 13:36
 *    desc   : F10简况页面
 */
class MarketDetailF10BriefFragment :
    AbsFragment<FragmentMarketDetailF10BriefBinding, MarketDetailF10BriefViewModel, MarketDetailF10BriefView, MarketDetailF10BriefPresenter>(),
    MarketDetailF10BriefView, View.OnClickListener {

    companion object {
        fun newInstance(code: String): MarketDetailF10BriefFragment {
            val fragment = MarketDetailF10BriefFragment()
            val arguments = Bundle()
            arguments.putString("code", code)
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
        val code = arguments?.getString("code")

        iv_company_manager_more.setOnClickListener(this)
        tv_company_business_more.setOnClickListener(this)
        iv_hareholder_change_more.setOnClickListener(this)
        iv_company_dividend_more.setOnClickListener(this)
        iv_company_repo_more.setOnClickListener(this)

        code?.let { presenter?.loadData(it) }
    }

    override fun onClick(v: View?) {
        when (v) {
            iv_company_manager_more -> {
                // 更多高管信息
            }
            tv_company_business_more -> {
                // 查看完整公司业务
                toggleViewCompanyBusiness()
            }
            iv_hareholder_change_more -> {
                // 更多股东信息
            }
            iv_company_dividend_more -> {
                // 更多分红信息
            }
            iv_company_repo_more -> {
                // 更多回购信息
            }
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
        manager: List<F10BrieResponse.Manager>?,
        shareHolderChange: List<F10BrieResponse.ShareHolderChange>?,
        dividend: List<F10BrieResponse.Dividend>?,
        repo: List<F10BrieResponse.Repo>?
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
            for (i in manager.indices) {
                val itemView = View.inflate(context, R.layout.layout_item_company_manager, null)
                ll_manager_list.addView(itemView)
                if (i != 0) {
                    val layoutParams = itemView.layoutParams
                    (layoutParams as LinearLayoutCompat.LayoutParams).topMargin = ResUtil.getDimensionDp2Px(14.5f)
                    itemView.layoutParams = layoutParams
                }
                val item = manager[i]
                item.name?.let { itemView.findViewById<TextView>(R.id.tv_name).text = it }
                item.jobTitle?.let { itemView.findViewById<TextView>(R.id.tv_jobtitle).text = it }
                item.salary?.let {
                    itemView.findViewById<TextView>(R.id.tv_salary).text = MathUtil.convertToUnitString(it, 1)
                }
            }
        }

        // 股东变动
        if (!shareHolderChange.isNullOrEmpty()) {
            for (i in shareHolderChange.indices) {
                val itemView = View.inflate(context, R.layout.layout_item_shareholder_change, null)
                ll_shareholder_list.addView(itemView)
                if (i != 0) {
                    val layoutParams = itemView.layoutParams
                    (layoutParams as LinearLayoutCompat.LayoutParams).topMargin = ResUtil.getDimensionDp2Px(14.5f)
                    itemView.layoutParams = layoutParams
                }
                val item = shareHolderChange[i]
                item.name?.let { itemView.findViewById<TextView>(R.id.tv_name).text = it }
                val changeNumberText = itemView.findViewById<ZRStockTextView>(R.id.tv_hareholder_change_number)
                if (item.changeType == 1) {
                    if (item.changeNumber == null) {
                        changeNumberText.setText(ResUtil.getString(R.string.hareholder_change_add), 1)
                    } else {
                        changeNumberText.setText("+" + MathUtil.convertToUnitString(item.changeNumber, 1), 1)
                    }
                } else {
                    if (item.changeNumber == null) {
                        changeNumberText.setText(ResUtil.getString(R.string.hareholder_change_sub), 2)
                    } else {
                        changeNumberText.setText("-" + MathUtil.convertToUnitString(item.changeNumber, 1), 2)
                    }
                }
                item.holdStockNumber?.let {
                    itemView.findViewById<TextView>(R.id.tv_hareholder_number).text =
                        MathUtil.convertToUnitString(it, 1)
                }
                item.date?.let { itemView.findViewById<TextView>(R.id.tv_date).text = DateUtil.formatDate(it) }
            }
        }

        // 分红送配
        if (!dividend.isNullOrEmpty()) {
            for (i in dividend.indices) {
                val itemView = View.inflate(context, R.layout.layout_item_dividend, null)
                ll_dividend_list.addView(itemView)
                if (i != 0) {
                    val layoutParams = itemView.layoutParams
                    (layoutParams as LinearLayoutCompat.LayoutParams).topMargin = ResUtil.getDimensionDp2Px(14.5f)
                    itemView.layoutParams = layoutParams
                }
                val item = dividend[i]
                item.exemptionDate?.let {
                    itemView.findViewById<TextView>(R.id.tv_exemption_date).text = DateUtil.formatDate(it)
                }
                item.date?.let { itemView.findViewById<TextView>(R.id.tv_dividend_date).text = DateUtil.formatDate(it) }
                item.allocationPlan?.let { itemView.findViewById<TextView>(R.id.tv_allocation_plan).text = it }
            }
        }

        // 公司回购
        if (!repo.isNullOrEmpty()) {
            for (i in repo.indices) {
                val itemView = View.inflate(context, R.layout.layout_item_repo, null)
                ll_repo_list.addView(itemView)
                if (i != 0) {
                    val layoutParams = itemView.layoutParams
                    (layoutParams as LinearLayoutCompat.LayoutParams).topMargin = ResUtil.getDimensionDp2Px(14.5f)
                    itemView.layoutParams = layoutParams
                }
                val item = repo[i]
                item.date?.let { itemView.findViewById<TextView>(R.id.tv_date).text = DateUtil.formatDate(it) }
                item.number?.let {
                    itemView.findViewById<TextView>(R.id.tv_number).text = MathUtil.convertToUnitString(it, 1)
                }
                item.avgPrice?.let {
                    itemView.findViewById<TextView>(R.id.tv_price).text = MathUtil.rounded3(it).toString()
                }
            }
        }
    }
}