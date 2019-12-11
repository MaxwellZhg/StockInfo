package com.zhuorui.securities.market.ui.adapter

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.commonwidget.ZRStockTextView
import com.zhuorui.commonwidget.config.LocalSettingsConfig
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.config.LocalStocksConfig
import com.zhuorui.securities.market.model.StockConsInfoModel
import com.zhuorui.securities.market.net.response.StockConsInfoResponse
import com.zhuorui.securities.market.util.MathUtil
import com.zhuorui.securities.personal.config.LocalAccountConfig

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:市场成分股adapter
 * */
class MarketPointConsInfoAdapter : BaseListAdapter<StockConsInfoModel>() {
    var onCombineInfoClickListener: OnCombineInfoClickListener? = null
    override fun getLayout(viewType: Int): Int {
        return R.layout.item_market_point_view
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v, false, false)
    }

    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<StockConsInfoModel>(v, needClick, needLongClick) {
        @BindView(R2.id.rl_content)
        lateinit var rl_content: RelativeLayout
        @BindView(R2.id.tv_stock_tile)
        lateinit var tv_stock_tile: TextView
        @BindView(R2.id.stock_code)
        lateinit var stock_code: TextView
        @BindView(R2.id.tv_trade_nums)
        lateinit var tv_trade_nums: TextView
        @BindView(R2.id.tv_consprice_rate)
        lateinit var tv_consprice_rate: TextView
        @BindView(R2.id.tv_newly_price)
        lateinit var tv_newly_price: TextView
        override fun bind(item: StockConsInfoModel?, position: Int) {
            item?.position = position
            tv_stock_tile.text=item?.stockInfo?.name
            tv_stock_tile.invalidate()
            stock_code.text=item?.stockInfo?.code
            // 跌涨幅是否大于0或者等于0
            item?.stockInfo?.diffRate?.let {
                val diffPriceVal = MathUtil.rounded(it).toInt()
                when {
                    diffPriceVal == 0 -> {
                        tv_consprice_rate.text=item?.stockInfo?.diffRate.toString()+"%"
                        tv_consprice_rate.setTextColor(LocalSettingsConfig.getInstance().getDefaultColor())
                        tv_newly_price.text=item?.stockInfo?.last.toString()
                        tv_newly_price.setTextColor(LocalSettingsConfig.getInstance().getDefaultColor())
                        tv_trade_nums.text= item?.stockInfo?.turnover?.let { it1 -> MathUtil.convertToUnitString(it1,1) }
                    }
                    diffPriceVal > 0 -> {
                        tv_consprice_rate.text=item?.stockInfo?.diffRate.toString()+"%"
                        tv_consprice_rate.setTextColor(LocalSettingsConfig.getInstance().getUpColor())
                        tv_newly_price.text=item?.stockInfo?.last.toString()
                        tv_newly_price.setTextColor(LocalSettingsConfig.getInstance().getUpColor())
                        tv_trade_nums.text= item?.stockInfo?.turnover?.let { it1 -> MathUtil.convertToUnitString(it1,1) }
                    }
                    else -> {
                        tv_consprice_rate.text=item?.stockInfo?.diffRate.toString()+"%"
                        tv_consprice_rate.setTextColor(LocalSettingsConfig.getInstance().getDownColor())
                        tv_newly_price.text=item?.stockInfo?.last.toString()
                        tv_newly_price.setTextColor(LocalSettingsConfig.getInstance().getDownColor())
                        tv_trade_nums.text= item?.stockInfo?.turnover?.let { it1 -> MathUtil.convertToUnitString(it1,1) }
                    }
                }
            }
        }

        init {
            rl_content.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v == rl_content) {
                onCombineInfoClickListener?.onCombineClick()
            } else {
                super.onClick(v)
            }
        }
    }

    interface OnCombineInfoClickListener{
        fun onCombineClick()
    }

}