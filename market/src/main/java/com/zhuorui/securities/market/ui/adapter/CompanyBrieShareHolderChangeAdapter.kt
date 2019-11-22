package com.zhuorui.securities.market.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.commonwidget.ZRStockTextView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.model.F10ManagerModel
import com.zhuorui.securities.market.model.F10ShareHolderModel
import com.zhuorui.securities.market.util.DateUtil
import com.zhuorui.securities.market.util.MathUtil

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/11 10:23
 *    desc   : 持股变动
 */
@Suppress("SENSELESS_COMPARISON")
class CompanyBrieShareHolderChangeAdapter : BaseListAdapter<F10ShareHolderModel>() {

    override fun getLayout(viewType: Int): Int {
        return R.layout.layout_item_shareholder_change
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View?) : ListItemViewHolder<F10ShareHolderModel>(v, false, false) {

        @BindView(R2.id.tv_name)
        lateinit var tvName: TextView

        @BindView(R2.id.tv_hareholder_change_number)
        lateinit var changeNumberText: ZRStockTextView

        @BindView(R2.id.tv_hareholder_number)
        lateinit var holderNumber: TextView

        @BindView(R2.id.tv_date)
        lateinit var tvDate: TextView

        @SuppressLint("SetTextI18n")
        override fun bind(item: F10ShareHolderModel?, position: Int) {
            item?.name?.let { tvName.text = it }
            if (item?.changeType == 1) {
                if (item.changeNumber == null) {
                    changeNumberText.setText(ResUtil.getString(R.string.hareholder_change_add), 1)
                } else {
                    changeNumberText.setText("+" + MathUtil.convertToUnitString(item.changeNumber, 1), 1)
                }
            } else {
                if (item?.changeNumber == null) {
                    changeNumberText.setText(ResUtil.getString(R.string.hareholder_change_sub), -1)
                } else {
                    changeNumberText.setText("-" + MathUtil.convertToUnitString(item.changeNumber, 1), -1)
                }
            }
            item?.holdStockNumber?.let {
                holderNumber.text =
                    MathUtil.convertToUnitString(it, 1)
            }
            item?.date?.let { tvDate.text = DateUtil.formatDate(it) }
        }
    }
}