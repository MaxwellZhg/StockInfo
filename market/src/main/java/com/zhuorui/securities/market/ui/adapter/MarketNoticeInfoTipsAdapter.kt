package com.zhuorui.securities.market.ui.adapter

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.net.response.MarketBaseInfoResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/31
 * Desc:
 */
class MarketNoticeInfoTipsAdapter :BaseListAdapter<Int>(){
    var onMarketNoticeClickListener:OnMarketNoticeClickListener?=null
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
        @BindView(R2.id.ll_content)
        lateinit var ll_content: LinearLayout

        init {
            ll_content.setOnClickListener(this)
        }
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

        override fun onClick(v: View) {
            if(v==ll_content){
                onMarketNoticeClickListener?.onMarketNoticeClick()
            }else {
                super.onClick(v)
            }
        }

    }

    interface OnMarketNoticeClickListener{
        fun onMarketNoticeClick()
    }
}