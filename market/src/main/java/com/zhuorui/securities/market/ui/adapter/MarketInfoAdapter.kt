package com.zhuorui.securities.market.ui.adapter

import android.view.View
import android.widget.LinearLayout
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
class MarketInfoAdapter :BaseListAdapter<Int>(){
    var onMarketInfoClickListener:OnMarketInfoClickListener?=null
    override fun getLayout(viewType: Int): Int {
        return R.layout.item_market_info_layout
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v,false,false)
    }

    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean):ListItemViewHolder<Int>(v, needClick, needLongClick){
        @BindView(R2.id.ll_content)
        lateinit var ll_content: LinearLayout
        override fun bind(item: Int?, position: Int) {

        }
        init {
            ll_content.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if(v==ll_content){
             onMarketInfoClickListener?.marketInfoClick()
            }else{
                super.onClick(v)
            }
        }
    }

    interface OnMarketInfoClickListener{
        fun marketInfoClick()
    }

}