package com.dycm.applib2.mvp

import com.dycm.applib2.contract.MarketTabContract
import com.dycm.applib2.databinding.FargmentTestsubBinding
import com.dycm.applib2.viewmodel.MarketTabViewModel
import com.dycm.base2app.mvp.wrapper.BaseViewWrapper

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
