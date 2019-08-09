package com.dycm.applib1.ui

import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.dycm.applib1.R
import com.dycm.applib1.R2
import com.dycm.applib1.model.SearchStockInfo
import com.dycm.base2app.adapter.BaseListAdapter
import com.dycm.base2app.util.ResUtil

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/26 14:12
 *    desc   : 搜索股票结果列表适配器
 */
class SearchStocksAdapter : BaseListAdapter<SearchStockInfo>() {
    private val default = 0x00
    private val bottom = 0x01
    var onAddTopicClickItemCallback: OnAddTopicClickItemCallback? = null

    override fun getItemCount(): Int {
        return if (items.size > 5) {
            items.size
        } else {
            items.size + 1
        }
    }

    override fun getLayout(viewType: Int): Int {
        return if (items.size > 5) {
            R.layout.item_search_topic_stock
        } else {
            when (viewType) {
                default -> R.layout.item_search_topic_stock
                else -> {
                    R.layout.item_add_more
                }
            }
        }
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return if (items.size > 5) {
            ViewHolder(v, false, false)
        } else {
            when (viewType) {
                default -> ViewHolder(v, false, false)
                else -> {
                    ViewHolderBottom(v, true, false)
                }
            }
        }
    }

    interface OnAddTopicClickItemCallback {
        fun onAddTopicClickItem(pos: Int, item: SearchStockInfo?, view: View)
    }

    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<SearchStockInfo>(v, needClick, needLongClick) {

        @BindView(R2.id.iv_tag)
        lateinit var iv_tag: ImageView

        @BindView(R2.id.tv_stock_name)
        lateinit var tv_stock_name: TextView

        @BindView(R2.id.tv_stock_code)
        lateinit var tv_stock_code: TextView

        @BindView(R2.id.iv_add)
        lateinit var iv_add: ImageView

        init {
            iv_add.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v == iv_add) {
                onAddTopicClickItemCallback?.onAddTopicClickItem(position, getItem(position), v)
            } else {
                super.onClick(v)
            }
        }

        override fun bind(item: SearchStockInfo?, position: Int) {
            // item_ts.text = item?.ts
            when (item?.ts) {
                "SH" -> {
                    iv_tag.background = ResUtil.getDrawable(R.mipmap.sh)
                }
                "SZ" -> {
                    iv_tag.background = ResUtil.getDrawable(R.mipmap.sz)
                }
                "HK" -> {
                    iv_tag.background = ResUtil.getDrawable(R.mipmap.hk)
                }
            }
            tv_stock_code.text = item?.code
            tv_stock_name.text = item?.name
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items.size > 5) {
            default
        } else {
            return when (position) {
                itemCount - 1 -> {
                    bottom
                }
                else -> {
                    default
                }
            }
        }
    }

    override fun getItem(position: Int): SearchStockInfo? {
        if (position > items.size || position == items.size) return null
        return super.getItem(position)
    }

    inner class ViewHolderBottom(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<SearchStockInfo>(v, needClick, needLongClick) {
        @BindView(R2.id.rl_add_more)
        lateinit var rl_add_more: RelativeLayout

        override fun bind(item: SearchStockInfo?, position: Int) {

        }
    }
}