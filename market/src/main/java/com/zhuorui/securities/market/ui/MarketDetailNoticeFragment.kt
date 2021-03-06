package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailNoticeBinding
import com.zhuorui.securities.market.net.response.MarketBaseInfoResponse
import com.zhuorui.securities.market.ui.adapter.MarketNoticeInfoTipsAdapter
import com.zhuorui.securities.market.ui.adapter.`MarketNoticeInfoTipsAdapter$ViewHolder_ViewBinding`
import com.zhuorui.securities.market.ui.presenter.MarketDetailNoticePresenter
import com.zhuorui.securities.market.ui.view.MarketDetailNoticeView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailNoticeViewModel
import kotlinx.android.synthetic.main.fragment_market_detail_notice.*

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:51
 *    desc   :
 */
class MarketDetailNoticeFragment :
    AbsFragment<FragmentMarketDetailNoticeBinding, MarketDetailNoticeViewModel, MarketDetailNoticeView, MarketDetailNoticePresenter>(),
    MarketDetailNoticeView,MarketNoticeInfoTipsAdapter.OnMarketNoticeClickListener{
    private var currentPage :Int = 1
    private var noticeAdapter: MarketNoticeInfoTipsAdapter?=null
    override val layout: Int
        get() = R.layout.fragment_market_detail_notice
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketDetailNoticePresenter
        get() = MarketDetailNoticePresenter()
    override val createViewModel: MarketDetailNoticeViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailNoticeViewModel::class.java)
    override val getView: MarketDetailNoticeView
        get() = this
    private var stockCode: String = ""
    companion object {
        fun newInstance(stockCode:String): MarketDetailNoticeFragment {
            val fragment = MarketDetailNoticeFragment()
            if (stockCode != null) {
                val bundle = Bundle()
                bundle.putString("code", stockCode)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        stockCode = arguments?.getString("code")?:stockCode
        presenter?.setLifecycleOwner(this)
        noticeAdapter=presenter?.getNoticeAdapter()
        noticeAdapter?.onMarketNoticeClickListener=this
        stockCode?.let { presenter?.getMarketBaseInfoData(it,currentPage) }
       // presenter?.getModelData()
        rv_notice.adapter =noticeAdapter
    }

    override fun onMarketNoticeClick(lineId:String) {
        var pre = parentFragment as AbsFragment<*, *, *, *>
        pre.start(MarketDetailNoticeDetailFragment.newInstance(lineId))
    }

    override fun addIntoNoticeData(list: List<MarketBaseInfoResponse.Source>) {
        noticeAdapter?.clearItems()
        if (noticeAdapter?.items == null) {
            noticeAdapter?.items = ArrayList()
        }
        noticeAdapter?.addItems(list)
    }
    override fun noMoreData() {

    }


}