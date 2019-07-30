package com.dycm.applib1.ui

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dycm.applib1.R
import com.dycm.applib1.model.SocketStockTopic
import com.dycm.applib1.net.IStockNet
import com.dycm.applib1.net.request.StockSearchRequset
import com.dycm.applib1.net.response.StockSearchResponse
import com.dycm.applib1.socket.SocketClient
import com.dycm.applib1.socket.StocksResponse
import com.dycm.base2app.Cache
import com.dycm.base2app.network.Network
import com.dycm.base2app.rxbus.EventThread
import com.dycm.base2app.rxbus.RxSubscribe
import com.dycm.base2app.ui.fragment.AbsBackFinishNetFragment
import kotlinx.android.synthetic.main.fragment_test.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/18 10:32
 *    desc   :
 */
class TestFragment : AbsBackFinishNetFragment(), View.OnClickListener {

    var mAdapter: StocksAdapter? = null

    override val layout: Int
        get() = R.layout.fragment_test

    override fun init() {
        btn_bind.setOnClickListener(this)
        btn_unbind.setOnClickListener(this)
        btn_search.setOnClickListener(this)

        stock_list.layoutManager = LinearLayoutManager(context)
        mAdapter = StocksAdapter()
        stock_list.adapter = mAdapter

        SocketClient.getInstance().connect()
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksResponse(response: StocksResponse) {
        if (response.body?.data != null) {
            if (mAdapter?.items?.isNotEmpty()!!) {
                for (item in mAdapter!!.items) {
                    if (item.code.equals(response.body!!.code)) {
                        item.data?.updateData(response.body)
                        mAdapter?.notifyDataSetChanged()
                        return
                    }
                }
            }

            mAdapter?.addItem(response.body)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_bind -> {
                onClickBind(v)
            }
            R.id.btn_unbind -> {
                onClickUnBind(v)
            }
            else -> {
                onClickSearch(v)
            }
        }
    }

    fun onClickBind(v: View?) {
        val stockTopic1 = SocketStockTopic(1, "SH", "600519", 2)
        SocketClient.getInstance().bindTopic(stockTopic1)
    }

    fun onClickUnBind(v: View?) {
        val stockTopic1 = SocketStockTopic(1, "SH", "600519", 2)
        SocketClient.getInstance().unBindTopic(stockTopic1)
    }

    fun onClickSearch(v: View?) {
        val requset = StockSearchRequset(edit_search_criteria.text.toString(), 0, 5, transactions.createTransaction())
        Cache[IStockNet::class.java]?.search(requset)
            ?.enqueue(Network.IHCallBack<StockSearchResponse>(requset))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockSearchResponse(response: StockSearchResponse) {
        if (response.data.datas.isNotEmpty()) {
            // 显示搜索列表
            search_list.visibility = View.VISIBLE
            // 设置数据
            if (search_list.adapter == null) {
                search_list.layoutManager = LinearLayoutManager(context)
                val adapter = SearchStocksAdapter()
                search_list.adapter = adapter
                adapter.addItems(response.data.datas)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        SocketClient.getInstance().destroy()
    }
}
