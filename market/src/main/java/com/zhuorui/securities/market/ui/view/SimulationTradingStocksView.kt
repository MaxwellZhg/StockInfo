package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.model.PushStockTransData
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/9 13:36
 *    desc   :
 */
interface SimulationTradingStocksView : AbsView {

    /**
     * 更新股价
     */
    fun updateStockPrice(price: BigDecimal, diffPrice: BigDecimal, diffRate: BigDecimal)

    /**
     * 更新盘口
     */
    fun updateStockTrans(transData: PushStockTransData, buyRate: Double, sellRate: Double)

    /**
     * 更新最大可买
     */
    fun updateMaxBuyNum(maxBuyCount: Long?)
}