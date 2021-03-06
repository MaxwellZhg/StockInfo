package com.zhuorui.securities.market.socket.response

import com.zhuorui.securities.market.socket.vo.StockTradeStaData

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/4 14:19
 *    desc   : 查询逐笔成交统计数据
 */
class GetStockTradeStaResponse : SocketResponse() {
    var data: MutableList<StockTradeStaData>? = null
}