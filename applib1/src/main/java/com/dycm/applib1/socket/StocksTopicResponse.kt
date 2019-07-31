package com.dycm.applib1.socket

import com.dycm.applib1.model.SocketPushStockInfo

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/23 15:49
 * desc   : websocket订阅自选股推送返回的信息
 */
class StocksTopicResponse {
    var header: SocketHeader? = null
    var body :SocketPushStockInfo? = null
}
