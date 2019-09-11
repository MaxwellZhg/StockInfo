package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.ui.view.MockStockMainView
import com.zhuorui.securities.market.ui.viewmodel.MockStockMainViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 16:03
 *    desc   :
 */
class MockStockMainPresenter : AbsNetPresenter<MockStockMainView, MockStockMainViewModel>() {


    fun createFundAccount() {
        view?.createFundAccountSuccess()
    }
}