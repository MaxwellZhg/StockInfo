package com.dycm.applib1.socket.response

import com.dycm.applib1.model.PushStockMarketData
import com.dycm.applib1.socket.request.SocketHeader

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/23 15:49
 * desc   : websocket订阅自选股行情推送返回的信息
 */
class StocksTopicMarketResponse(
    val header: SocketHeader,
    val body:  ArrayList<PushStockMarketData>
)