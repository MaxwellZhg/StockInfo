package com.zhuorui.securities.market.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/25 18:36
 *    desc   : 搜索返回的自选股信息
 */

@Parcelize
open class SearchStockInfo : BaseStockMarket(), Parcelable {

    // 股票代码 600004.SH
    @IgnoredOnParcel
    var tsCode: String? = null
    // 名称
    @IgnoredOnParcel
    var name: String? = null
    // 是否收藏
    @IgnoredOnParcel
    var collect: Boolean = false
    // 当前价格：如13.75
    @IgnoredOnParcel
    var last: BigDecimal? = null
    // 昨收
    @IgnoredOnParcel
    var preClose: BigDecimal? = null
    // 涨跌幅：如-0.0018（-0.18%）
    @IgnoredOnParcel
    var diffRate: BigDecimal? = null
    // 股市状态
    @IgnoredOnParcel
    var suspension: StockSuspension? = StockSuspension.empty
}