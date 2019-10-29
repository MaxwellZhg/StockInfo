package com.zhuorui.securities.market.ui.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.customer.view.GlobalStockTipsView
import com.zhuorui.securities.market.model.GlobalStockInfo

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/29
 * Desc:
 */
class GlobalStockInfoAdapter(context: Context?) :BaseListAdapter<GlobalStockInfo>(){
    private val context=context
    override fun getLayout(viewType: Int): Int {
        return R.layout.item_global_info_layout
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
       return ViewHolder(v,false,false)
    }
    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean):ListItemViewHolder<GlobalStockInfo>(v, needClick, needLongClick){
        @BindView(R2.id.tv_tips_view)
        lateinit var tv_tips_view: GlobalStockTipsView
        @BindView(R2.id.rv_coustom)
        lateinit var rv_coustom:RecyclerView
        override fun bind(item: GlobalStockInfo?, position: Int) {
            item?.type?.let { tv_tips_view.setTipsData(it) }
            var adapter= GlobalStockInfoTipsAdapter()
            rv_coustom.setHasFixedSize(true)
            rv_coustom.isNestedScrollingEnabled=false
            rv_coustom.adapter=adapter
            rv_coustom.layoutManager= LinearLayoutManager(context)
            if (adapter.items == null) {
                adapter.items = ArrayList()
            }
            adapter.addItems(item?.info)
            adapter.notifyDataSetChanged()
        }

    }

}