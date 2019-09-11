package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.model.SearchStockInfo

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 16:02
 *    desc   :
 */
interface SimulationTradingSearchView : AbsView {
    fun setSearchData(datas: List<SearchStockInfo>)
}