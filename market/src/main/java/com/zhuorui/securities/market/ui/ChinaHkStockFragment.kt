package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentChinaHkStockTabBinding
import com.zhuorui.securities.market.ui.presenter.ChinaHkStockTabPresenter
import com.zhuorui.securities.market.ui.view.ChinaHkStockTabView
import com.zhuorui.securities.market.ui.viewmodel.ChinaHkStockTabViewModel
import kotlinx.android.synthetic.main.fragment_china_hk_stock_tab.*
import kotlinx.android.synthetic.main.item_stock_detail_header.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/28
 * Desc:
 */
class ChinaHkStockFragment :AbsSwipeBackNetFragment<FragmentChinaHkStockTabBinding,ChinaHkStockTabViewModel,ChinaHkStockTabView,ChinaHkStockTabPresenter>(),ChinaHkStockTabView{
    override val layout: Int
        get() = R.layout.fragment_china_hk_stock_tab
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: ChinaHkStockTabPresenter
        get() = ChinaHkStockTabPresenter()
    override val createViewModel: ChinaHkStockTabViewModel?
        get() =ViewModelProviders.of(this).get(ChinaHkStockTabViewModel::class.java)
    override val getView: ChinaHkStockTabView
        get() = this
    companion object {
        fun newInstance(): ChinaHkStockFragment {
            return ChinaHkStockFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        tv_point_one.text="沪港通"
        tv_point_two.text="沪港通100"
        tv_point_three.text="沪港通300"
        zr_line.setType(1)
        zr_line.setValues(40,10,50)
        zr_line_text.setType(0)
        zr_line_text.setValues(40,10,50)
        zr_line1.setType(1)
        zr_line1.setValues(50,40,30)
        zr_line_text1.setType(0)
        zr_line_text1.setValues(50,40,30)
        zr_line2.setType(1)
        zr_line2.setValues(20,40,50)
        zr_line_text2.setType(0)
        zr_line_text2.setValues(20,40,50)
        trend_one.setType(1)
        trend_two.setType(2)
    }
}