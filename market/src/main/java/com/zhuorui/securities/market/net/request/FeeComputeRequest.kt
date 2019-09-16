package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/16 11:04
 *    desc   : 计算交易费用
 */
class FeeComputeRequest(
    val stockMarket: Int, // 股票市场（1-港股 2-美股 3-A股）
    val accountId: String, // 资金账户id
    val chargeType: Int, // 收费类型（1-买入 2-卖出）
    val transactionAmount: BigDecimal, // 交易总金额
    transaction: String
) : BaseRequest(transaction) {

    init {
        generateSign()
    }
}