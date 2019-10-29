package com.zhuorui.securities.market.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/29
 * Desc:
 */
class ChinaHkStockAdapter :BaseListAdapter<Int>(){
    private val default = 0x00
    private val bottom = 0x01
    var onItemClickMoreListener: OnItemClickMoreListener? = null
    override fun getLayout(viewType: Int): Int {
        return when (viewType) {
            default -> R.layout.item_market_part_stock_info
            else -> {
                R.layout.item_china_hk_stock_see_more
            }
        }
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            default -> ViewHolder(v, false, false)
            else -> {
                ViewHolderBottom(v, true, false)
            }
        }
    }

   override fun getItemCount(): Int {
       return when{
                    items == null -> return 0
                    items.size > 5 -> items.size
                    items.size == 5 -> items.size + 1
                    else -> items.size

            }
   }

    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<Int>(v, needClick, needLongClick) {
        override fun bind(item: Int?, position: Int) {

        }
    }

    inner class ViewHolderBottom(v: View?, needClick: Boolean, needLongClick: Boolean):
        ListItemViewHolder<Int>(v, needClick, needLongClick) {
        @BindView(R2.id.tv_see_more)
        lateinit var tv_see_more: TextView
        init {
            tv_see_more.setOnClickListener(this)
        }

        override fun bind(item: Int?, position: Int) {
        }

        override fun onClick(v: View) {
            if(v==tv_see_more){
              onItemClickMoreListener?.onclickMore()
            }else{
                super.onClick(v)
            }
        }


    }

    override fun getItem(position: Int): Int? {
        if (position > items.size || position == items.size) return null
        return super.getItem(position)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            items.size > 5 -> default
            items.size == 5 -> when (position) {
                itemCount - 1 -> {
                    bottom
                }
                else -> {
                    default
                }
            }
            else -> default
        }

    }

    interface OnItemClickMoreListener{
        fun onclickMore()
    }

}