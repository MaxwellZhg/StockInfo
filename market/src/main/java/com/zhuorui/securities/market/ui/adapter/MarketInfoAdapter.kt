package com.zhuorui.securities.market.ui.adapter

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.net.response.MarketNewsListResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/31
 * Desc:
 */
class MarketInfoAdapter :BaseListAdapter<MarketNewsListResponse.DataList>(){
    var onMarketInfoClickListener:OnMarketInfoClickListener?=null
    override fun getLayout(viewType: Int): Int {
        return R.layout.item_market_info_layout
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v,false,false)
    }

    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean):ListItemViewHolder<MarketNewsListResponse.DataList>(v, needClick, needLongClick){
        @BindView(R2.id.ll_content)
        lateinit var ll_content: LinearLayout
        @BindView(R2.id.tv_info)
        lateinit var tv_info: TextView
        @BindView(R2.id.tv_time)
        lateinit var tv_time: TextView
        override fun bind(item: MarketNewsListResponse.DataList?, position: Int) {
            tv_info.text=item?.newsTitle
            tv_time.text= item?.createTime?.let { TimeZoneUtil.timeFormat(it, "HH:mm") }
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