package com.zhuorui.securities.market.socket.push

import com.zhuorui.securities.market.socket.request.SocketHeader
import com.zhuorui.securities.market.socket.response.SocketResponse
import com.zhuorui.securities.market.socket.vo.CapitalData
import com.zhuorui.securities.market.socket.vo.OrderBrokerData
import com.zhuorui.securities.market.socket.vo.StockHandicapData

/**
 *    author : liuwei
 *    e-mail :
 *    date   :
 *    desc   : websocket订阅自选股推送资金统计数据
 */
class StocksTopicCapitalResponse : SocketResponse() {
    var header: SocketHeader? = null
    var body: String? = null
}
