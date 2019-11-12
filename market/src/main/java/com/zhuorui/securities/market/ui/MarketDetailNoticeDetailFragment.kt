package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailNoticeDetailBinding
import com.zhuorui.securities.market.ui.presenter.MarketDetailNoticeDetailPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailNoticeDetailView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailNoticeDetailViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:51
 *    desc   :
 */


class MarketDetailNoticeDetailFragment :AbsSwipeBackNetFragment<FragmentMarketDetailNoticeDetailBinding,MarketDetailNoticeDetailViewModel,MarketDetailNoticeDetailView,MarketDetailNoticeDetailPresenter>(),MarketDetailNoticeDetailView{
    override val layout: Int
        get() = R.layout.fragment_market_detail_notice_detail
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketDetailNoticeDetailPresenter
        get() = MarketDetailNoticeDetailPresenter()
    override val createViewModel: MarketDetailNoticeDetailViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailNoticeDetailViewModel::class.java)
    override val getView: MarketDetailNoticeDetailView
        get() = this
    private var lineId: String? = null
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        lineId = arguments?.getSerializable("lineId") as String?
        lineId?.let { presenter?.getAllAttachment(it) }
    }

    companion object{
        fun newInstance(lineId:String):MarketDetailNoticeDetailFragment{
            val fragment = MarketDetailNoticeDetailFragment()
            if (lineId != null) {
                val bundle = Bundle()
                bundle.putSerializable("lineId", lineId)
                fragment.arguments = bundle
            }
            return fragment
        }
    }
}
