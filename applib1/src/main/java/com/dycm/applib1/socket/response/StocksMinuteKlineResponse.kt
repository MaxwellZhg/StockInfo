package com.dycm.applib1.socket.response

import com.dycm.applib1.socket.vo.kline.MinuteKlineData

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/23 15:49
 * desc   : websocket拉取自选股K线补偿数据
 */
class StocksMinuteKlineResponse : SocketResponse() {
    var data: ArrayList<MinuteKlineData>? = null
}