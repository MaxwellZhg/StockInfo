package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.model.CapitalTrendModel
import com.zhuorui.securities.market.socket.vo.CapitalData

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:56
 *    desc   :
 */
interface MarketDetailCapitalView : AbsView {

    fun onTodayFundTransactionData(data: CapitalData?)

    fun onTodatCapitalFlowTrendData(data:List<CapitalTrendModel>)

}