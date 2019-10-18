package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.ui.presenter.HkStockDetailPresenter
import com.zhuorui.securities.market.ui.view.HkStockDetailView
import com.zhuorui.securities.market.ui.viewmodel.HkStockDetailViewModel
import kotlinx.android.synthetic.main.item_stock_detail_header.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/17
 * Desc:
 */
class HkStockDetailFragment :
    AbsSwipeBackNetFragment<com.zhuorui.securities.market.databinding.FragmentHkStockDetailBinding, HkStockDetailViewModel, HkStockDetailView, HkStockDetailPresenter>(),
    HkStockDetailView {
    override val layout: Int
        get() = R.layout.fragment_hk_stock_detail
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: HkStockDetailPresenter
        get() = HkStockDetailPresenter()
    override val createViewModel: HkStockDetailViewModel?
        get() = ViewModelProviders.of(this).get(HkStockDetailViewModel::class.java)
    override val getView: HkStockDetailView
        get() = this

    companion object {
        fun newInstance(): HkStockDetailFragment {
            return HkStockDetailFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
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
    }

}