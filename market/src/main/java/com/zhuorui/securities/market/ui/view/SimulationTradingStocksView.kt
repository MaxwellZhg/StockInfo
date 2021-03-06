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
     * 初始化K线
     */
    fun initKline(ts: String, code: String, tsCode: String, type: Int)

    /**
     * 更新股价
     */
    fun updateStockPrice(price: BigDecimal?, diffPrice: BigDecimal?, diffRate: BigDecimal?, diffState :Int)

    /**
     * 更新盘口
     */
    fun updateStockTrans(transData: PushStockTransData, buyRate: Double, sellRate: Double)

    /**
     * 根据不同的交易类型显示界面
     * @param trustType 0默认购买 1买入改单 2卖出改单 3已持仓可买买卖 4未持仓购买
     */
    fun changeTrustType(trustType: Int)

    /**
     * 更新最大可买
     */
    fun updateMaxBuyNum(maxBuyCount: Long?)

    /**
     * 更新最大可卖
     */
    fun updateMaxBuySell(maxSellCount: Long?)

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
        count: String,
        commission: Double,
        money: String
    )

    /**
     * 买入/卖出成功
     */
    fun tradStocksSuccessful()

    /**
     * 切换K线
     */
    fun toggleKline()

    /**
     * 退出界面
     */
    fun exit()

}