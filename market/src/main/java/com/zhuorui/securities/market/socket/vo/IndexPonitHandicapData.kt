package com.zhuorui.securities.market.socket.vo

import com.zhuorui.securities.market.model.BaseStockMarket
import java.math.BigDecimal

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/22
 * Desc:
 */
class IndexPonitHandicapData(
    var diffPrice: BigDecimal?,//涨跌价格
    var diffRate: BigDecimal?,//涨跌率
    var fall: Int?,//下跌股数
    var flatPlate: Int?,//平盘股数
    var high: BigDecimal?,//最高价
    var last: BigDecimal?,//最新价
    var low: BigDecimal?,//最低价
    var open: BigDecimal?,//开盘价
    var rise: Int?,//上涨股数
    var sharestraded: BigDecimal?,//成交量
    var time: Long?,//行情时间
    var turnover: BigDecimal?//成交额
) : BaseStockMarket() {

}


