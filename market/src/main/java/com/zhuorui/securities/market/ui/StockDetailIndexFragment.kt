package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentStockDetailIndexBinding
import com.zhuorui.securities.market.ui.presenter.StockDetailIndexPresenter
import com.zhuorui.securities.market.ui.view.StockDetailIndexView
import com.zhuorui.securities.market.ui.viewmodel.StockDetailIndexViewModel


/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-11-04 15:11
 *    desc   : 指数Fragment
 */
class StockDetailIndexFragment :
    AbsFragment<FragmentStockDetailIndexBinding, StockDetailIndexViewModel, StockDetailIndexView, StockDetailIndexPresenter>(),
    StockDetailIndexView {

    companion object {
        fun newInstance(code: String, ts: String): StockDetailIndexFragment {
            val b = Bundle()
            b.putString("code", code)
            b.putString("ts", ts)
            val fragment = StockDetailIndexFragment()
            fragment.arguments = b
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_stock_detail_index

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: StockDetailIndexPresenter
        get() = StockDetailIndexPresenter()

    override val createViewModel: StockDetailIndexViewModel?
        get() = ViewModelProviders.of(this).get(StockDetailIndexViewModel::class.java)

    override val getView: StockDetailIndexView
        get() = this


}