package com.zhuorui.securities.market.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.model.TestNoticeData

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/31
 * Desc:
 */
class MarketNoticeInfoAdapter :BaseListAdapter<TestNoticeData>(){
    override fun getLayout(viewType: Int): Int {
       return R.layout.item_notice_info_layout
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
      return ViewHolder(v,false,false)
    }
    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean):ListItemViewHolder<TestNoticeData>(v, needClick, needLongClick){
        @BindView(R2.id.tv_date_tips)
        lateinit var tv_date_tips: TextView
        @BindView(R2.id.rv_infos)
        lateinit var rv_infos: RecyclerView
        override fun bind(item: TestNoticeData?, position: Int) {
            tv_date_tips.text=item?.tips
            var adapter= MarketNoticeInfoTipsAdapter()
            rv_infos.adapter=adapter
            if (adapter.items == null) {
                adapter.items = ArrayList()
            }
            adapter.addItems(getItem(position)?.list)
        }

    }

}