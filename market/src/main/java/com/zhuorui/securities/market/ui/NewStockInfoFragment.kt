package com.zhuorui.securities.market.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.ui.presenter.NewStockInfoPresenter
import com.zhuorui.securities.market.ui.view.NewStockInfoView
import com.zhuorui.securities.market.ui.viewmodel.NewStockInfoViewModel
import com.zhuorui.securities.market.databinding.FragmentNewStockInfoBinding
/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/24
 * Desc:新股信息
 * */
class NewStockInfoFragment :AbsSwipeBackNetFragment<FragmentNewStockInfoBinding,NewStockInfoViewModel,NewStockInfoView,NewStockInfoPresenter>(),NewStockInfoView{
    override val layout: Int
        get() = R.layout.fragment_new_stock_info
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: NewStockInfoPresenter
        get() = NewStockInfoPresenter()
    override val createViewModel: NewStockInfoViewModel?
        get() = ViewModelProviders.of(this).get(NewStockInfoViewModel::class.java)
    override val getView: NewStockInfoView
        get() = this

    companion object{
        fun newInstance(): NewStockInfoFragment {
            return NewStockInfoFragment()
        }
    }

}