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

    /**
     * 今日资金分布数据
     */
    fun onTodayFundTransactionData(data: CapitalData?)

    /**
     * 今天资金趋势数据
     */
    fun onTodatCapitalFlowTrendData(data: List<CapitalTrendModel>)

    /**
     * 历史资金流向数据
     */
    fun onHistoricalCapitalFlowData(data: List<CapitalTrendModel>)

    /**
     * 股票实时价格
     */
    fun onUpPrice(t: Float?)

}