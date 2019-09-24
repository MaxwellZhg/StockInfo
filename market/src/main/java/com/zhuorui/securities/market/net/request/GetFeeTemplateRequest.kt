package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/23 17:21
 *    desc   : 交易费用规则模版
 */
class GetFeeTemplateRequest(
    val stockMarket: String, // 股票市场（1-港股 2-美股 3-A股）
    val accountId: String, // 资金账户id
    transaction: String
) : BaseRequest(transaction) {
    init {
        generateSign()
    }
}