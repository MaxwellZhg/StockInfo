package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/25 11:01
 *    desc   : 撤销买入订单
 */
class CancelTrustRequest(
    val trustId: String,// 主委托表的订单id
    transaction: String
) : BaseRequest(transaction) {
    init {
        generateSign()
    }
}