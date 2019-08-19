package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.event.AddTopicStockEvent
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.StockSearchRequest
import com.zhuorui.securities.market.net.response.StockSearchResponse
import com.zhuorui.securities.market.ui.view.StockSearchFragmentView
import com.zhuorui.securities.market.ui.viewmodel.StockSearchViewModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/19 16:16
 *    desc   :
 */
class StockSearchFragmentPresenter : AbsNetPresenter<StockSearchFragmentView, StockSearchViewModel>() {

    override fun init() {
        super.init()
        view?.init()
    }

    fun getTopicStockData(keyWord: String, count: Int) {
        val requset = StockSearchRequest(keyWord, 0, count, transactions.createTransaction())
        Cache[IStockNet::class.java]?.search(requset)
            ?.enqueue(Network.IHCallBack<StockSearchResponse>(requset))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockSearchResponse(response: StockSearchResponse) {
        view?.onStockSearchResponse(response)
    }

    fun onAddTopicClickItem(item: SearchStockInfo?) {
        // 点击添加到自选列表
        item?.let { AddTopicStockEvent(it) }?.let { RxBus.getDefault().post(it) }
        toast(R.string.add_topic_successful)
    }
}