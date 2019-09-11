package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.ui.view.SimulationTradingMainView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingMainViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 16:03
 *    desc   :
 */
class SimulationTradingMainPresenter : AbsNetPresenter<SimulationTradingMainView, SimulationTradingMainViewModel>() {


    fun createFundAccount() {
        view?.createFundAccountSuccess()
    }
}