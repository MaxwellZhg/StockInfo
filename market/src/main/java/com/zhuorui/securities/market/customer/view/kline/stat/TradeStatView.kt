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
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicTradeStaResponse
import com.zhuorui.securities.market.socket.vo.StockTradeStaData
import com.zhuorui.securities.market.util.MathUtil
import java.math.RoundingMode
import kotlin.random.Random

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/10/30 11:30
 * desc   : 展示股票成交统计
 */
@SuppressLint("ViewConstructor")
class TradeStatView(context: Context, val ts: String, val code: String, val type: Int) :
    LinearLayout(context) {

    private var recyclerView: RecyclerView? = null
    private var stockTopic: StockTopic? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_stock_trade_detail, this, true)

        RxBus.getDefault().register(this)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.addItemDecoration(LinearSpacingItemDecoration())
        val adapter = TradeStatViewAdapter()
        // TODO 测试数据
        val list = ArrayList<StockTradeStaData>()
        // 最大百分比
        var maxPercent = 0.00
        for (index in 0..19) {
            var diffPreMark = 0
            if (index != 0) {
                diffPreMark = if (index > 10) {
                    1
                } else {
                    -1
                }
            }
            val item = StockTradeStaData()
            item.diffPreMark = diffPreMark
            item.price = Random.nextDouble(1.000, 100.000).toBigDecimal()

            item.todayBuyQty = Random.nextInt(100, 100000).toBigDecimal()
            item.todaySellQty = Random.nextInt(100, 100000 - item.todayBuyQty!!.toInt()).toBigDecimal()
            item.todayUnchangeQty =
                Random.nextInt(100, 100000 - item.todayBuyQty!!.toInt() - item.todaySellQty!!.toInt()).toBigDecimal()

            item.todayQty = Random.nextInt(100, 100000).toBigDecimal()
            item.todayTotalQty = 1000000.toBigDecimal()

            // 该价总成交百分比=该价当天成交量/该股票当天总成交量
            maxPercent = MathUtil.multiply2(
                item.todayQty?.divide(item.todayTotalQty, 4, RoundingMode.DOWN)!!,
                100.toBigDecimal()
            ).toDouble().coerceAtLeast(maxPercent)

            list.add(item)
        }
        adapter.maxPercent = maxPercent.toBigDecimal()
        adapter.items = list
        recyclerView?.adapter = adapter

        //  // 发起订阅
//          stockTopic = StockTopic(StockTopicDataTypeEnum.STOCK_TRADE_STA, ts, code, type)
        //  SocketClient.getInstance().bindTopic(stockTopic)
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
     * 订阅自选股逐笔成交数据推送回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicTradeStaResponse(response: StocksTopicTradeStaResponse) {
        if (response.body != null && recyclerView?.adapter != null) {
            val adapter = (recyclerView?.adapter as TradeStatViewAdapter)
            if (adapter.items.isNullOrEmpty()) {
                val items = ArrayList<StockTradeStaData>()
                adapter.items = items
            } else {
                adapter.addItem(response.body)
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