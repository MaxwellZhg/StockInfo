package com.dycm.applib1.socket.vo.kline

import java.math.BigDecimal

/**
 * 分时数据基础对象
 */
class MinuteKlineData {
    /**
     * 交易量
     */
    var vol: BigDecimal? = null

    /**
     * 交易金额
     */
    var amount: BigDecimal? = null

    /**
     * 开盘价格
     */
    var openPrice: BigDecimal? = null

    /**
     * 均价
     */
    var avgPrice: BigDecimal? = null

    /**
     * 当前价格
     */
    var price: BigDecimal? = null

    /**
     * 今日最高价
     */
    var high: BigDecimal? = null

    /**
     * 今日最低价
     */
    var low: BigDecimal? = null

    /**
     * 日期时间
     */
    var dateTime: Long? = null
}
