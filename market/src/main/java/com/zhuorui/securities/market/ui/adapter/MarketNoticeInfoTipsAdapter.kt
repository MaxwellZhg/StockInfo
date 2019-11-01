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
 * Date: 2019/10/31
 * Desc:
 */
class MarketNoticeInfoTipsAdapter :BaseListAdapter<Int>(){
    override fun getLayout(viewType: Int): Int {
        return R.layout.item_attention_tips_layout
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v,needClick = true,needLongClick = false)
    }
    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean):ListItemViewHolder<Int>(v, needClick, needLongClick){
        @BindView(R2.id.view_header)
        lateinit var view_header: View
        @BindView(R2.id.view_coustom)
        lateinit var view_coustom: View
        override fun bind(item: Int?, position: Int) {
            when(position){
                0->{
                    view_header.visibility=View.VISIBLE
                    view_coustom.visibility=View.GONE
                }
                else->{
                    view_coustom.visibility=View.VISIBLE
                    view_header.visibility=View.GONE
                }
            }
        }

    }
}