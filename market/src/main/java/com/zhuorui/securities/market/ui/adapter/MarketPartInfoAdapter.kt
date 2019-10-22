package com.zhuorui.securities.market.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.market.R

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/22
 * Desc:
 */
class MarketPartInfoAdapter :BaseListAdapter<Int>(){
    override fun getLayout(viewType: Int): Int {
       return  R.layout.item_market_part_stock_info
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
       return ViewHolder(v,false,false)
    }

    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean):ListItemViewHolder<Int>(v, needClick, needLongClick){
        override fun bind(item: Int?, position: Int) {

        }
    }

}