package com.dycm.applib

import com.dycm.applib1.ui.SearchStocksAdapter


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dycm.applib1.R
import com.dycm.applib1.model.SearchStockInfo
import com.dycm.applib1.net.IStockNet
import com.dycm.applib1.net.request.StockSearchRequset
import com.dycm.applib1.net.response.StockSearchResponse
import com.dycm.base2app.Cache
import com.dycm.base2app.network.Network
import com.dycm.base2app.rxbus.EventThread
import com.dycm.base2app.rxbus.RxSubscribe
import com.dycm.base2app.ui.fragment.AbsBackFinishNetFragment
import com.dycm.base2app.ui.fragment.AbsFragment
import com.dycm.base2app.ui.fragment.AbsSwipeBackEventFragment
import kotlinx.android.synthetic.main.fragment_topic_stock_search.*
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/8
 * Desc:自选股搜索
 */
class TopicStockSearchFragment : AbsSwipeBackEventFragment(), TextWatcher,
    SearchStocksAdapter.OnAddTopicClickItemCallback {
    private var type: Int = 0
    private lateinit var tips: String
    private lateinit var datas: List<SearchStockInfo>
    private var adapter: SearchStocksAdapter? = null
    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    companion object {

        const val min = 1 // 列表1-5
        const val max = 2 // 列表>5

        fun newInstance(type: Int): TopicStockSearchFragment {
            val fragment = TopicStockSearchFragment()
            val bundle = Bundle()
            bundle.putInt("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_topic_stock_search

    override fun init() {
        et_serach.addTextChangedListener(this)
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                getTopicStockData(it, 5)
                type = min
            }

        } else {
            search_list.visibility = View.INVISIBLE
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockSearchResponse(response: StockSearchResponse) {
        if (response.data.datas.isNotEmpty()) {
            // 显示搜索列表
            datas = response.data.datas
            search_list.visibility = View.VISIBLE
            // 设置数据
            if (search_list.adapter == null) {
                search_list.layoutManager = LinearLayoutManager(context)
                adapter = SearchStocksAdapter()
                adapter!!.onAddTopicClickItemCallback = this
                search_list.adapter = adapter
                adapter!!.addItems(response.data.datas)
            }
        }
    }

    override fun onAddTopicClickItem(pos: Int, item: SearchStockInfo?, view: View) {

    }

    override fun addSerachTopicMore(pos: Int, item: SearchStockInfo?, view: View) {
        type = max
        //20条数据请求。。目前注释
        //getTopicStockData(tips,20)
        //假数据操作
        for (i in 1..4) {
            if (adapter?.items?.size!! < 20) {
                adapter?.addItems(datas)
            }
            adapter?.notifyDataSetChanged()
        }
    }

    fun getTopicStockData(str: String, count: Int) {
        val requset = StockSearchRequset(str, 0, count, transactions.createTransaction())
        Cache[IStockNet::class.java]?.search(requset)
            ?.enqueue(Network.IHCallBack<StockSearchResponse>(requset))
        tips = str
    }


}