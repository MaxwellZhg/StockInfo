package com.zhuorui.securities.market.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.model.F10ManagerModel
import com.zhuorui.securities.market.util.MathUtil

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/11 10:23
 *    desc   : 公司高管
 */
class CompanyBrieManagerAdapter : BaseListAdapter<F10ManagerModel>() {

    override fun getLayout(viewType: Int): Int {
        return R.layout.layout_item_company_manager
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View?) : ListItemViewHolder<F10ManagerModel>(v, false, false) {

        @BindView(R2.id.tv_name)
        lateinit var tv_name: TextView

        @BindView(R2.id.tv_jobtitle)
        lateinit var tv_jobtitle: TextView

        @BindView(R2.id.tv_salary)
        lateinit var tv_salary: TextView

        override fun bind(item: F10ManagerModel?, position: Int) {
            item?.name?.let { tv_name.text = it }
            item?.jobTitle?.let { tv_jobtitle.text = it }
            item?.salary?.let { tv_salary.text = MathUtil.convertToUnitString(it, 1) }
        }
    }
}