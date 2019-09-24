package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.model.STFundAccountData
import com.zhuorui.securities.market.model.STOrderData
import com.zhuorui.securities.market.model.STPositionData

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 16:02
 *    desc   :
 */
interface SimulationTradingMainView : AbsView {
    fun createFundAccountSuccess()
    fun onUpData(positionDatas: List<STPositionData>?, orderDatas: List<STOrderData>?, fundAccount: STFundAccountData)
    fun onGetFundAccountError(code: String?, msg: String?)
    fun onCreateFundAccountError(code: String, message: String?)
}