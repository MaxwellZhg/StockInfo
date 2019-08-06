package com.dycm.applib1.socket.response

import com.dycm.applib1.socket.request.SocketHeader
import com.dycm.applib1.socket.vo.kline.MinuteKlineData
import com.dycm.applib1.socket.vo.kline.PushKlineDataVo

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/23 15:49
 * desc   : websocket订阅自选股K线推送返回的信息
 */
class StocksTopicMinuteKlineResponse {
    var header: SocketHeader? = null
    var body: PushKlineDataVo<MinuteKlineData>? = null
}