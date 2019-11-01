package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailNoticeBinding
import com.zhuorui.securities.market.model.TestNoticeData
import com.zhuorui.securities.market.ui.adapter.MarketNoticeInfoAdapter
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
    AbsSwipeBackNetFragment<FragmentMarketDetailNoticeBinding, MarketDetailNoticeViewModel, MarketDetailNoticeView, MarketDetailNoticePresenter>(),
    MarketDetailNoticeView {
    private var noticeAdapter:MarketNoticeInfoAdapter?=null
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

    companion object {
        fun newInstance(): MarketDetailNoticeFragment {
            return MarketDetailNoticeFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        presenter?.setLifecycleOwner(this)
        noticeAdapter=presenter?.getNoticeAdapter()
        presenter?.getNoticeData()
        rv_notice.adapter =noticeAdapter
    }

    override fun addIntoNoticeData(list: List<TestNoticeData>) {
        noticeAdapter?.clearItems()
        if (noticeAdapter?.items == null) {
            noticeAdapter?.items = ArrayList()
        }
        noticeAdapter?.addItems(list)
    }

}