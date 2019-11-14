package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailF10FinancialBinding
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.net.response.FinancialReportResponse
import com.zhuorui.securities.market.ui.presenter.MarketDetailF10FinancialPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailF10FinancialView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailF10FinancialViewModel
import kotlinx.android.synthetic.main.fragment_market_detail_f10_financial.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/6 13:35
 *    desc   : F10财报页面
 */
class MarketDetailF10FinancialFragment :
    AbsFragment<FragmentMarketDetailF10FinancialBinding, MarketDetailF10FinancialViewModel, MarketDetailF10FinancialView, MarketDetailF10FinancialPresenter>(),
    MarketDetailF10FinancialView {

    private var mStock: SearchStockInfo? = null
    override val layout: Int
        get() = R.layout.fragment_market_detail_f10_financial

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: MarketDetailF10FinancialPresenter
        get() = MarketDetailF10FinancialPresenter()

    override val createViewModel: MarketDetailF10FinancialViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailF10FinancialViewModel::class.java)

    override val getView: MarketDetailF10FinancialView
        get() = this

    private var stockCode: String? = null
    private var ts: String? = null
    companion object {
        fun newInstance(stock: SearchStockInfo): MarketDetailF10FinancialFragment {
            val fragment = MarketDetailF10FinancialFragment()
            if (stock != null) {
                val bundle = Bundle()
                bundle.putParcelable(SearchStockInfo::class.java.simpleName, stock)
                fragment.arguments = bundle
            }
            return fragment

        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mStock = arguments?.getParcelable(SearchStockInfo::class.java.simpleName)!!
        presenter?.setLifecycleOwner(this)
        presenter?.getFinancialData(mStock!!.code,mStock!!.ts)
      /*  val outData = mutableListOf<Float>()
        outData.add(4158.15f)
        outData.add(1249.04f)
        outData.add(1149.74f)
        outData.add(345.53f)*/
    }

    override fun updataBuisnessData(data: FinancialReportResponse.Business) {
        financial_view.setPieCharBean(data)
    }

    override fun updataProfitListData(profitList: List<FinancialReportResponse.CashFlowReport>) {
        crash_flow.setProfitListData(profitList)
    }

    override fun updataErrorData() {
        financial_view.setPieCharBean(null)
        profit_chat_one.setProfitChatData(null)
        profit_chat_two.setOutProfitChatData(null)
        crash_flow.setProfitListData(null)
    }
    override fun updataProfitChatData(profitList: List<FinancialReportResponse.ProfitReport>) {
       profit_chat_one.setProfitChatData(profitList)
    }

    override fun updataOutProfitChatData(profitList: List<FinancialReportResponse.LiabilistyReport>) {
        profit_chat_two.setOutProfitChatData(profitList)
    }

}