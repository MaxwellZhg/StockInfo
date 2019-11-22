package com.zhuorui.securities.market.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/8 14:08
 *    desc   : 自选股行情信息
 */
@Parcelize
class StockMarketInfo : SearchStockInfo(), Parcelable {

    //登录后拉取列表会返回id
    @IgnoredOnParcel
    var id: String? = null
    // 排序
    @IgnoredOnParcel
    var sort: Int = 0
    // 当前价格：如13.75
    @IgnoredOnParcel
    var last: BigDecimal? = null
    // 跌涨价格：如1.33
    @IgnoredOnParcel
    var diffPrice: BigDecimal? = null
    // 涨跌幅：如-0.0018（-0.18%）
    @IgnoredOnParcel
    var diffRate: BigDecimal? = null
    // 创建时间
    @IgnoredOnParcel
    var createTime: Long = 0
    // 股市状态
    @IgnoredOnParcel
    var suspension: StockSuspension = StockSuspension.empty
    // 涨跌标识(1:涨，0:平，-1:跌)
    @IgnoredOnParcel
    var pctTag: Int? = null
    // 长按
    @IgnoredOnParcel
    var longClick: Boolean = false

    companion object {
        /**
         * 将源对象中的属性赋值给目标对象
         */
        fun copyProperties(origin: StockMarketInfo, target: StockMarketInfo) {
            target.id = origin.id
            target.ts = origin.ts
            target.code = origin.code
            target.tsCode = origin.tsCode
            target.name = origin.name
            target.type = origin.type
            target.sort = origin.sort
            target.last = origin.last
            target.diffPrice = origin.diffPrice
            target.diffRate = origin.diffRate
            target.createTime = origin.createTime
            target.longClick = origin.longClick
        }
    }
}