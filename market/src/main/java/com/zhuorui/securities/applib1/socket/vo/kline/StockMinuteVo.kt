package com.zhuorui.securities.applib1.socket.vo.kline

import com.zhuorui.securities.applib1.model.BaseStockMarket

/**
 * 分时数据
 */
class StockMinuteVo : BaseStockMarket() {

    var data: List<MinuteKlineData>? = null

}
