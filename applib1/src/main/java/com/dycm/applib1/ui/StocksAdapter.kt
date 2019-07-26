package com.dycm.applib1.ui

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.dycm.applib1.R
import com.dycm.applib1.R2
import com.dycm.applib1.model.SocketPushStockInfo
import com.dycm.applib1.util.MathUtil
import com.dycm.base2app.adapter.BaseListAdapter
import kotlin.math.abs

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/23 16:10
 *    desc   : 行情列表
 */
class StocksAdapter : BaseListAdapter<SocketPushStockInfo>() {

    override fun getLayout(viewType: Int): Int {
        return R.layout.item_stock
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v, true, false)
    }

    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<SocketPushStockInfo>(v, needClick, needLongClick) {

        @BindView(R2.id.item_name)
        lateinit var item_name: TextView

        @BindView(R2.id.item_id)
        lateinit var item_id: TextView

        @BindView(R2.id.item_price)
        lateinit var item_price: TextView

        @BindView(R2.id.item_higher)
        lateinit var item_higher: TextView

        @SuppressLint("SetTextI18n")
        override fun bind(item: SocketPushStockInfo?, position: Int) {
            item_name.text = item!!.data?.name
            item_id.text = item.code
            item_price.text = item.data?.price.toString()
            if (item.data?.openPrice!! > 0) {
                val higher = abs(item.data?.price!! - item.data?.openPrice!!)
                item_higher.text =
                    (if (higher > 0) "+ " else "- ") + MathUtil.division(
                        higher * 100,
                        item.data!!.openPrice
                    ).toString() + "%"
            } else {
                item_higher.text = ""
            }
        }
    }
}