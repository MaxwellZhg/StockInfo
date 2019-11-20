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
open class SearchStockInfo : BaseStockMarket(), Parcelable {

    @IgnoredOnParcel
    var tsCode: String? = null//股票代码 600004.SH
    @IgnoredOnParcel
    var name: String? = null//名称
    @IgnoredOnParcel
    var collect:Boolean =false//是否收藏
}