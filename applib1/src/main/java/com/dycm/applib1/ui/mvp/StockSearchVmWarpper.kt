package com.dycm.applib1.ui.mvp

import com.dycm.applib1.databinding.FragmentTopicStockSearchBinding
import com.dycm.applib1.ui.contract.StockSearchContract
import com.dycm.base2app.mvp.wrapper.BaseViewWrapper

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class StockSearchVmWarpper(view: StockSearchContract.View):BaseViewWrapper<StockSearchContract.View,FragmentTopicStockSearchBinding>(),StockSearchContract.ViewWrapper{
    init {
        attachView(view)
    }
    override fun setData() {

    }

}