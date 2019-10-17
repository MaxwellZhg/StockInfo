package com.zhuorui.securities.market.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.ui.presenter.HkStockDetailPresenter
import com.zhuorui.securities.market.ui.view.HkStockDetailView
import com.zhuorui.securities.market.ui.viewmodel.HkStockDetailViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/17
 * Desc:
 */
class HkStockDetailFragment :AbsSwipeBackNetFragment<com.zhuorui.securities.market.databinding.FragmentHkStockDetailBinding,HkStockDetailViewModel,HkStockDetailView,HkStockDetailPresenter>(),HkStockDetailView{
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

}