package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/9 13:36
 *    desc   :
 */
interface SimulationTradingStocksView : AbsView {

    fun updateStockPrice(price: BigDecimal, diffPrice: BigDecimal, diffRate: BigDecimal)
}