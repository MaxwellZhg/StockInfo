package com.zhuorui.securities.market.ui.adapter

import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/23
 * Desc:
 */
class SearchInfomationAdapter(type:Int) :BaseListAdapter<Int>(){
    private var type=type
    private val default = 0x00
    private val bottom = 0x01
    override fun getLayout(viewType: Int): Int {
        return when (viewType) {
            default -> R.layout.item_seach_stock_infomation_layout
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

    override fun getItemCount(): Int {
        when(type){
            1->{
                return when {
                    items == null -> return 0
                    items.size > 8 -> items.size
                    items.size == 8 -> items.size + 1
                    else -> items.size
                }
            }
            else->{
                return when {
                    items == null -> return 0
                    items.size > 5 -> items.size
                    items.size == 5 -> items.size + 1
                    else -> items.size
                }
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
        @BindView(R2.id.tv_tips_info)
        lateinit var tv_tips_info: AppCompatTextView
        override fun bind(item: Int?, position: Int) {
            tv_tips_info.text=ResUtil.getString(R.string.see_more_infomation)
        }

    }

    override fun getItem(position: Int): Int? {
        if (position > items.size || position == items.size) return null
        return super.getItem(position)
    }

    override fun getItemViewType(position: Int): Int {
        when(type){
            1->{
                return if (items.size > 8) {
                    default
                } else if (items.size == 8) {
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
            else->{
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
        }
    }
}