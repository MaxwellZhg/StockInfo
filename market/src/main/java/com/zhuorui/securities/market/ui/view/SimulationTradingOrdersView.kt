package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.model.STOrderData

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 16:02
 *    desc   :
 */
interface SimulationTradingOrdersView : AbsView {
    fun addData(total: Int, list: List<STOrderData>?)
    fun getDataError(msg: String)
    fun onRefreshData()
}