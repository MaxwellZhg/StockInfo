package com.zhuorui.securities.market.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.model.SearchStockInfo

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/23
 * Desc:
 */
class SearchStockInfoAdapter : BaseListAdapter<Int>(){
    private val default = 0x00
    private val bottom = 0x01
    override fun getLayout(viewType: Int): Int {
        return when (viewType) {
            default -> R.layout.item_stock_search_layout
            else -> {
                R.layout.item_search_stock_tips_more
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


   inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean) :
    ListItemViewHolder<Int>(v, needClick, needLongClick) {
       override fun bind(item: Int?, position: Int) {

       }
   }

    inner class ViewHolderBottom(v: View?, needClick: Boolean, needLongClick: Boolean):
        ListItemViewHolder<Int>(v, needClick, needLongClick) {
           override fun bind(item: Int?, position: Int) {

           }

      }

    override fun getItemViewType(position: Int): Int {
        return if (items.size > 5) {
            default
        } else if (items.size == 5) {
            when (position) {
                itemCount - 1 -> {
                    bottom
                }
                else -> {
                    default
                }
            }
        } else {
            default
        }
    }

    override fun getItemCount(): Int {
        return when {
            items == null -> return 0
            items.size > 5 -> items.size
            items.size == 5 -> items.size + 1
            else -> items.size
        }
    }

    override fun getItem(position: Int): Int? {
        if (position > items.size || position == items.size) return null
        return super.getItem(position)
    }


}