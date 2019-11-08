package com.zhuorui.securities.market.model

import java.math.BigDecimal

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-18 18:09
 *    desc   : 模拟炒股持仓数据
 */
class STPositionData : STOrderData() {
    var marketValue: BigDecimal? = null//市值
    var currentPrice: BigDecimal? = null//现价
    var profitAndLoss: BigDecimal? = null//盈亏
    var profitAndLossPercentage: BigDecimal? = null//盈亏比
    var unitCost: BigDecimal? = null//成本单价
}