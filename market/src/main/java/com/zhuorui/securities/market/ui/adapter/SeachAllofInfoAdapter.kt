package com.zhuorui.securities.market.ui.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.event.ChageSearchTabEvent
import com.zhuorui.securities.market.model.SearchDeafaultData
import com.zhuorui.securities.market.model.SearchStokcInfoEnum

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/23
 * Desc:
 */
class SeachAllofInfoAdapter(context: Context?) : BaseListAdapter<SearchDeafaultData>(){
    private val context=context
    private val itemHeader=0x01
    private val itemBottom=0x02
    override fun getLayout(viewType: Int): Int {
        return when(viewType){
            itemHeader->{
                R.layout.item_stock_info_layout
            }
            else->{
                R.layout.item_infomation_layout
            }
        }
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            itemHeader->{
                ViewHolderHeader(v, needClick = false, needLongClick = false)
            }
            else->{
                ViewHolderBottom(v, needClick = false, needLongClick = false)
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when(position){
            0->{
                itemHeader
            }
            else->{
                itemBottom
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    inner class ViewHolderHeader(v: View?, needClick: Boolean, needLongClick: Boolean):
        ListItemViewHolder<SearchDeafaultData>(v, needClick, needLongClick){
        @BindView(R2.id.rv_stock_info)
        lateinit var rv_stock_info: RecyclerView
        override fun bind(item: SearchDeafaultData?, position: Int) {
            var adapter= SearchStockInfoAdapter()
            rv_stock_info.adapter=adapter
            rv_stock_info.layoutManager=LinearLayoutManager(context)
            if (adapter.items == null) {
                adapter.items = ArrayList()
            }
            adapter.addItems(item?.hotlist)
            adapter.setClickItemCallback{ i: Int, i1: Int, view: View ->
                if(i1==null){
                    RxBus.getDefault().post(ChageSearchTabEvent(SearchStokcInfoEnum.Stock))
                }
            }
        }
    }
    inner class ViewHolderBottom(v: View?, needClick: Boolean, needLongClick: Boolean):
        ListItemViewHolder<SearchDeafaultData>(v, needClick, needLongClick){
        @BindView(R2.id.rv_infomation)
        lateinit var rv_infomation: RecyclerView
        override fun bind(item: SearchDeafaultData?, position: Int) {
            var adapter= SearchInfomationAdapter(0)
            rv_infomation.adapter=adapter
            rv_infomation.layoutManager=LinearLayoutManager(context)
            if (adapter.items == null) {
                adapter.items = ArrayList()
            }
            adapter.addItems(item?.history)
            adapter.setClickItemCallback{ i: Int, i1: Int, view: View ->
                 if(i1==null){
                     RxBus.getDefault().post(ChageSearchTabEvent(SearchStokcInfoEnum.Info))
                 }
            }
        }

    }

}