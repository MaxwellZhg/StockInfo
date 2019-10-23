package com.zhuorui.securities.market.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.ui.presenter.NewStockDatePresenter
import com.zhuorui.securities.market.ui.view.NewStockDateView
import com.zhuorui.securities.market.ui.viewmodel.NewStockDateViewModel
import com.zhuorui.securities.market.databinding.FragmentNewStockDateBinding
/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/23
 * Desc:
 */
class NewStockDateFragment :AbsSwipeBackNetFragment<FragmentNewStockDateBinding,NewStockDateViewModel,NewStockDateView,NewStockDatePresenter>(),NewStockDateView{
    override val layout: Int
        get() = R.layout.fragment_new_stock_date
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: NewStockDatePresenter
        get() = NewStockDatePresenter()
    override val createViewModel: NewStockDateViewModel?
        get() = ViewModelProviders.of(this).get(NewStockDateViewModel::class.java)
    override val getView: NewStockDateView
        get() = this

    companion object {
        fun newInstance(): NewStockDateFragment {
            return NewStockDateFragment()
        }
    }

}