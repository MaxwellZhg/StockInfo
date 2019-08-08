package com.dycm.applib1.ui

import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.dycm.applib1.R
import com.dycm.applib1.R2
import com.dycm.applib1.model.ChooseStockData
import com.dycm.base2app.adapter.BaseListAdapter
import com.dycm.base2app.util.ResUtil

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/7
 * Desc:
 */
class TopicStocksAdapter(val context: TopicStockListFragment) : BaseListAdapter<ChooseStockData>() {

    private val default = 0x00
    private val bottom = 0x01

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun getItem(position: Int): ChooseStockData? {
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
            default -> R.layout.item_choose_stock
            else -> {
                R.layout.item_add_stock
            }
        }
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            default -> ViewHolderdefalt(v, false, false)
            else -> {
                ViewHolderBottom(v, false, false)
            }
        }
    }

    inner class ViewHolderdefalt(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<ChooseStockData>(v, needClick, needLongClick) {
        @BindView(R2.id.tv_stock_tile)
        lateinit var tv_stock_tile: TextView
        @BindView(R2.id.iv_stock_tag)
        lateinit var iv_stock_tag: ImageView
        @BindView(R2.id.stock_code)
        lateinit var stock_code: TextView
        @BindView(R2.id.stock_up_down)
        lateinit var stock_up_down: TextView
        @BindView(R2.id.tv_precent)
        lateinit var tv_precent: TextView
        @BindView(R2.id.rl_stock_up_down)
        lateinit var rl_stock_up_down: RelativeLayout

        init {
            rl_stock_up_down.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v == rl_stock_up_down) {
                // onAddTopicClickItemCallback?.onAddTopicClickItem(position, getItem(position), v)
            } else {
                super.onClick(v)
            }
        }

        override fun bind(item: ChooseStockData?, position: Int) {
            tv_stock_tile.text = item?.ts
            stock_code.text = item?.code
            stock_up_down.text = item?.precent
            tv_precent.text = item?.num
            when (item?.type) {
                1 -> {
                    iv_stock_tag.background = ResUtil.getDrawable(R.mipmap.hk)
                }
                2 -> {
                    iv_stock_tag.background = ResUtil.getDrawable(R.mipmap.sh)
                }
            }
            when (item?.updown) {
                1 -> {
                    rl_stock_up_down.setBackgroundColor(ResUtil.getColor(R.color.up_stock_color)!!)
                    tv_precent.setTextColor(ResUtil.getColor(R.color.up_price_color)!!)
                }
                2 -> {
                    rl_stock_up_down.setBackgroundColor(ResUtil.getColor(R.color.down_stock_color)!!)
                    tv_precent.setTextColor(ResUtil.getColor(R.color.down_price_color)!!)
                }
            }
        }
    }

    inner class ViewHolderBottom(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<ChooseStockData>(v, needClick, needLongClick) {
        @BindView(R2.id.ll_add_stock)
        lateinit var ll_add_stock: LinearLayout

        init {
            ll_add_stock.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v == ll_add_stock) {
            } else {
                super.onClick(v)
            }
        }

        override fun bind(item: ChooseStockData?, position: Int) {

        }

    }
}