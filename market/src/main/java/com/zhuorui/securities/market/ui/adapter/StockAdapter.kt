package com.zhuorui.securities.market.ui.adapter

import android.view.View
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
 * Date: 2019/9/23
 * Desc:
 */
class StockAdapter() :BaseListAdapter<SearchStockInfo>(){
    private lateinit var keywords:String
    override fun getLayout(viewType: Int): Int {
       return R.layout.item_stock_search_layout
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v,true,false)
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
        init {
            iv_topic.setOnClickListener(this)
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
        }

        override fun onClick(v: View) {
            super.onClick(v)
        }
    }

    fun setkeywords(str:String){
        keywords=str
    }


}