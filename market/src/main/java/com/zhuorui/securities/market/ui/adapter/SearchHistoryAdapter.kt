package com.zhuorui.securities.market.ui.adapter

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.commonwidget.ZrCompareTextView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.model.SearchStockInfo

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/20
 * Desc:
 */
class SearchHistoryAdapter :BaseListAdapter<SearchStockInfo>(){
    var onClickCollectStockHistoryListener:OnClickCollectStockHistoryListener?=null
    override fun getLayout(viewType: Int): Int {
        return R.layout.item_serach_history_info
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v, needClick = true, needLongClick = false)
    }

    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean) : ListItemViewHolder<SearchStockInfo>(v, needClick, needLongClick) {
        @BindView(R2.id.tv_stock_name)
        lateinit var tv_stock_name: AppCompatTextView
        @BindView(R2.id.iv_stock_state)
        lateinit var iv_stock_state: ImageView
        override fun bind(item: SearchStockInfo?, position: Int) {
           tv_stock_name.text=item?.name+"("+item?.tsCode+")"
            when(item?.collect){
                true->{
                    iv_stock_state.background= ResUtil.getDrawable(R.mipmap.icon_stock_topiced)
                }
                false->{
                    iv_stock_state.background= ResUtil.getDrawable(R.mipmap.ic_topic_history_d)
                }
            }
        }
        init {
            iv_stock_state.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if(v==iv_stock_state){
                getItem(position)?.let { onClickCollectStockHistoryListener?.topicStockInfo(it) }
            }else{
                super.onClick(v)
            }
        }

    }
    interface OnClickCollectStockHistoryListener{
        fun topicStockInfo(stockInfo:SearchStockInfo)
    }
}