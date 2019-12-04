package com.zhuorui.securities.market.ui.adapter

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.commonwidget.ZRStockStatusButton
import com.zhuorui.commonwidget.ZRStockTextView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.StockTsEnum
import com.zhuorui.securities.market.model.TopicStockModel
import com.zhuorui.securities.market.util.MarketUtil
import com.zhuorui.securities.market.util.MathUtil

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/7
 * Desc: 自选股列表适配器
 */
class TopicStocksAdapter : BaseListAdapter<TopicStockModel>() {

    private val default = 0x00
    private val bottom = 0x01

    override fun getItemCount(): Int {
        if (items.isNullOrEmpty()) {
            return 0
        }
        return items.size + 1
    }

    override fun getItem(position: Int): TopicStockModel? {
        if (items.isNullOrEmpty() || position > items.size || position == items.size) return null
        return super.getItem(position)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> {
                bottom
            }
            else -> {
                default
            }
        }
    }

    override fun getLayout(viewType: Int): Int {
        return when (viewType) {
            default -> R.layout.item_topic_stock
            else -> {
                R.layout.item_add_topic_stock
            }
        }
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            default -> ViewHolderdefalt(v, true, true)
            else -> {
                ViewHolderBottom(v, true, false)
            }
        }
    }

    inner class ViewHolderdefalt(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<TopicStockModel>(v, needClick, needLongClick) {

        @BindView(R2.id.tv_stock_tile)
        lateinit var tv_stock_tile: TextView
        @BindView(R2.id.iv_stock_ts)
        lateinit var iv_stock_ts: ImageView
        @BindView(R2.id.stock_code)
        lateinit var stock_code: TextView
        @BindView(R2.id.stock_up_down)
        lateinit var stock_up_down: ZRStockStatusButton
        @BindView(R2.id.tv_price)
        lateinit var tv_price: ZRStockTextView
        @BindView(R2.id.diff_mark)
        lateinit var diff_mark: View

        var upDownAnim: ObjectAnimator? = null

        @SuppressLint("SetTextI18n")
        override fun bind(item: TopicStockModel?, position: Int) {
            item?.position = position
            val stockInfo = item?.stockInfo
            if (stockInfo?.pctTag != null) {
                LogInfra.Log.d(
                    TAG,
                    TopicStocksAdapter@ this.toString() + " bind item " + item.stockInfo!!.code + " position " + position + " pctTag " + stockInfo.pctTag
                )
                when (stockInfo.pctTag) {
                    1 -> {
                        // 闪涨
                        upDownAnim = MarketUtil.showUpDownAnim(upDownAnim, diff_mark, true)
                    }
                    -1 -> {
                        // 闪跌
                        upDownAnim = MarketUtil.showUpDownAnim(upDownAnim, diff_mark, false)
                    }
                }
                stockInfo.pctTag = 0
            }

            if (item?.longClick != null && item.longClick) {
                itemView.setBackgroundResource(R.color.color_312E40_85)
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT)
            }

            tv_stock_tile.text = stockInfo?.name
            tv_stock_tile.invalidate()
            stock_code.text = stockInfo?.code
            when (stockInfo?.ts) {
                StockTsEnum.HK.name -> {
                    iv_stock_ts.setImageResource(R.mipmap.ic_ts_hk)
                }
                StockTsEnum.SH.name -> {
                    iv_stock_ts.setImageResource(R.mipmap.ic_ts_sh)
                }
                StockTsEnum.SZ.name -> {
                    iv_stock_ts.setImageResource(R.mipmap.ic_ts_sz)
                }
            }


            when (stockInfo?.suspension) {
                null -> {
                    // 无状态
                    tv_price.setText("--", 0)
                    stock_up_down.setUpDown(0)
                    stock_up_down.text = "--"
                }
                2 -> {
                    // 停牌状态
                    tv_price.setText("--", 0)
                    stock_up_down.setUpDown(0)
                    stock_up_down.text = ResUtil.getString(R.string.suspension)
                }
                else -> {
                    // 正常状态
                    // 跌涨幅是否大于0或者等于0
                    stockInfo.diffRate?.let {
                        val diffRate = MathUtil.rounded(it).toInt()
                        when {
                            diffRate == 0 -> {
                                tv_price.setText(stockInfo.last.toString(), 0)
                                stock_up_down.setUpDown(0)
                                stock_up_down.text = stockInfo.diffRate.toString() + "%"
                            }
                            diffRate > 0 -> {
                                tv_price.setText(stockInfo.last.toString(), 1)
                                stock_up_down.setUpDown(1)
                                stock_up_down.text = "+" + stockInfo.diffRate + "%"
                            }
                            else -> {
                                tv_price.setText(stockInfo.last.toString(), -1)
                                stock_up_down.setUpDown(-1)
                                stock_up_down.text = stockInfo.diffRate.toString() + "%"
                            }
                        }
                    }
                }
            }
        }
    }

    inner class ViewHolderBottom(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<StockMarketInfo>(v, needClick, needLongClick) {
        @BindView(R2.id.ll_add_stock)
        lateinit var ll_add_stock: LinearLayout

        override fun bind(item: StockMarketInfo?, position: Int) {

        }

    }
}