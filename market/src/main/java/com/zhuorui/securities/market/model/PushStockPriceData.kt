package com.zhuorui.securities.market.model

import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/6 11:18
 *    desc   : 推送股价数据
 */
class PushStockPriceData  : BaseStockMarket() {
    /**
     * 开盘价格
     */
    var openPrice: BigDecimal? = null
    /**
     * 昨日收盘价格
     */
    var preClosePrice: BigDecimal? = null
    /**
     * 当前价格
     */
    var price: BigDecimal? = null
    /**
     * 日期时间
     */
    var dateTime: Long? = null
}