package com.zhuorui.securities.market.socket.vo

import com.zhuorui.securities.market.model.BaseStockMarket
import java.math.BigDecimal

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-11-20 14:39
 *    desc   :
 */
class StockHandicapData(
    var amplitude:String?,//振幅
    var averagePrice:Float?,//平均价
    var comparison:String?,//委比
    var diffPrice:Float?,//涨跌价格
    var diffRate:Float?,//涨跌率
    var dividendRateFLY:String?,//股息率FLY
    var dividendRateTTM:String?,//股息率TTM
    var equityHK:Double?,//港股股本
    var fiftyTwoWeeksHigh:Float?,//52周最高
    var fiftyTwoWeeksLow:Float?,//52周最低
    var hands:Int?,//每手数
    var high:Float?,//最高价
    var last:Float?,//最新价
    var low:Float?,//最低价
    var marketRate:Float?,//市净率
    var open:Float?,//开盘价
    var peRatioStatic:Float?,//市盈率静
    var peRatioTTM:Float?,//市盈率ttm
    var preClose:Float?,//昨收价
    var sharestraded:BigDecimal?,//成交量
    var time:Long?,
    var totalCapitalStock:BigDecimal?,//总股本
    var totalMarkValue:BigDecimal?,//总市值
    var totalMarkValueHK:Double?,//港股总市值
    var turnover:BigDecimal?,//成交额
    var turnoverRate:String?,//换手率
    var volumeRatio:String?//量比

): BaseStockMarket(){

}