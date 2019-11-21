package com.zhuorui.securities.market.socket.vo

import com.zhuorui.securities.market.model.BaseStockMarket

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-11-20 14:39
 *    desc   : 资金统计数据
 */
class CapitalData(
    var cacheDay:String?,//开盘数据时间
    var latestTrends:Double?,//今日资金流向趋势数据(一分钟一条)
    var totalLargeSingleInflow:Double?,//大单流入总额
    var totalLargeSingleOutflow:Double?,//大单流出总额
    var totalMediumInflow:Double?,//中单流入总额
    var totalMediumOutflow:Double?,//中单流出总额
    var totalSmallInflow:Double?,//小单流入总额
    var totalSmallOutflow:Double?,//小单流出总额
    var maps:List<Double>?//最新总趋势金额（近一分钟）

): BaseStockMarket(){

}