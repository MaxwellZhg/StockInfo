package com.zhuorui.securities.market.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentAllChooseStockBinding
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.StockTsEnum
import com.zhuorui.securities.market.ui.detail.StockDetailLandActivity
import com.zhuorui.securities.market.ui.presenter.TopicStockListFragmentPresenter
import com.zhuorui.securities.market.ui.view.TopicStockListFragmentView
import com.zhuorui.securities.market.ui.viewmodel.TopicStockListViewModel
import kotlinx.android.synthetic.main.fragment_all_choose_stock.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/7
 * Desc: 自选股列表界面
 */
class TopicStockListFragment :
    AbsFragment<FragmentAllChooseStockBinding, TopicStockListViewModel, TopicStockListFragmentView, TopicStockListFragmentPresenter>(),
    BaseListAdapter.OnClickItemCallback<StockMarketInfo>, View.OnClickListener,
    TopicStockListFragmentView {

    private var type: StockTsEnum? = null
    private var mAdapter: TopicStocksAdapter? = null
    private var currentPage = 0
    private var pageSize = 20

    companion object {
        fun newInstance(type: StockTsEnum?): TopicStockListFragment {
            val fragment = TopicStockListFragment()
            if (type != null) {
                val bundle = Bundle()
                bundle.putSerializable("type", type)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_all_choose_stock

    override val viewModelId: Int
        get() = BR.viewModel
    
    override val createPresenter: TopicStockListFragmentPresenter
        get() = TopicStockListFragmentPresenter()
    
    override val createViewModel: TopicStockListViewModel?
        get() = ViewModelProviders.of(this).get(TopicStockListViewModel::class.java)
    
    override val getView: TopicStockListFragmentView
        get() = this

    override fun init() {
        type = arguments?.getSerializable("type") as StockTsEnum?
        rl_updown.setOnClickListener(this)
        rl_arrows.setOnClickListener(this)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        // 设置列表数据适配器
        (rv_stock.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        rv_stock.layoutManager = LinearLayoutManager(context)
        mAdapter = presenter?.getAdapter()
        mAdapter?.setClickItemCallback(this)
        rv_stock.adapter = mAdapter

        requestStocks()
    }

    override fun onClickItem(pos: Int, item: StockMarketInfo?, v: View?) {
        if (item != null) {
            // 跳转到详情页
            startActivity(Intent(context, StockDetailLandActivity::class.java))
        } else {
            // 跳转到搜索
            (parentFragment as AbsFragment<*, *, *, *>).start(StockSearchFragment.newInstance(1))
        }
    }

    /**
     * 加载推荐自选股列表
     */
    private fun requestStocks() {
        presenter?.requestStocks(type, currentPage, pageSize)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.rl_updown -> {

            }
            R.id.rl_arrows -> {

            }
        }
    }
}