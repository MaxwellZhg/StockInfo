package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.event.AddTopicStockEvent
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.model.StockTsEnum
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.RecommendStocklistRequest
import com.zhuorui.securities.market.net.response.RecommendStocklistResponse
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicPriceResponse
import com.zhuorui.securities.market.ui.TopicStocksAdapter
import com.zhuorui.securities.market.ui.view.TopicStockListFragmentView
import com.zhuorui.securities.market.ui.viewmodel.TopicStockListViewModel
import com.zhuorui.securities.market.util.MathUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/19 17:13
 *    desc   :
 */
class TopicStockListFragmentPresenter : AbsNetPresenter<TopicStockListFragmentView, TopicStockListViewModel>() {

    private var ts: StockTsEnum? = null
    private var disposables = LinkedList<Disposable>()

    override fun init() {
        super.init()
        view?.init()
    }

    fun setType(type: StockTsEnum?) {
        ts = type
    }

    /**
     * 加载推荐自选股列表
     */
    fun requestStocks(currentPage: Int, pageSize: Int) {
        val request = RecommendStocklistRequest(ts, currentPage, pageSize, transactions.createTransaction())
        Cache[IStockNet::class.java]?.list(request)
            ?.enqueue(Network.IHCallBack<RecommendStocklistResponse>(request))
    }

    fun getAdapter(): TopicStocksAdapter? {
        if (viewModel?.adapter?.value == null) {
            viewModel?.adapter?.value = TopicStocksAdapter()
        }
        return viewModel?.adapter?.value
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onRecommendStocklistResponse(response: RecommendStocklistResponse) {
        if (!transactions.isMyTransaction(response)) return
        val datas = response.data?.datas
        if (datas.isNullOrEmpty()) return
        viewModel?.adapter?.value?.addItems(datas)

        // 发起价格订阅
        for (item in datas) {
            val stockTopic = item.ts?.let {
                item.code?.let { it1 ->
                    item.type?.let { it2 ->
                        StockTopic(
                            StockTopicDataTypeEnum.price, it,
                            it1, it2
                        )
                    }
                }
            }
            SocketClient.getInstance().bindTopic(stockTopic)
        }
    }

    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onStocksTopicPriceResponse(response: StocksTopicPriceResponse) {

        val adapter = viewModel?.adapter?.value ?: return
        val adapterData = adapter.items
        if (adapterData?.isNullOrEmpty()!!) return

        val stockPriceDatas = response.body

        for (index in adapter.items?.indices!!) {
            val item = adapter.items!![index]
            for (sub in stockPriceDatas) {
                if (item.ts == sub.ts && item.code == sub.code) {
                    // 更新数据
                    val item = adapter.items!![index]
                    item.price = sub.price!!
                    item.diffRate =
                        MathUtil.division((sub.price!! - sub.openPrice!!) * 100, sub.openPrice!!)
//                    _mActivity?.runOnUiThread { mAdapter?.notifyItemChanged(index) }
                    break
                }
            }
        }
    }

    /**
     * 添加自选股
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onAddTopicStockEvent(event: AddTopicStockEvent) {
        if (ts != null && !event.stock.ts.equals(ts?.name)) return

        val adapter = viewModel?.adapter?.value
        for (item in adapter?.items!!) {
            if (item.ts.equals(event.stock.ts) && item.code.equals(event.stock.ts)) return
        }

        // 发起订阅价格
        val stockTopic = event.stock.ts?.let {
            event.stock.code?.let { it1 ->
                event.stock.type?.let { it2 ->
                    StockTopic(
                        StockTopicDataTypeEnum.market,
                        it, it1, it2
                    )
                }
            }
        }
        SocketClient.getInstance().bindTopic(stockTopic)

        // 显示新添加的自选股
        val stock = StockMarketInfo()
        stock.ts = event.stock.ts
        stock.code = event.stock.code
        stock.name = event.stock.name
        stock.type = event.stock.type
        stock.tsCode = event.stock.tsCode

        adapter.items.add(stock)
//        val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
//            adapter.addItem(stock)
//            emitter.onNext(true)
//            emitter.onComplete()
//        }).subscribeOn(AndroidSchedulers.mainThread())
//            .subscribe()
//        disposables.add(disposable)
    }

    override fun destroy() {
        super.destroy()

        // 释放disposable
        if (disposables.isNullOrEmpty()) return
        for (disposable in disposables) {
            disposable.dispose()
        }
        disposables.clear()
    }
}