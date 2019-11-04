package com.zhuorui.securities.market.socket.push

import com.zhuorui.securities.market.socket.request.SocketHeader
import com.zhuorui.securities.market.socket.response.SocketResponse
import com.zhuorui.securities.market.socket.vo.OrderData

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/11/4 11:13
 * desc   :websocket订阅自选股推送委托挂单数据(买卖盘)
 */
class StocksTopicOrderResponse : SocketResponse() {
    var header: SocketHeader? = null
    var body: OrderData? = null
}
