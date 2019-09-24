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

    /**
     * 取消价格焦点
     */
    fun clearBuyPriceFocus()

    /**
     * 取消数量焦点
     */
    fun clearBuyCountFocus()

    /**
     * 查看订单交易明细
     * @param accountId 账户id
     * @param chargeType 1买入 2卖出
     * @param stockName 股票名称
     * @param tsCode 股票代码
     * @param price 价格
     * @param count 数量
     * @param commission 佣金
     * @param money 金额
     */
    fun showTradingStocksOrderDetail(
        accountId: String,
        chargeType: Int,
        stockName: String,
        tsCode: String,
        price: String,
        count: Int,
        commission: Double,
        money: String
    )

    /**
     * 买入成功
     */
    fun buyStocksSuccessful()
}