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
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.manager.StockTradeDetailDataManager

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/10/30 11:30
 * desc   : 展示股票成交明细
 */
@SuppressLint("ViewConstructor")
class TradeDetailView(context: Context, val ts: String, val code: String, val type: Int) :
    LinearLayout(context), Observer {

    private var recyclerView: RecyclerView? = null
    private var adapter: TradeDetailViewAdapter? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_stock_trade_detail, this, true)

        RxBus.getDefault().register(this)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.addItemDecoration(LinearSpacingItemDecoration())
        adapter = TradeDetailViewAdapter()
        recyclerView?.adapter = adapter

        val manager = StockTradeDetailDataManager.getInstance(ts, code, type)
        // 从缓存中取数据
        adapter?.items = manager.tradeDatas
        if (adapter?.itemCount!! > 0) {
            // 让列表滚动到底部
            recyclerView?.scrollToPosition(adapter?.itemCount!! - 1)
        }
        // 添加监听
        manager.registerObserver(this)
    }

    private fun testData(){
        //        // TODO 测试数据
//        val list = ArrayList<StockTradeDetailData>()
//        for (index in 0..30) {
//            var diffPreMark = 0
//            if (index != 0) {
//                diffPreMark = if (index > 10) {
//                    1
//                } else {
//                    -1
//                }
//            }
//            list.add(
//                StockTradeDetailData(
//                    "",
//                    diffPreMark,
//                    1,
//                    "20191030161528333",
//                    Random.nextDouble(1.000, 100.000).toBigDecimal(),
//                    Random.nextLong(1, 10000).toBigDecimal(),
//                    "N",
//                    "11"
//                )
//            )
//        }
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
        if (subject is StockTradeDetailDataManager) {
            if (adapter?.items.isNullOrEmpty()) {
                adapter?.items = subject.tradeDatas
            } else {
                adapter?.notifyDataSetChanged()
            }
            if (adapter?.itemCount!! > 0) {
                // 让列表滚动到底部
                recyclerView?.scrollToPosition(adapter?.itemCount!! - 1)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // 删除监听
        StockTradeDetailDataManager.getInstance(ts, code, type).removeObserver(this)
    }
}