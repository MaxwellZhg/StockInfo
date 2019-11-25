package com.zhuorui.securities.market.socket.vo

import com.zhuorui.securities.market.model.BaseStockMarket
import java.math.BigDecimal

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-11-20 14:39
 *    desc   : 资金统计数据
 */
class CapitalData(
    var cacheDay:String?,//开盘数据时间
    var latestTrends:BigDecimal?,//今日资金流向趋势数据(一分钟一条)
    var totalLargeSingleInflow:BigDecimal?,//大单流入总额
    var totalLargeSingleOutflow:BigDecimal?,//大单流出总额
    var totalMediumInflow:BigDecimal?,//中单流入总额
    var totalMediumOutflow:BigDecimal?,//中单流出总额
    var totalSmallInflow:BigDecimal?,//小单流入总额
    var totalSmallOutflow:BigDecimal?,//小单流出总额
    var maps:Map<String,BigDecimal>?//最新总趋势金额（近一分钟）

): BaseStockMarket(){

}