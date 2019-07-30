package com.dycm.applib1.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.dycm.applib1.R
import com.dycm.applib1.R2
import com.dycm.applib1.model.NetStockInfo
import com.dycm.base2app.adapter.BaseListAdapter

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/26 14:12
 *    desc   : 搜索股票结果列表适配器
 */
class SearchStocksAdapter : BaseListAdapter<NetStockInfo>() {

    override fun getLayout(viewType: Int): Int {
        return R.layout.item_search_stock
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v, false, false)
    }

    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<NetStockInfo>(v, needClick, needLongClick) {

        @BindView(R2.id.item_ts)
        lateinit var item_ts: TextView

        @BindView(R2.id.item_code)
        lateinit var item_code: TextView

        @BindView(R2.id.item_name)
        lateinit var item_name: TextView

        override fun bind(item: NetStockInfo?, position: Int) {
            item_ts.text = item?.ts
            item_code.text = item?.code
            item_name.text = item?.name
        }
    }
}