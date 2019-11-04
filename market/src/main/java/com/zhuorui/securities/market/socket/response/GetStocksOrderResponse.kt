package com.zhuorui.securities.market.socket.response

import com.zhuorui.securities.market.socket.vo.OrderData

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/23 15:49
 * desc   : 查询最新委托挂单数据
 */
class GetStocksOrderResponse : SocketResponse() {
    var data: OrderData? = null
}