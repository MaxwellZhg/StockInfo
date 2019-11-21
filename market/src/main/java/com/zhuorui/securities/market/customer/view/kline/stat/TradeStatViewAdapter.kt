package com.zhuorui.securities.market.customer.view.kline.stat

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.commonwidget.ZRStockTextView
import com.zhuorui.commonwidget.config.LocalSettingsConfig
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.socket.vo.StockTradeStaData
import com.zhuorui.securities.market.util.MarketUtil
import com.zhuorui.securities.market.util.MathUtil
import java.math.BigDecimal
import java.math.RoundingMode

class TradeStatViewAdapter : BaseListAdapter<StockTradeStaData>() {

    private var settingsConfig: LocalSettingsConfig? = null

    // 最大百分比
    var maxPercent: BigDecimal = BigDecimal.ONE

    init {
        settingsConfig = LocalSettingsConfig.getInstance()
    }

    override fun getLayout(viewType: Int): Int {
        return R.layout.item_stock_trade_stat
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View?) :
        ListItemViewHolder<StockTradeStaData>(v, false, false) {

        @BindView(R2.id.tv_price)
        lateinit var tvPrice: ZRStockTextView

        @BindView(R2.id.tv_volume)
        lateinit var tvVolume: TextView

        @BindView(R2.id.tv_stat)
        lateinit var tvStat: TextView

        @BindView(R2.id.ll_stat)
        lateinit var llStat: View

        @BindView(R2.id.buy_qty)
        lateinit var buyQty: View

        @BindView(R2.id.sell_qty)
        lateinit var sellQty: View

        @BindView(R2.id.unchange_qty)
        lateinit var unchangeQty: View

        @BindView(R2.id.diff_mark)
        lateinit var diffMark: View

        // ResUtil.getDimensionDp2Px的参数，必须对应llStat在xml中的大小
        private var width = ResUtil.getDimensionDp2Px(31.5f)

        init {
            settingsConfig?.getUpColor()?.let { buyQty.setBackgroundColor(it) }
            settingsConfig?.getDownColor()?.let { sellQty.setBackgroundColor(it) }
            settingsConfig?.getDefaultColor()?.let { unchangeQty.setBackgroundColor(it) }
        }

        @SuppressLint("SetTextI18n")
        override fun bind(item: StockTradeStaData?, position: Int) {
            tvPrice.setText(item?.price?.let { MathUtil.rounded3(it).toString() }, item?.diffPreMark!!)
            if (position == itemCount - 1) {
                if (item.diffPreMark == 1) {
                    // 闪涨
                    MarketUtil.showUpDownAnim(null, diffMark, true)
                } else if (item.diffPreMark == -1) {
                    // 闪涨
                    MarketUtil.showUpDownAnim(null, diffMark, false)
                }
            }
            tvVolume.text = item?.todayQty?.let { MathUtil.convertToUnitString(it) }
            // 百分比=该档价格成交总量/总成交量
            tvStat.text = MathUtil.multiply2(
                item?.todayQty?.divide(item.todayTotalQty, 4, RoundingMode.DOWN)!!,
                100.toBigDecimal()
            ).toString() + "%"
            // 买入占比量柱长短=（该价格档主动买入股票量/总成交量)/最大百分比
            val buyPercent = MathUtil.divide2(
                MathUtil.multiply2(
                    item.todayBuyQty?.divide(item.todayTotalQty, 4, RoundingMode.DOWN)!!,
                    100.toBigDecimal()
                ), maxPercent
            )
            (buyQty.layoutParams as LinearLayout.LayoutParams).width =
                (width * buyPercent.toDouble()).toInt()
            // 卖出占比量柱=该价格档主动卖出百分比/最大百分比
            val sellPercent = MathUtil.divide2(
                MathUtil.multiply2(
                    item.todaySellQty?.divide(item.todayTotalQty, 4, RoundingMode.DOWN)!!,
                    100.toBigDecimal()
                ), maxPercent
            )
            (sellQty.layoutParams as LinearLayout.LayoutParams).width =
                (width * sellPercent.toDouble()).toInt()
            // 中性占比量柱长短=该价格档中性盘百分比/最大百分比
            val unchangePercent =
                MathUtil.divide2(
                    MathUtil.multiply2(
                        item.todayUnchangeQty?.divide(item.todayTotalQty, 4, RoundingMode.DOWN)!!,
                        100.toBigDecimal()
                    ), maxPercent
                )
            (unchangeQty.layoutParams as LinearLayout.LayoutParams).width =
                (width * unchangePercent.toDouble()).toInt()
        }
    }
}