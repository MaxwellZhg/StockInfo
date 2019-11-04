package com.zhuorui.securities.market.model

import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/6 11:18
 *    desc   : 推送股价数据
 */
class PushStockPriceData : BaseStockMarket() {
    /**
     * 开盘价格
     */
    var open: BigDecimal? = null
    /**
     * 昨日收盘价格
     */
    var preClose: BigDecimal? = null
    /**
     * 当前价格
     */
    var last: BigDecimal? = null
    /**
     * 涨跌标识(1:涨，0:平，-1:跌)
     */
    var pctTag: Int? = null
    /**
     * 行情时间
     */
    var time: Long? = null
}