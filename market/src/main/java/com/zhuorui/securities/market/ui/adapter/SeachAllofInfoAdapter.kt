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
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.SearchStokcInfoEnum
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/23
 * Desc:全部搜索的adapter
 */
class SeachAllofInfoAdapter(context: Context?) : BaseListAdapter<SearchDeafaultData>(){
    private val context=context
    private val itemHeader=0x01
    private val itemBottom=0x02
    private lateinit var keywords:String
    var onTopicStockInfoListenner:OnTopicStockInfoListenner? = null
    var onClickStockAllIntoStockDetailListener: OnClickStockAllIntoStockDetailListener?=null
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

    inner class ViewHolderHeader(v: View?, needClick: Boolean, needLongClick: Boolean):
        ListItemViewHolder<SearchDeafaultData>(v, needClick, needLongClick),SearchStockInfoAdapter.OnTopicStockInfoListener,
        SearchStockInfoAdapter.OnClickStockIntoStockDetailListener {
        @BindView(R2.id.rv_stock_info)
        lateinit var rv_stock_info: RecyclerView
        override fun bind(item: SearchDeafaultData?, position: Int) {
            var adapter= SearchStockInfoAdapter(keywords)
            adapter.onTopicStockInfoListener=this
            adapter.onClickStockIntoStockDetailListener=this
            rv_stock_info.setHasFixedSize(true)
            rv_stock_info.isNestedScrollingEnabled=false
            rv_stock_info.adapter=adapter
            rv_stock_info.layoutManager=LinearLayoutManager(context)
            if (adapter.items == null) {
                adapter.items = ArrayList()
            }
            adapter.addItems(item?.hotlist)
            adapter.notifyDataSetChanged()
        }
        override fun topicStockInfo(stockInfo: SearchStockInfo) {
           onTopicStockInfoListenner?.onClickCollectionStock(stockInfo)
        }
        override fun onClickStockIntoDetail(stock:SearchStockInfo) {
            onClickStockAllIntoStockDetailListener?.onClickStockAllIntoDeatil(stock)
        }
    }
    inner class ViewHolderBottom(v: View?, needClick: Boolean, needLongClick: Boolean):
        ListItemViewHolder<SearchDeafaultData>(v, needClick, needLongClick),OnClickItemCallback<Int>{

        @BindView(R2.id.rv_infomation)
        lateinit var rv_infomation: RecyclerView
        override fun bind(item: SearchDeafaultData?, position: Int) {
            var adapter= SearchInfomationAdapter(0,keywords)
            rv_infomation.setHasFixedSize(true)
            rv_infomation.isNestedScrollingEnabled=false
            rv_infomation.adapter=adapter
            rv_infomation.layoutManager=LinearLayoutManager(context)
            if (adapter.items == null) {
                adapter.items = ArrayList()
            }
            adapter.addItems(item?.history)
            adapter.setClickItemCallback(ViewHolderBottom@this)
        }
        override fun onClickItem(pos: Int, item: Int?, v: View?) {
            if(item==null){
                RxBus.getDefault().post(ChageSearchTabEvent(SearchStokcInfoEnum.Info))
            }
        }

    }

    fun setkeywords(str:String){
         keywords=str
    }
   interface OnTopicStockInfoListenner{
       fun onClickCollectionStock(stock:SearchStockInfo)
   }

  interface OnClickStockAllIntoStockDetailListener{
      fun onClickStockAllIntoDeatil(stock:SearchStockInfo)
  }

}