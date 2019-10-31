package com.zhuorui.securities.market.socket.vo

import com.zhuorui.securities.market.model.BaseStockMarket

import java.math.BigDecimal


/**
 * 股票逐笔明细统计
 *
 * TradeVo
 * @author zhanglong
 * @date 2019年10月30日
 */
class StockTradeStaData : BaseStockMarket() {
    /**
     * 唯一id
     */
    var recordId: String? = null
    /**
     * 成交价
     */
    var price: BigDecimal? = null

    /**
     * 与昨收价对比上涨还是下跌
     */
    var diffPreMark: Int? = null

    //1.该价总成交百分比=该价当天成交量/该股票当天总成交量
    //2.总柱子长度占比=该价总成交百分比/页面展示的最大总成交百分比
    //3.红色柱子长度=该价当天主买成交量/该股票当天总成交量/页面展示的最大总成交百分比
    //4.绿色柱子长度=该价当天主卖成交量/该股票当天总成交量/页面展示的最大总成交百分比
    //5.灰色柱子长度=该价当天中性盘成交量/该股票当天总成交量/页面展示的最大总成交百分比

    /**
     * 该价当天主买成交量
     */
    var todayBuyQty: BigDecimal? = null

    /**
     * 该价当天主卖成交量
     */
    var todaySellQty: BigDecimal? = null

    /**
     * 该价当天中性盘成交量
     */
    var todayUnchangeQty: BigDecimal? = null

    /**
     * 该价当天成交量
     */
    var todayQty: BigDecimal? = null

    /**
     * 该股票当天总成交量
     */
    var todayTotalQty: BigDecimal? = null

    /**
     * 1 新增 2 修改
     */
    var saveType: Int = 0

}
