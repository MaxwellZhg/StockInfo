package com.dycm.applib1.ui

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.dycm.applib1.R
import com.dycm.applib1.R2
import com.dycm.applib1.model.StockMarketInfo
import com.dycm.applib1.model.StockTsEnum
import com.dycm.base2app.adapter.BaseListAdapter
import com.dycm.base2app.util.ResUtil

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/7
 * Desc: 自选股列表适配器
 */
class TopicStocksAdapter : BaseListAdapter<StockMarketInfo>() {

    private val default = 0x00
    private val bottom = 0x01

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun getItem(position: Int): StockMarketInfo? {
        if (position > items.size || position == items.size) return null
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
            default -> ViewHolderdefalt(v, true, false)
            else -> {
                ViewHolderBottom(v, true, false)
            }
        }
    }

    inner class ViewHolderdefalt(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<StockMarketInfo>(v, needClick, needLongClick) {

        @BindView(R2.id.tv_stock_tile)
        lateinit var tv_stock_tile: TextView
        @BindView(R2.id.iv_stock_ts)
        lateinit var iv_stock_ts: ImageView
        @BindView(R2.id.stock_code)
        lateinit var stock_code: TextView
        @BindView(R2.id.stock_up_down)
        lateinit var stock_up_down: TextView
        @BindView(R2.id.tv_price)
        lateinit var tv_price: TextView
        @BindView(R2.id.rl_stock_up_down)
        lateinit var rl_stock_up_down: RelativeLayout

        @SuppressLint("SetTextI18n")
        override fun bind(item: StockMarketInfo?, position: Int) {
            tv_stock_tile.text = item?.name
            stock_code.text = item?.code
            when (item?.ts) {
                StockTsEnum.HK.name -> {
                    iv_stock_ts.setImageResource(R.mipmap.hk)
                }
                StockTsEnum.SH.name -> {
                    iv_stock_ts.setImageResource(R.mipmap.sh)
                }
            }
            tv_price.text = item?.price.toString()

            if (item?.diffRate!! > 0 || item.diffRate == 0.0) {
                tv_price.setTextColor(ResUtil.getColor(R.color.up_price_color)!!)

                rl_stock_up_down.setBackgroundColor(ResUtil.getColor(R.color.up_stock_color)!!)
                stock_up_down.text = "+" + item.diffRate * 100 + "%"
            } else {
                tv_price.setTextColor(ResUtil.getColor(R.color.down_price_color)!!)

                rl_stock_up_down.setBackgroundColor(ResUtil.getColor(R.color.down_stock_color)!!)
                stock_up_down.text = "-" + item.diffRate * 100 + "%"
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