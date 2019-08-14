package com.dycm.applib1.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dycm.applib1.R
import com.dycm.applib1.databinding.FragmentTopicStockSearchBinding
import com.dycm.applib1.event.AddTopicStockEvent
import com.dycm.applib1.model.SearchStockInfo
import com.dycm.applib1.net.IStockNet
import com.dycm.applib1.net.request.StockSearchRequest
import com.dycm.applib1.net.response.StockSearchResponse
import com.dycm.applib1.ui.contract.StockSearchContract
import com.dycm.applib1.ui.mvp.StockSearchPresenter
import com.dycm.applib1.ui.mvp.StockSearchVmWarpper
import com.dycm.base2app.Cache
import com.dycm.base2app.adapter.BaseListAdapter
import com.dycm.base2app.mvp.wrapper.BaseMvpNetVmFragment
import com.dycm.base2app.mvp.wrapper.BaseMvpSwipeVmFragment
import com.dycm.base2app.mvp.wrapper.BaseSwipevVmFragment
import com.dycm.base2app.network.Network
import com.dycm.base2app.rxbus.EventThread
import com.dycm.base2app.rxbus.RxBus
import com.dycm.base2app.rxbus.RxSubscribe
import com.dycm.base2app.ui.fragment.AbsSwipeBackEventFragment
import kotlinx.android.synthetic.main.fragment_topic_stock_search.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/8
 * Desc:自选股搜索
 */
class StockSearchFragment : BaseMvpSwipeVmFragment<StockSearchPresenter, StockSearchVmWarpper, FragmentTopicStockSearchBinding>(), TextWatcher,
    SearchStocksAdapter.OnAddTopicClickItemCallback, BaseListAdapter.OnClickItemCallback<SearchStockInfo>,StockSearchContract.View {

    private var type: Int = 0
    private lateinit var tips: String
    private var adapter: SearchStocksAdapter? = null
    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

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

    override fun init() {
        dataBinding.etSerach.addTextChangedListener(this)
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
        if (response.data!=null&&response.data.datas.isNotEmpty()) {
            // 显示搜索列表
            dataBinding.searchList.visibility = View.VISIBLE
            // 设置数据
            if (dataBinding.searchList.adapter == null) {
                dataBinding.searchList.layoutManager = LinearLayoutManager(context)
                adapter = SearchStocksAdapter()
                adapter!!.setClickItemCallback(this)
                adapter!!.onAddTopicClickItemCallback = this
                search_list.adapter = adapter
                adapter!!.addItems(response.data.datas)
            }else{
                if(adapter!!.items.size<20) {
                    adapter!!.addItems(response.data.datas)
                }
            }
        }
    }

    override fun onClickItem(pos: Int, item: SearchStockInfo?, v: View?) {
        if (item == null) {
            type = max
            //做更多的请求
            getTopicStockData(tips, 20)
        }
    }

    override fun onAddTopicClickItem(pos: Int, item: SearchStockInfo?, view: View) {
        // 点击添加到自选列表
        item?.let { AddTopicStockEvent(it) }?.let { RxBus.getDefault().post(it) }
        toast(R.string.add_topic_successful)
    }

    fun getTopicStockData(str: String, count: Int) {
        val requset = StockSearchRequest(str, 0, count, transactions.createTransaction())
        Cache[IStockNet::class.java]?.search(requset)
            ?.enqueue(Network.IHCallBack<StockSearchResponse>(requset))
        tips = str
    }
    override fun createPresenter(): StockSearchPresenter {
       return StockSearchPresenter(this)
    }

    override fun isDestroyed(): Boolean {
        return false
    }

    override fun createWrapper(): StockSearchVmWarpper {
        return StockSearchVmWarpper(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = generateDataBinding(inflater,container,layout)
        if (viewWrapper != null) {
            viewWrapper.setBinding(dataBinding)
        }
        presenter.fetchData()
        dataBinding.root.setPadding(0, getRootViewFitsSystemWindowsPadding(), 0, 0)
        return dataBinding.root
    }
}