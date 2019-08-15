package com.zhuorui.securities.applib1.socket.push

import com.zhuorui.securities.applib1.model.PushStockMarketData
import com.zhuorui.securities.applib1.socket.request.SocketHeader

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/23 15:49
 * desc   : websocket订阅自选股盘口推送返回的信息
 */
class StocksTopicTransResponse(
    val header: SocketHeader,
    val body: PushStockMarketData
)