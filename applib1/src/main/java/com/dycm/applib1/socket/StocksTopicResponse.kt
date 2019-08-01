package com.dycm.applib1.socket

import com.dycm.applib1.model.StockData

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/23 15:49
 * desc   : websocket订阅自选股推送返回的信息
 */
class StocksTopicResponse(
    val header: SocketHeader,
    val body: Boby
) {

    data class Boby(
        val dataType: String,// 数据类型(1:行情,2:K线,3:盘口,4:股价)
        val stockData: ArrayList<StockData> // 股票数据
    )
}
