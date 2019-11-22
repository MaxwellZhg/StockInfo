package com.zhuorui.securities.market.customer.view.kline.stat

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zhuorui.commonwidget.model.Observer
import com.zhuorui.commonwidget.model.Subject
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.manager.StockTradeStaDataManager

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/10/30 11:30
 * desc   : 展示股票成交统计
 */
@SuppressLint("ViewConstructor")
class TradeStatView(context: Context, val ts: String, val code: String, val type: Int) :
    LinearLayout(context), Observer {


    private var recyclerView: RecyclerView? = null
    private var adapter: TradeStatViewAdapter? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_stock_trade_detail, this, true)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.addItemDecoration(LinearSpacingItemDecoration())
        adapter = TradeStatViewAdapter()
        recyclerView?.adapter = adapter

        val manager = StockTradeStaDataManager.getInstance(ts, code, type)
        // 从缓存中取数据
        adapter?.items = manager.tradeDatas
        // 添加监听
        manager.registerObserver(this)
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

    override fun update(subject: Subject<*>?) {
        if (subject is StockTradeStaDataManager) {
            adapter?.maxPercent = subject.maxPercent
            if (adapter?.items.isNullOrEmpty()) {
                adapter?.items = subject.tradeDatas
            } else {
                adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // 删除监听
        StockTradeStaDataManager.getInstance(ts, code, type).removeObserver(this)
    }

//    private fun testData(){
//        // TODO 测试数据
//        val list = ArrayList<StockTradeStaData>()
//        // 最大百分比
//        var maxPercent = 0.00
//        for (index in 0..19) {
//            var diffPreMark = 0
//            if (index != 0) {
//                diffPreMark = if (index > 10) {
//                    1
//                } else {
//                    -1
//                }
//            }
//            val item = StockTradeStaData()
//            item.diffPreMark = diffPreMark
//            item.price = Random.nextDouble(1.000, 100.000).toBigDecimal()
//
//            item.todayBuyQty = Random.nextInt(100, 100000).toBigDecimal()
//            item.todaySellQty = Random.nextInt(100, 100000 - item.todayBuyQty!!.toInt()).toBigDecimal()
//            item.todayUnchangeQty =
//                Random.nextInt(100, 100000 - item.todayBuyQty!!.toInt() - item.todaySellQty!!.toInt()).toBigDecimal()
//
//            item.todayQty = Random.nextInt(100, 100000).toBigDecimal()
//            item.todayTotalQty = 1000000.toBigDecimal()
//
//            // 该价总成交百分比=该价当天成交量/该股票当天总成交量
//            maxPercent = MathUtil.multiply2(
//                item.todayQty?.divide(item.todayTotalQty, 4, RoundingMode.DOWN)!!,
//                100.toBigDecimal()
//            ).toDouble().coerceAtLeast(maxPercent)
//
//            list.add(item)
//        }
//        adapter.maxPercent = maxPercent.toBigDecimal()
//        adapter.items = list
//    }
}