package com.zhuorui.securities.applib1.ui.mvp

import com.zhuorui.securities.applib1.databinding.FragmentStockTabBinding
import com.zhuorui.securities.applib1.ui.contract.StockTabContract
import com.zhuorui.securities.applib1.ui.viewmodel.StockTabViewModel
import com.zhuorui.securities.base2app.mvp.wrapper.BaseViewWrapper

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class StockTabVmWarpper(view: StockTabContract.View) : BaseViewWrapper<StockTabContract.View,FragmentStockTabBinding>(),StockTabContract.ViewWrapper{
    private val viewModel by lazy {
        StockTabViewModel()
    }
   init {
      attachView(view)
   }
    override fun setData() {

    }

    override fun onBind() {
        dataBinding.viewmodel = viewModel
        dataBinding.executePendingBindings()
    }

}