package com.dycm.applib1.socket

import com.dycm.applib1.model.SocketPushStockInfo

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/23 15:49
 * desc   : 股票行情信息
 */
class StocksResponse {
    var header: SocketHeader? = null
    var body :SocketPushStockInfo? = null
}
