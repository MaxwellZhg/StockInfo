package com.zhuorui.securities.market.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.model.F10RepoModel
import com.zhuorui.securities.market.util.DateUtil
import com.zhuorui.securities.market.util.MathUtil

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/11 10:23
 *    desc   : 公司回购
 */
class CompanyBrieRepoAdapter : BaseListAdapter<F10RepoModel>() {

    override fun getLayout(viewType: Int): Int {
        return R.layout.layout_item_repo
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View?) : ListItemViewHolder<F10RepoModel>(v, false, false) {

        @BindView(R2.id.tv_date)
        lateinit var tv_date: TextView

        @BindView(R2.id.tv_number)
        lateinit var tv_number: TextView

        @BindView(R2.id.tv_price)
        lateinit var tv_price: TextView

        @SuppressLint("SetTextI18n")
        override fun bind(item: F10RepoModel?, position: Int) {
            item?.date?.let { tv_date.text = DateUtil.formatDate(it) }
            item?.number?.let { tv_number.text = MathUtil.convertToUnitString(it, 1) }
            item?.avgPrice?.let { tv_price.text = MathUtil.rounded3(it).toString() }
        }
    }
}