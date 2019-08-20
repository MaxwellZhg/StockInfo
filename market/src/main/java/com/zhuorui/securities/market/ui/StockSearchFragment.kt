package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentTopicStockSearchBinding
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.net.response.StockSearchResponse
import com.zhuorui.securities.market.ui.presenter.StockSearchFragmentPresenter
import com.zhuorui.securities.market.ui.view.StockSearchFragmentView
import com.zhuorui.securities.market.ui.viewmodel.StockSearchViewModel
import kotlinx.android.synthetic.main.fragment_topic_stock_search.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/8
 * Desc:自选股搜索
 */
class StockSearchFragment :
    AbsSwipeBackNetFragment<FragmentTopicStockSearchBinding, StockSearchViewModel, StockSearchFragmentView, StockSearchFragmentPresenter>(),
    StockSearchFragmentView, TextWatcher, SearchStocksAdapter.OnAddTopicClickItemCallback,
    BaseListAdapter.OnClickItemCallback<SearchStockInfo> {

    private var type: Int = 0
    private lateinit var tips: String
    private var adapter: SearchStocksAdapter? = null

    private var handler = Handler()
    private var getTopicStockDataRunnable: GetTopicStockDataRunnable? = null

    companion object {

        const val min = 1 // 列表1-5
        const val max = 2 // 列表>5

        fun newInstance(type: Int): StockSearchFragment {
            val fragment = StockSearchFragment()
            val bundle = Bundle()
            bundle.putInt("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_topic_stock_search

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: StockSearchFragmentPresenter
        get() = StockSearchFragmentPresenter()

    override val createViewModel: StockSearchViewModel?
        get() = ViewModelProviders.of(this).get(StockSearchViewModel::class.java)

    override val getView: StockSearchFragmentView get() = this

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    override fun init() {
        et_serach.addTextChangedListener(this)
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {

            p0?.toString()?.trim()?.let {
                handler.removeCallbacks(getTopicStockDataRunnable)
                getTopicStockDataRunnable = GetTopicStockDataRunnable(it)
                handler.postDelayed(getTopicStockDataRunnable, 500)
                type = min
            }

        } else {
            search_list.visibility = View.INVISIBLE
        }
    }

    private inner class GetTopicStockDataRunnable(val keyWord: String) : Runnable {
        override fun run() {
            search_list.adapter = null
            tips = keyWord
            presenter?.getTopicStockData(keyWord, 5)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onStockSearchResponse(response: StockSearchResponse) {
        if (response.data != null && response.data.datas.isNotEmpty()) {
            // 显示搜索列表
            search_list.visibility = View.VISIBLE
            // 设置数据
            if (search_list.adapter == null) {
                search_list.layoutManager = LinearLayoutManager(context)
                adapter = SearchStocksAdapter()
                adapter!!.setClickItemCallback(this)
                adapter!!.onAddTopicClickItemCallback = this
                search_list.adapter = adapter
                adapter!!.addItems(response.data.datas)
            } else {
                if (adapter!!.items.size < 20) {
                    adapter!!.addItems(response.data.datas)
                }
            }
        }
    }

    override fun onClickItem(pos: Int, item: SearchStockInfo?, v: View?) {
        if (item == null) {
            type = max
            //做更多的请求
            presenter?.getTopicStockData(tips, 20)
        }
    }

    override fun onAddTopicClickItem(pos: Int, item: SearchStockInfo?, view: View) {
        // 点击添加到自选列表
        presenter?.onAddTopicClickItem(item)
    }
}