package com.zhuorui.securities.market.ui.adapter

import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/22
 * Desc:
 */
class MarketPartInfoAdapter(type:Int,state:Int) :BaseListAdapter<Int>(){
    var type =type
    var state :Int = state
    var onPartInfoClickListener:OnAllPartInfoClickListener?=null
    var onMainPartInfoClickListener:OnMainPartInfoClickListener?=null
    var onCreatePartInfoClickListener:OnCreatePartInfoClickListener?=null
    var onCombineInfoClickListener:OnCombineInfoClickListener?=null
    override fun getLayout(viewType: Int): Int {
       return when(type){
           1->{
               R.layout.item_market_part_stock_info
           }
           else->{
               R.layout.item_market_point_view
           }
       }
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
       return ViewHolder(v,false,false)
    }

    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean):ListItemViewHolder<Int>(v, needClick, needLongClick) {
        @BindView(R2.id.rl_content)
        lateinit var rl_content: RelativeLayout

        override fun bind(item: Int?, position: Int) {

        }

        init {
            rl_content.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v == rl_content) {
             when(state){
                 1->{
                     onPartInfoClickListener?.onPartInfoClick()
                 }
                 2->{
                     onMainPartInfoClickListener?.onMainPartInfoClick()
                 }
                 3->{
                     onCreatePartInfoClickListener?.onCreatePartInfoClick()
                 }
                 4->{
                     onCombineInfoClickListener?.onCombineClick()
                 }
              }
            } else {
                super.onClick(v)
            }
        }
    }

    interface OnAllPartInfoClickListener{
        fun onPartInfoClick()
    }
    interface OnMainPartInfoClickListener{
        fun onMainPartInfoClick()
    }
    interface OnCreatePartInfoClickListener{
        fun onCreatePartInfoClick()
    }
    interface OnCombineInfoClickListener{
        fun onCombineClick()
    }

}