package com.zhuorui.securities.market.socket.push

import com.zhuorui.securities.market.socket.request.SocketHeader
import com.zhuorui.securities.market.socket.response.SocketResponse
import com.zhuorui.securities.market.socket.vo.StockHandicapData

/**
 *    author : liuwei
 *    e-mail :
 *    date   :
 *    desc   : websocket订阅自选股推送股票盘口数据
 */
class StocksTopicHandicapResponse : SocketResponse() {
    var header: SocketHeader? = null
    var body: String? = null
}
