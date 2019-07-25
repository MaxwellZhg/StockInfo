package com.dycm.applib1.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.dycm.applib1.R
import com.dycm.applib1.model.StockInfo
import com.dycm.applib1.socket.SocketClient
import com.dycm.applib1.socket.StocksResponse
import com.dycm.base2app.infra.LogInfra
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
class TestFragment : AbsBackFinishEventFragment() {

    var mAdapter: StocksAdapter? = null

    override val layout: Int
        get() = R.layout.fragment_test

    override fun init() {
        recyclerview.layoutManager = LinearLayoutManager(context)
        mAdapter = StocksAdapter()
        recyclerview.adapter = mAdapter

        SocketClient.getInstance().newClient()
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksResponse(response: StocksResponse) {
        if (response.body.isNotEmpty()) {
            LogInfra.Log.d(TAG, "onStocksResponse: " + response.body.size)

            val stocksList = ArrayList<StockInfo>()
            if (mAdapter?.items?.isNotEmpty()!!) {
                for (item in mAdapter!!.items) {
                    val stockInfo = response.body[item.id]
                    if (item.updateData(stockInfo)) {
                        response.body.remove(item.id)
                    }
                }
            }

            for (id in response.body.keys) {
                val stockInfo = response.body[id]
                stockInfo?.id = id
                stockInfo?.let { stocksList.add(it) }
            }

            mAdapter?.addItems(stocksList)
        }
    }

    override fun onDetach() {
        super.onDetach()
        SocketClient.getInstance().destroy()
    }
}