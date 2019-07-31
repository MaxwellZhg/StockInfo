package com.dycm.applib1.ui

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dycm.applib1.R
import com.dycm.applib1.config.LocalStocksConfig
import com.dycm.applib1.model.SearchStockInfo
import com.dycm.applib1.model.SocketPushStockInfo
import com.dycm.applib1.model.StockTopic
import com.dycm.applib1.net.IStockNet
import com.dycm.applib1.net.request.StockSearchRequset
import com.dycm.applib1.net.response.StockSearchResponse
import com.dycm.applib1.socket.SocketClient
import com.dycm.applib1.socket.StockSubTopic
import com.dycm.applib1.socket.StockUnBindTopicResponse
import com.dycm.applib1.socket.StocksTopicResponse
import com.dycm.base2app.Cache
import com.dycm.base2app.infra.LogInfra
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
class TestFragment : AbsBackFinishNetFragment(), View.OnClickListener, StocksAdapter.OnDeleteClickItemCallback,
    SearchStocksAdapter.OnAddTopicClickItemCallback {

    var mAdapter: StocksAdapter? = null

    override val layout: Int
        get() = R.layout.fragment_test

    override fun init() {
        btn_search.setOnClickListener(this)

        stock_list.layoutManager = LinearLayoutManager(context)
        mAdapter = StocksAdapter()
        mAdapter?.onDeleteCallback = this
        stock_list.adapter = mAdapter

        SocketClient.getInstance().connect()
    }

    override fun onDeleteClickItem(pos: Int, item: SocketPushStockInfo, view: View) {
        // 取消订阅
        val stockTopic = StockTopic(1, item.ts!!, item.code!!, 2)
        SocketClient.getInstance().unBindTopic(stockTopic)
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockUnBindTopicResponse(response: StockUnBindTopicResponse) {
        // 找到当前自选股列表中被取消订阅的item并删除
        val unBindTopic = response.request.body as StockSubTopic
        val stockTopic = unBindTopic.topics?.get(0)
        if (stockTopic != null) {
            for (index in mAdapter?.items?.indices!!) {
                val item = mAdapter?.items!![index]
                if (item.code.equals(stockTopic.code) && item.ts.equals(stockTopic.ts)) {
                    mAdapter?.items?.removeAt(index)
                    mAdapter?.notifyItemRemoved(index)
                    // 删除本地自选股记录
                    LocalStocksConfig.read().remove(item.code, item.ts)
                    break
                }
            }
        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksResponse(response: StocksTopicResponse) {
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
        val requset = StockSearchRequset(edit_search_criteria.text.toString(), 0, 5, transactions.createTransaction())
        Cache[IStockNet::class.java]?.search(requset)
            ?.enqueue(Network.IHCallBack<StockSearchResponse>(requset))
    }

    override fun onAddTopicClickItem(pos: Int, item: SearchStockInfo, view: View) {
        val stockTopic = StockTopic(1, item.ts!!, item.code!!, item.type)
        SocketClient.getInstance().bindTopic(stockTopic)
        // 添加纪录
        if (LocalStocksConfig.read().add(stockTopic)) {
            LogInfra.Log.d(TAG, "添加记录成功")
        } else {
            LogInfra.Log.d(TAG, "添加记录失败")
        }
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
                adapter.onAddTopicClickItemCallback = this
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
