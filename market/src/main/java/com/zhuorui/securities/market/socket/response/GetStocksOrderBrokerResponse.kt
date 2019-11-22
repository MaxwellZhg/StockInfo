package com.zhuorui.securities.market.socket.response

import com.zhuorui.securities.market.socket.vo.OrderBrokerData

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/23 15:49
 * desc   : 查询最新买卖经纪数据
 */
class GetStocksOrderBrokerResponse(val data: Data?) : SocketResponse() {

    data class Data(val buy: OrderBrokerData?, val sell: OrderBrokerData?)

}