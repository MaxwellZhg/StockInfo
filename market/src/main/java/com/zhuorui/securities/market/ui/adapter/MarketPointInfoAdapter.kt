package com.zhuorui.securities.market.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.net.response.MarketNewsListResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/23
 * Desc:市场指数详情咨询adapter
 */
class MarketPointInfoAdapter :BaseListAdapter<MarketNewsListResponse.DataList>(){
    override fun getLayout(viewType: Int): Int {
        return R.layout.item_market_point_info_layout
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v,false,false)
    }
    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean):ListItemViewHolder<MarketNewsListResponse.DataList>(v, needClick, needLongClick){
        override fun bind(item: MarketNewsListResponse.DataList?, position: Int) {

        }

    }

}