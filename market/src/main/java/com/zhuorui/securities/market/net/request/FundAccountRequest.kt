package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/16 11:04
 *    desc   : 计算交易费用
 */
class FundAccountRequest(val token: String, // 用户登陆token
    transaction: String
) : BaseRequest(transaction) {

    init {
        generateSign()
    }
}