package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import java.math.BigDecimal

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-27 18:19
 *    desc   :
 */
class TodayProfitAndLossResponse(val data: Data) : BaseResponse() {
    data class Data(
        val todaySellAmount: BigDecimal,//今日卖出成交额
        val todayBuyAmount: BigDecimal,//今日买入成交额
        val yesterdayTotalAmount: BigDecimal,//昨日持仓市值
        val todaySellPositionAmount: BigDecimal//今日卖出股票持仓收益
    )
}