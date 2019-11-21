package com.zhuorui.securities.market.customer.view.kline.stat

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.commonwidget.ZRStockTextView
import com.zhuorui.commonwidget.config.LocalSettingsConfig
import com.zhuorui.commonwidget.config.StocksThemeColor
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.socket.vo.StockTradeDetailData
import com.zhuorui.securities.market.util.MathUtil

class TradeDetailViewAdapter : BaseListAdapter<StockTradeDetailData>() {

    private var stocksThemeColor: StocksThemeColor? = null

    init {
        stocksThemeColor = LocalSettingsConfig.getInstance().stocksThemeColor
    }

    override fun getLayout(viewType: Int): Int {
        return R.layout.item_stock_trans_volume
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View?) :
        ListItemViewHolder<StockTradeDetailData>(v, false, false) {

        @BindView(R2.id.tv_time)
        lateinit var tvTime: TextView

        @BindView(R2.id.tv_price)
        lateinit var tvPrice: ZRStockTextView

        @BindView(R2.id.tv_volume)
        lateinit var tvVolume: ZRStockTextView

        @SuppressLint("SetTextI18n")
        override fun bind(item: StockTradeDetailData?, position: Int) {
            try {
                tvTime.text = item?.time?.substring(8, 10) + ":" + item?.time?.substring(10, 12)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (item != null) {
                tvPrice.setText(MathUtil.rounded3(item.price).toString(), item.diffPreMark)
                tvVolume.setText(MathUtil.convertToUnitString(item.qty), item.diffPreMark)
                tvVolume.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.ic_price_def, 0)

                when (item.diffPreMark) {
                    1 -> {
                        if (stocksThemeColor == StocksThemeColor.redUpGreenDown) {
                            tvVolume.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.ic_price_up_red, 0)
                        } else {
                            tvVolume.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                R.mipmap.ic_price_up_green,
                                0
                            )
                        }
                    }
                    -1 -> {
                        if (stocksThemeColor == StocksThemeColor.redUpGreenDown) {
                            tvVolume.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                R.mipmap.ic_price_down_green,
                                0
                            )
                        } else {
                            tvVolume.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                R.mipmap.ic_price_down_red,
                                0
                            )
                        }
                    }
                }
            }
        }
    }
}