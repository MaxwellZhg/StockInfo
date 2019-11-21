package com.zhuorui.securities.market.socket.response

import com.zhuorui.securities.market.socket.vo.StockHandicapData

/**
 *    author : liuwei
 *    e-mail :
 *    date   :
 *    desc   : 查询股票盘口数据
 */
class GetStockHandicapResponse : SocketResponse() {
    var data: StockHandicapData? = null
}