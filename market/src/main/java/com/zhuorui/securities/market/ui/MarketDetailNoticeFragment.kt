package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailNoticeBinding
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
    AbsSwipeBackNetFragment<FragmentMarketDetailNoticeBinding, MarketDetailNoticeViewModel, MarketDetailNoticeView, MarketDetailNoticePresenter>(),
    MarketDetailNoticeView,MarketNoticeInfoTipsAdapter.OnMarketNoticeClickListener{
    override fun addIntoNoticeData(list: List<Int>) {
        noticeAdapter?.clearItems()
        if (noticeAdapter?.items == null) {
            noticeAdapter?.items = ArrayList()
        }
        noticeAdapter?.addItems(list)
    }

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

    companion object {
        fun newInstance(stockCode:String): MarketDetailNoticeFragment {
            val fragment = MarketDetailNoticeFragment()
            if (stockCode != null) {
                val bundle = Bundle()
                bundle.putSerializable("code", stockCode)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        presenter?.setLifecycleOwner(this)
        noticeAdapter=presenter?.getNoticeAdapter()
        noticeAdapter?.onMarketNoticeClickListener=this
        presenter?.getNoticeData()
        rv_notice.adapter =noticeAdapter
    }

    override fun onMarketNoticeClick() {
      ToastUtil.instance.toastCenter("公告")
    }

}