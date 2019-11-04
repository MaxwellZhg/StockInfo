package com.zhuorui.securities.market.socket.response

import com.zhuorui.securities.market.model.PushStockPriceData

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/4 14:19
 *    desc   : 查询股票价格数据
 */
class GetStockPriceResponse : SocketResponse() {
    var data: List<PushStockPriceData>? = null
}