package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.event.AddTopicStockEvent
import com.zhuorui.securities.market.event.NotifyStockCountEvent
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.model.StockTsEnum
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.RecommendStocklistRequest
import com.zhuorui.securities.market.net.response.RecommendStocklistResponse
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicPriceResponse
import com.zhuorui.securities.market.ui.view.TopicStockListView
import com.zhuorui.securities.market.ui.viewmodel.TopicStockListViewModel
import com.zhuorui.securities.market.util.MathUtil

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/19 17:13
 *    desc   :
 */
@Suppress("NAME_SHADOWING")
class TopicStockListPresenter : AbsNetPresenter<TopicStockListView, TopicStockListViewModel>() {

    private var ts: StockTsEnum? = null

    override fun init() {
        super.init()
        view?.init()
    }

    fun setType(type: StockTsEnum?) {
        ts = type
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.datas?.observe(it,
                androidx.lifecycle.Observer<List<StockMarketInfo>> { t ->
                    RxBus.getDefault().post(NotifyStockCountEvent(ts, t.size))
                    view?.notifyDataSetChanged(t)
                })
        }
    }

    /**
     * 加载推荐自选股列表
     */
    fun requestStocks(currentPage: Int, pageSize: Int) {
        val request = RecommendStocklistRequest(
            if (ts == StockTsEnum.HS) StockTsEnum.SH.name + "," + StockTsEnum.SZ.name else ts?.name,
            currentPage,
            pageSize,
            transactions.createTransaction()
        )
        Cache[IStockNet::class.java]?.list(request)
            ?.enqueue(Network.IHCallBack<RecommendStocklistResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onRecommendStocklistResponse(response: RecommendStocklistResponse) {
        if (!transactions.isMyTransaction(response)) return
        val datas = response.data?.datas
        if (datas.isNullOrEmpty()) return
        viewModel?.datas?.value = datas

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

        val datas = viewModel?.datas?.value ?: return

        val stockPriceDatas = response.body

        for (index in datas.indices) {
            val item = datas[index]
            for (sub in stockPriceDatas) {
                if (item.ts == sub.ts && item.code == sub.code) {
                    // 更新数据
                    val item = datas[index]
                    item.price = sub.price!!
                    item.diffRate =
                        MathUtil.division((sub.price!! - sub.openPrice!!) * 100, sub.openPrice!!)
                    view?.notifyItemChanged(index)
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
        val datas = viewModel?.datas?.value ?: return

        for (item in datas) {
            if (item.ts.equals(event.stock.ts) && item.code.equals(event.stock.ts)) return
        }

        // 发起订阅价格
        val stockTopic = event.stock.ts?.let {
            event.stock.code?.let { it1 ->
                event.stock.type?.let { it2 ->
                    StockTopic(
                        StockTopicDataTypeEnum.price,
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

        datas.add(stock)

        view?.notifyItemInserted(datas.size - 1)
        RxBus.getDefault().post(NotifyStockCountEvent(ts, datas.size))
    }

    fun onStickyOnTop(item: StockMarketInfo?) {
        val datas = viewModel?.datas?.value ?: return
        datas.remove(item)
        item?.let { datas.add(0, it) }
        view?.notifyDataSetChanged(datas)
    }

    fun onDelete(item: StockMarketInfo?) {
        val datas = viewModel?.datas?.value ?: return
        datas.remove(item)
        view?.notifyDataSetChanged(datas)
        // 取消订阅
        val stockTopic = item?.ts?.let {
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
        RxBus.getDefault().post(NotifyStockCountEvent(ts, datas.size))
    }
}