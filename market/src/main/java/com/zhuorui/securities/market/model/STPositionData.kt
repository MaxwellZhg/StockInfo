package com.zhuorui.securities.market.model

import java.math.BigDecimal

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-18 18:09
 *    desc   : 模拟炒股持仓数据
 */
class STPositionData : STOrderData() {
    var marketValue: BigDecimal? = null
    var presentPrice: BigDecimal? = null
    var profitAndLoss: BigDecimal? = null
    var profitAndLossPercentage: BigDecimal? = null
}