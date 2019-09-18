package com.zhuorui.securities.market.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/25 18:36
 *    desc   : 搜索返回的自选股信息
 */
@Parcelize
class SearchStockInfo : BaseStockMarket(), Parcelable {
    @IgnoredOnParcel
    var id: String? = null
    @IgnoredOnParcel
    var tsCode: String? = null
    @IgnoredOnParcel
    var name: String? = null
}