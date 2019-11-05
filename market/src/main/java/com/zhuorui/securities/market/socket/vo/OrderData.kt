package com.zhuorui.securities.market.socket.vo

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/4 17:55
 *    desc   : 查询最新委托挂单数据
 */
class OrderData {

    var ts: String? = null
    var code: String? = null
    var type: Int? = null
    var asklist: List<AskModel>? = null
    var bidlist: List<BidModel>? = null

    data class AskModel(
        val num: String,
        val price: String,
        val qty: String
    )

    data class BidModel(
        val num: String,
        val price: String,
        val qty: String
    )
}