package com.dycm.applib1.socket.vo.kline

import com.dycm.applib1.model.BaseStockMarket

/**
 * 分时数据
 */
class StockMinuteVo : BaseStockMarket() {

    var data: ArrayList<MinuteKlineData>? = null

}
