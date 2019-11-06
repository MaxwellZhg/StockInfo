package com.zhuorui.securities.market.customer.view.kline.stat

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicTradeResponse
import com.zhuorui.securities.market.socket.response.GetStockTradeResponse
import com.zhuorui.securities.market.socket.vo.StockTradeDetailData
import kotlin.random.Random

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/10/30 11:30
 * desc   : 展示股票成交明细
 */
@SuppressLint("ViewConstructor")
class TradeDetailView(context: Context, val ts: String, val code: String, val type: Int) :
    LinearLayout(context) {

    private var recyclerView: RecyclerView? = null
    private var adapter: TradeDetailViewAdapter? = null
    private var requestIds = java.util.ArrayList<String>()
    private var stockTopic: StockTopic? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_stock_trade_detail, this, true)

        RxBus.getDefault().register(this)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.addItemDecoration(LinearSpacingItemDecoration())
        adapter = TradeDetailViewAdapter()
        // TODO 测试数据
        val list = ArrayList<StockTradeDetailData>()
        for (index in 0..30) {
            var diffPreMark = 0
            if (index != 0) {
                diffPreMark = if (index > 10) {
                    1
                } else {
                    -1
                }
            }
            list.add(
                StockTradeDetailData(
                    "",
                    diffPreMark,
                    1,
                    "20191030161528333",
                    Random.nextDouble(1.000, 100.000).toBigDecimal(),
                    Random.nextLong(1, 10000).toBigDecimal(),
                    "N",
                    "11"
                )
            )
        }
        adapter?.items = list
        recyclerView?.adapter = adapter

//        // 拉取数据
//        val requestId =
//            SocketClient.getInstance().postRequest(GetStockTradeRequestBody(ts, code), SocketApi.GET_STOCK_TRADE)
//        requestIds.add(requestId)
    }

    inner class LinearSpacingItemDecoration : RecyclerView.ItemDecoration() {
        private val spacing = ResUtil.getDimensionDp2Px(6.6f)
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            //这里是关键，需要根据你有几列来判断
            val position = parent.getChildAdapterPosition(view) // item position
            if (position == 0) {
                outRect.top = ResUtil.getDimensionDp2Px(2f)
            } else if (position > 0) { // top edge
                outRect.top = spacing
            }
        }
    }

    /**
     * 查询选股逐笔成交数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetStockTradeResponse(response: GetStockTradeResponse) {
        if (requestIds.remove(response.respId)) {
            adapter?.items = response.data

            // 发起订阅
            stockTopic = StockTopic(StockTopicDataTypeEnum.STOCK_TRADE, ts, code, type)
            SocketClient.getInstance().bindTopic(stockTopic)
        }
    }

    /**
     * 订阅自选股逐笔成交数据推送回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicTradeResponse(response: StocksTopicTradeResponse) {
        if (response.body != null) {
            if (adapter?.items.isNullOrEmpty()) {
                val items = ArrayList<StockTradeDetailData>()
                adapter?.items = items
            } else {
                adapter?.addItem(response.body)
            }
        }
    }

    @RxSubscribe(observeOnThread = EventThread.NEW)
    fun onSocketAuthCompleteEvent(event: SocketAuthCompleteEvent) {
        // 恢复订阅
        if (stockTopic != null) {
            SocketClient.getInstance().bindTopic(stockTopic)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().unregister(this)
        }
        // 取消订阅
        if (stockTopic != null) {
            SocketClient.getInstance().unBindTopic(stockTopic)
        }
    }
}