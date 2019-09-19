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
    var presentPrice: Float? = null
    var profitAndLoss: Float? = null
    var profitAndLossPercentage: Float? = null
}