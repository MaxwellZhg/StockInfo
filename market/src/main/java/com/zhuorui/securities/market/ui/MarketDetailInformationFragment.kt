package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailBinding
import com.zhuorui.securities.market.event.MarketDetailInfoEvent
import com.zhuorui.securities.market.net.response.MarketNewsListResponse
import com.zhuorui.securities.market.ui.adapter.MarketInfoAdapter
import com.zhuorui.securities.market.ui.presenter.MarketDetailInformationPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailInformationView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailInformationViewModel
import kotlinx.android.synthetic.main.fragment_market_detail_information.*

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:51
 *    desc   : 个股详情资讯页面
 */
class MarketDetailInformationFragment :
    AbsFragment<FragmentMarketDetailBinding, MarketDetailInformationViewModel, MarketDetailInformationView, MarketDetailInformationPresenter>(),
    MarketDetailInformationView,MarketInfoAdapter.OnMarketInfoClickListener,
    OnRefreshLoadMoreListener {

    private var currentPage: Int = 1

    private var infoAdapter: MarketInfoAdapter? = null

    override val layout: Int
        get() = R.layout.fragment_market_detail_information

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: MarketDetailInformationPresenter
        get() = MarketDetailInformationPresenter()

    override val createViewModel: MarketDetailInformationViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailInformationViewModel::class.java)

    override val getView: MarketDetailInformationView
        get() = this

    private var stockCode: String? = null

    companion object {
        fun newInstance(stockCode: String): MarketDetailInformationFragment {
            val fragment = MarketDetailInformationFragment()
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
        stockCode = arguments?.getSerializable("code") as String?
        srl_layout.setEnableLoadMore(true)
        srl_layout.setOnRefreshLoadMoreListener(this)
        presenter?.setLifecycleOwner(this)
        infoAdapter = presenter?.getInfoAdapter()
        infoAdapter?.onMarketInfoClickListener = this
        stockCode?.let { presenter?.getNewsListData(it, currentPage) }
        rv_market_info.adapter = infoAdapter
    }

    override fun addIntoInfoData(list: List<MarketNewsListResponse.DataList>) {
        if (currentPage == 1) {
            empty_view.visibility = View.INVISIBLE
            infoAdapter?.clearItems()
        }
        if (infoAdapter?.items == null) {
            infoAdapter?.items = ArrayList()
        }
        srl_layout.finishLoadMore(true)
        infoAdapter?.addItems(list)

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        currentPage++
        stockCode?.let { presenter?.getNewsListData(it, currentPage) }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

    }





    override fun marketInfoClick() {
        ToastUtil.instance.toastCenter("资讯")
    }

    override fun noMoreData() {
        when (currentPage) {
            1 -> {
                empty_view.visibility = View.VISIBLE
            }
            else -> {
                srl_layout.finishLoadMore(true)//结束加载（加载失败）
                srl_layout.finishLoadMoreWithNoMoreData()
                srl_layout.setNoMoreData(true)
            }
        }

    }

    override fun loadFailData() {
        srl_layout.finishLoadMore(false)
    }


}