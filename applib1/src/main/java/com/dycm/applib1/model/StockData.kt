package com.dycm.applib1.model

/**
 * 描述一支自选股的信息
 */
data class StockData(
    val amount: Int,
    val avgPrice: Double,
    val closePrice: Double,
    val code: String,// 股票代码
    val name: String,// 股票名称
    val date: String,
    val high: Double,
    val low: Double,
    val openPrice: Double,
    val price: Double,
    val time: String,
    val ts: String,// 属于的股票市场(SZ-深圳,SH-上海,HK-港股,US-美股)
    val type: Int,// 类型 1:指数,2:股票
    val vol: Int
)