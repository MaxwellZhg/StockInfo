package com.zhuorui.securities.market.socket.push

import com.zhuorui.securities.market.socket.response.SocketResponse
import com.zhuorui.securities.market.socket.vo.StockTradeDetailData

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/10/30 16:50
 *    desc   :  websocket订阅自选股逐笔成交数据推送返回的信息
 */
class StocksTopicTradeResponse : SocketResponse() {
    var body: StockTradeDetailData? = null
}