package com.zhuorui.securities.market.socket.push

import com.zhuorui.securities.market.socket.request.SocketHeader
import com.zhuorui.securities.market.socket.response.SocketResponse

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/11/4 11:13
 * desc   :websocket订阅自选股推送委托挂单数据(买卖盘)
 */
class StocksTopicOrderResponse : SocketResponse() {
    var header: SocketHeader? = null
    var body: Body? = null

    data class Body(
        val ts: String,
        val code: String,
        val type: Int,
        val asklist: List<AskModel>,
        val bidlist: List<BidModel>
    )

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
