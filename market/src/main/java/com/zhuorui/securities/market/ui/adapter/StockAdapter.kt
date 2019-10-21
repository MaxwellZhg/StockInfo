package com.zhuorui.securities.market.ui.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.commonwidget.ZrCompareTextView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.config.LocalSearchConfig
import com.zhuorui.securities.market.model.SearchStockInfo

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/23
 * Desc:
 */
class StockAdapter() :BaseListAdapter<SearchStockInfo>(){
    private val default = 0x00
    private val bottom = 0x01
    private lateinit var keywords:String
    override fun getLayout(viewType: Int): Int {
        return when (viewType) {
            default -> R.layout.item_stock_search_layout
            else -> {
                R.layout.item_layout_fifth_data_info
            }
        }
    }


    var onClickStockIntoStockDetailListener:OnClickStockIntoStockDetailListener?=null
    var onStockColollectListenner:OnStockColollectListenner?=null
    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            default -> ViewHolder(v, false, false)
            else -> {
                ViewBottomHolder(v, true, false)
            }
        }
    }


    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<SearchStockInfo>(v, needClick, needLongClick) {
        @BindView(R2.id.tv_stock_info_name)
        lateinit var tv_stock_info_name: ZrCompareTextView
        @BindView(R2.id.iv_stock_logo)
        lateinit var iv_stock_logo: AppCompatImageView
        @BindView(R2.id.tv_stock_code)
        lateinit var tv_stock_code: ZrCompareTextView
        @BindView(R2.id.iv_topic)
        lateinit var iv_topic: AppCompatImageView
        @BindView(R2.id.rl_stock)
        lateinit var rl_stock: ConstraintLayout
        init {
            iv_topic.setOnClickListener(this)
            rl_stock.setOnClickListener(this)
        }

        override fun bind(item: SearchStockInfo?, position: Int) {
            tv_stock_info_name.setText(item?.name,keywords)
            tv_stock_code.setText(item?.code,keywords)
            when (item?.ts) {
                "SH" -> {
                    iv_stock_logo.background = ResUtil.getDrawable(R.mipmap.ic_ts_sh)
                }
                "SZ" -> {
                    iv_stock_logo.background = ResUtil.getDrawable(R.mipmap.ic_ts_sz)
                }
                "HK" -> {
                    iv_stock_logo.background = ResUtil.getDrawable(R.mipmap.ic_ts_hk)
                }
            }
            when(item?.collect){
                true->{
                    iv_topic.background=ResUtil.getDrawable(R.mipmap.icon_stock_topiced)
                }
                false->{
                    iv_topic.background=ResUtil.getDrawable(R.mipmap.ic_topic_history_d)
                }
            }
        }

        override fun onClick(v: View) {
                if (v == iv_topic) {
                    getItem(position)?.let { onStockColollectListenner?.onStockCollectionStock(it) }
                }else if(v == rl_stock){
                    onClickStockIntoStockDetailListener?.onClickStockIntoDetail()
                } else {
                    super.onClick(v)
                }
        }
    }

    inner class ViewBottomHolder(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<SearchStockInfo>(v, needClick, needLongClick) {
        override fun bind(item: SearchStockInfo?, position: Int) {

        }
    }

    fun setkeywords(str:String){
        keywords=str
    }
   interface OnStockColollectListenner{
       fun onStockCollectionStock(stockInfo:SearchStockInfo)
     }
    interface OnClickStockIntoStockDetailListener{
        fun onClickStockIntoDetail()
    }
    override fun getItemCount(): Int {
        return when {
            items == null -> return 0
            items.size > 50 -> items.size
            items.size == 50 -> items.size + 1
            else -> items.size
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (items.size > 50) {
            default
        } else if (items.size == 50) {
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


    override fun getItem(position: Int): SearchStockInfo? {
        if (position > items.size || position == items.size) return null
        return super.getItem(position)
    }


}