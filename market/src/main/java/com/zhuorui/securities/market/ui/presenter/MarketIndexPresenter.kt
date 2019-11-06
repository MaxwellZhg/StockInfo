package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsEventPresenter
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicPriceResponse
import com.zhuorui.securities.market.ui.view.MarketIndexView
import com.zhuorui.securities.market.ui.viewmodel.MarketIndexViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-11-04 17:38
 *    desc   :
 */
class MarketIndexPresenter : AbsEventPresenter<MarketIndexView, MarketIndexViewModel>() {

    fun getData() {
        val datas = getTestData()
        val titles = arrayListOf<String>()
        val codes = arrayListOf<String>()
        val tss = arrayListOf<String>()
        datas.forEachIndexed { index, item ->
            run {
                titles.add(item.name.toString())
                codes.add(item.code.toString())
                tss.add(item.ts.toString())
            }
        }
        viewModel?.titles = titles.toTypedArray()
        viewModel?.codes = codes.toTypedArray()
        viewModel?.tss= tss.toTypedArray()
        view?.onUpdata()
        topicPrice(datas)
    }

    private fun getTestData(): List<SearchStockInfo> {
        val titles = arrayOf("恒生指数", "国企指数", "红筹指数")
        val codes = arrayOf("800000", "800100", "800151")
        val datas = mutableListOf<SearchStockInfo>()
        for (i in 0 until titles.size) {
            val data = SearchStockInfo()
            data.type = 1
            data.ts = "HK"
            data.code = codes[i]
            data.name = titles[i]
            data.tsCode = data.code + "." + data.ts
            datas.add(data)
        }
        return datas.toList()
    }

    /**
     *  发起价格订阅
     */
    private fun topicPrice(datas: List<SearchStockInfo>): Boolean {
        for (item in datas) {
            val stockTopic = StockTopic(StockTopicDataTypeEnum.STOCK_PRICE, item.ts!!, item.code!!, item.type!!)
            SocketClient.getInstance().bindTopic(stockTopic)
        }
        return true
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    @Synchronized
    fun onStocksTopicPriceResponse(response: StocksTopicPriceResponse) {

    }

    fun getCodes(): Array<String>? {
        return viewModel!!.codes
    }

    fun getTitles(): Array<String>? {
        return viewModel!!.titles
    }

    fun getTss(): Array<String>? {
        return viewModel!!.tss
    }
}