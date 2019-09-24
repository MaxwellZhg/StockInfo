package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

class FundAccountRequest(
    val stockMarket: Int, // 股票市场股票市场（1-港股 2-美股  3-A股
    transaction: String
) : BaseRequest(transaction) {

    init {
        generateSign()
    }
}