package com.zhuorui.securities.market.ui

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.BaseApplication.Companion.context
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.model.SearchDeafaultData

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/19
 * Desc:
 */
class SearchInfoAdapter(context:Context?) :BaseListAdapter<SearchDeafaultData>(){
    private val itemHeader=0x01
    private val itemBottom=0x02
    override fun getLayout(viewType: Int): Int {
        return when(viewType){
            itemHeader->{
                R.layout.item_search_header
            }
            else->{
                R.layout.item_search_history
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
    inner class ViewHolderHeader(v: View?, needClick: Boolean, needLongClick: Boolean):ListItemViewHolder<SearchDeafaultData>(v, needClick, needLongClick){
        @BindView(R2.id.rl_hot)
        lateinit var rl_hot:RecyclerView
        override fun bind(item: SearchDeafaultData?, position: Int) {
          var adapter=HotSearchAdapter()
            rl_hot.adapter=adapter
            rl_hot.layoutManager=GridLayoutManager(context,2)
            rl_hot.addItemDecoration(SpaceItemDecoration(ResUtil.getDimensionDp2Px(25f)))
            if (adapter.items == null) {
                adapter.items = ArrayList()
            }
            adapter.addItems(item?.hotlist)
        }
    }
    inner class ViewHolderBottom(v: View?, needClick: Boolean, needLongClick: Boolean):ListItemViewHolder<SearchDeafaultData>(v, needClick, needLongClick){
        @BindView(R2.id.rv_history)
        lateinit var rv_history:RecyclerView
        override fun bind(item: SearchDeafaultData?, position: Int) {
            var adapter=SearchHistoryAdapter()
            rv_history.adapter=adapter
            if (adapter.items == null) {
                adapter.items = ArrayList()
            }
            adapter.addItems(item?.history)
        }

    }

}