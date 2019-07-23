package com.dycm.applib1.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.dycm.applib1.R
import com.dycm.applib1.R2
import com.dycm.applib1.model.StockInfo
import com.dycm.base2app.adapter.BaseListAdapter

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/23 16:10
 *    desc   : 行情列表
 */
class StocksAdapter : BaseListAdapter<StockInfo>() {

    override fun getLayout(viewType: Int): Int {
        return R.layout.item_stock
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v, true, false)
    }

    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<StockInfo>(v, needClick, needLongClick) {

        @BindView(R2.id.item_name)
        lateinit var item_name: TextView

        @BindView(R2.id.item_id)
        lateinit var item_id: TextView

        @BindView(R2.id.item_price)
        lateinit var item_price: TextView

        @BindView(R2.id.item_higher)
        lateinit var item_higher: TextView

        override fun bind(item: StockInfo?, position: Int) {
            item_name.text = item!!.name
            item_id.text = item.id
            item_price.text = item.price.toString()
//            item_higher.text = item.
        }
    }
}