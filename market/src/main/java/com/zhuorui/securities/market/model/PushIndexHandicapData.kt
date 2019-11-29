package com.zhuorui.securities.market.model

import java.math.BigDecimal

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/25
 * Desc:指数盘口涨跌推送数据
 * */
class PushIndexHandicapData(
    var last: BigDecimal?,//最新股价
    var diffPrice: BigDecimal?,//涨跌价格
    var diffRate: BigDecimal?,//涨跌率
    var time: Long?,//行情时间
    var high: BigDecimal?,//最高价
    var open: BigDecimal?,//开盘价
    var low: BigDecimal?,//最低价
    var preClose: BigDecimal?,//昨收价
    var sharestraded: BigDecimal?,//成交量
    var amplitude: String?,//成交量
    var volumeRatio: String?,//行情时间
    var averagePrice: BigDecimal?,//成交额
    var rise: Int?,//上涨
    var fall: Int?,//下跌
    var flatPlate: Int?//平盘
) :BaseStockMarket(){

}