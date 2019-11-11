package com.zhuorui.securities.market.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.model.F10DividendModel
import com.zhuorui.securities.market.util.DateUtil

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/11 10:23
 *    desc   : 分红派送
 */
class CompanyBrieDividendAdapter : BaseListAdapter<F10DividendModel>() {

    override fun getLayout(viewType: Int): Int {
        return R.layout.layout_item_dividend
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View?) : ListItemViewHolder<F10DividendModel>(v, false, false) {

        @BindView(R2.id.tv_exemption_date)
        lateinit var tvExemptionDate: TextView

        @BindView(R2.id.tv_dividend_date)
        lateinit var tvDividendDate: TextView

        @BindView(R2.id.tv_allocation_plan)
        lateinit var tvAllocationPlan: TextView

        override fun bind(item: F10DividendModel?, position: Int) {
            item?.exemptionDate?.let {
                tvExemptionDate.text = DateUtil.formatDate(it)
            }
            item?.date?.let { tvDividendDate.text = DateUtil.formatDate(it) }
            item?.allocationPlan?.let { tvAllocationPlan.text = it }
        }
    }
}