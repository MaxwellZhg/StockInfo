package com.dycm.applib1.ui

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dycm.applib1.R
import com.dycm.applib1.net.IStockNet
import com.dycm.applib1.net.request.StockSearchRequset
import com.dycm.applib1.net.response.StockSearchResponse
import com.dycm.applib1.socket.SocketClient
import com.dycm.applib1.socket.StocksResponse
import com.dycm.base2app.Cache
import com.dycm.base2app.network.Network
import com.dycm.base2app.rxbus.EventThread
import com.dycm.base2app.rxbus.RxSubscribe
import com.dycm.base2app.ui.fragment.AbsBackFinishEventFragment
import kotlinx.android.synthetic.main.fragment_test.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/18 10:32
 *    desc   :
 */
class TestFragment : AbsBackFinishEventFragment(), View.OnClickListener {

    var mAdapter: StocksAdapter? = null

    override val layout: Int
        get() = R.layout.fragment_test

    override fun init() {
        btn_search.setOnClickListener(this)

        recyclerview.layoutManager = LinearLayoutManager(context)
        mAdapter = StocksAdapter()
        recyclerview.adapter = mAdapter

        SocketClient.getInstance().newClient()
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
        val requset = StockSearchRequset(edit_search_criteria.text.toString(), 1, 5, transactions.createTransaction())
        Cache[IStockNet::class.java]?.search(edit_search_criteria.text.toString(), 1, 5)?.enqueue(Network.IHCallBack<StockSearchResponse>(requset))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockSearchResponse(response: StockSearchResponse) {

    }

    override fun onDetach() {
        super.onDetach()
        SocketClient.getInstance().destroy()
    }
}
