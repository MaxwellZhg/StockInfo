package com.zhuorui.securities.market.socket.push

import com.zhuorui.securities.market.socket.request.SocketHeader
import com.zhuorui.securities.market.socket.response.SocketResponse
import com.zhuorui.securities.market.socket.vo.OrderBrokerData

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/4 11:29
 *    desc   : websocket订阅自选股推送买卖经纪席位数据(买卖盘席位)
 */
class StocksTopicOrderBrokerResponse : SocketResponse() {
    var header: SocketHeader? = null
    var body: OrderBrokerData? = null
}
