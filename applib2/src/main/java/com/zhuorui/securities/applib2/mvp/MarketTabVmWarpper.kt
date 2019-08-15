package com.zhuorui.securities.applib2.mvp

import com.zhuorui.securities.applib2.contract.MarketTabContract
import com.zhuorui.securities.applib2.databinding.FargmentTestsubBinding
import com.zhuorui.securities.applib2.viewmodel.MarketTabViewModel
import com.zhuorui.securities.base2app.mvp.wrapper.BaseViewWrapper

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class MarketTabVmWarpper (view: MarketTabContract.View):BaseViewWrapper<MarketTabContract.View,FargmentTestsubBinding>(),MarketTabContract.ViewWrapper{

    private val viewModel by lazy {
        MarketTabViewModel()
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
