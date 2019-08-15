package com.zhuorui.securities.applib2.mvp

import com.zhuorui.securities.applib2.contract.MarketTabContract
import com.zhuorui.securities.base2app.mvp.BasePresenter

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class MarketTabPresenter(view: MarketTabContract.View):BasePresenter<MarketTabContract.View,MarketTabContract.ViewWrapper>(),MarketTabContract.Presenter{

    init {
        attachView(view)
    }
    override fun fetchData() {
        viewWrapper.setData()
    }



}