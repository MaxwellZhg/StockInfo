package com.zhuorui.securities.market.model

import java.io.Serializable
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/8 14:08
 *    desc   : 自选股行情信息
 */
class StockMarketInfo : SearchStockInfo(), Serializable {

    // 排序
    var sort: Int = 0
    // 当前价格：如13.75
    var price: BigDecimal? = null
    // 跌涨价格：如1.33
    var diffPrice: BigDecimal? = null
    // 涨跌幅：如-0.0018（-0.18%）
    var diffRate: BigDecimal? = null
    // 创建时间
    var createTime: Long = 0
    // 长按
    var longClick: Boolean = false


    override fun toString(): String {
        return "StockMarketInfo(id=$id, name=$name, sort=$sort, tsCode=$tsCode, price=$price, diffPrice=$diffPrice, diffRate=$diffRate, createTime=$createTime, longClick=$longClick)"
    }


}