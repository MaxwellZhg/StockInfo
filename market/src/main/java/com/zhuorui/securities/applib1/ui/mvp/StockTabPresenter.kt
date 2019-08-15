package com.zhuorui.securities.applib1.ui.mvp

import com.zhuorui.securities.applib1.ui.contract.StockTabContract
import com.zhuorui.securities.base2app.mvp.BasePresenter

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class StockTabPresenter (view: StockTabContract.View):BasePresenter<StockTabContract.View,StockTabContract.ViewWrapper>(),StockTabContract.Presenter{
    init {
        attachView(view)
    }
    override fun fetchData() {
      viewWrapper.setData()
    }

}