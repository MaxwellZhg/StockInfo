package com.zhuorui.securities.market.ui.mvp

import com.zhuorui.securities.market.databinding.FragmentTopicStockSearchBinding
import com.zhuorui.securities.market.ui.contract.StockSearchContract
import com.zhuorui.securities.base2app.mvp.wrapper.BaseViewWrapper

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