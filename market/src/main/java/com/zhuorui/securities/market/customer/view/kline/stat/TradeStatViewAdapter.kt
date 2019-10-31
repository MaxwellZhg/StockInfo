package com.zhuorui.securities.market.customer.view.kline.stat

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.commonwidget.ZRStockTextView
import com.zhuorui.commonwidget.config.LocalSettingsConfig
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.socket.vo.StockTradeStaData
import com.zhuorui.securities.market.util.MathUtil
import java.math.BigDecimal

class TradeStatViewAdapter : BaseListAdapter<StockTradeStaData>() {

    private var settingsConfig: LocalSettingsConfig? = null

    // 最大百分比
    var maxPercent: BigDecimal = BigDecimal.ONE

    init {
        settingsConfig = LocalSettingsConfig.read()
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

        @BindView(R2.id.buy_qty)
        lateinit var buyQty: View

        @BindView(R2.id.sell_qty)
        lateinit var sellQty: View

        @BindView(R2.id.unchange_qty)
        lateinit var unchangeQty: View

        init {
            settingsConfig?.getUpColor()?.let { buyQty.setBackgroundColor(it) }
            settingsConfig?.getDownColor()?.let { sellQty.setBackgroundColor(it) }
            settingsConfig?.getDefaultColor()?.let { unchangeQty.setBackgroundColor(it) }
        }

        @SuppressLint("SetTextI18n")
        override fun bind(item: StockTradeStaData?, position: Int) {
            when (item?.diffPreMark) {
                0 -> {
                    tvPrice.setText(item.price?.let { MathUtil.rounded3(it).toString() }, 0)
                }
                1 -> {
                    tvPrice.setText(item.price?.let { MathUtil.rounded3(it).toString() }, 1)
                }
                -1 -> {
                    tvPrice.setText(item.price?.let { MathUtil.rounded3(it).toString() }, 2)
                }
            }
            tvVolume.text = item?.todayQty?.let { MathUtil.convertToUnitString(it) }
            // 百分比=该档价格成交总量/总成交量
            if (item?.todayQty != null && item.todayTotalQty != null) {
                tvStat.text = MathUtil.multiply2(
                    MathUtil.divide2(item.todayQty!!, item.todayTotalQty!!),
                    100.toBigDecimal()
                ).toString() + "%"
            }

            // 最大百分比=该价格档主动买入百分比/最大百分比

            // 买入占比量柱长短=（该价格档主动买入股票量/总成交量)/最大百分比

            // 卖出占比量柱=该价格档主动卖出百分比/最大百分比

            // 中性占比量柱长短=该价格档中性盘百分比/最大百分比

        }
    }
}