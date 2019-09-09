package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.ui.viewmodel.StockTabViewModel
import java.util.ArrayList

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/19 15:43
 *    desc   :
 */
interface StockTabView : AbsView {

    fun init(fragments: ArrayList<StockTabViewModel.PageInfo>)

    /**
     * 切换选择列表
     */
    fun toggleStockTab(show: Boolean)

    /**
     * 跳转到开户
     */
    fun onJumpToSimulationTradingStocksPage()
}