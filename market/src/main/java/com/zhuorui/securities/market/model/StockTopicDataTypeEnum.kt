package com.zhuorui.securities.market.model

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/31 17:47
 *    desc   : 订阅股票的数据类型
 */
enum class StockTopicDataTypeEnum(var value: String, var description: String) {
    DETAIL("1", "详情"),
    MINUTE("2-1", "K线-分时"),
    FIVE_DAY("2-2", "K线-五日"),
    DAY("2-3", "K线-日K"),
    WEEK("2-4", "K线-周K"),
    MONTH("2-5", "K线-月K"),
    SEASON("2-6", "K线-季K"),
    YEAR("2-7", "K线-年K"),
    MINUTE1("2-8", "K线-1分钟"),
    MINUTE3("2-9", "K线-3分钟"),
    MINUTE5("2-10", "K线-5分钟"),
    MINUTE15("2-11", "K线-15分钟"),
    MINUTE30("2-12", "K线-30分钟"),
    MINUTE60("2-13", "K线-60分钟"),
    MINUTE120("2-14", "K线-120分钟"),
    MINUTE240("2-15", "K线-240分钟"),
    HANDICAP("3", "盘口"),
    STOCK_PRICE("4", "股价"),
    STOCK_ORDER("5", "买卖盘，委托挂单"),
    STOCK_ORDER_BROKER("6", "买卖经纪席位"),
    STOCK_TRADE("7", "逐笔成交"),
    STOCK_TRADE_STA("8", "成交统计");
}