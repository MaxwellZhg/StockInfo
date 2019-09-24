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
class SearchStockInfo : BaseStockMarket(), IStocks, Parcelable {

    @IgnoredOnParcel
    var id: String? = null
    @IgnoredOnParcel
    var tsCode: String? = null
    @IgnoredOnParcel
    var name: String? = null

    override fun getIID(): String {
        return if (id == null) "" else id!!
    }

    override fun getIType(): Int {
        return if (type == null) 0 else type!!
    }

    override fun getIName(): String {
        return if (name == null) "" else name!!
    }

    override fun getICode(): String {
        return if (code == null) "" else code!!
    }

    override fun getITs(): String {
        return if (ts == null) "" else ts!!
    }

    override fun getITsCode(): String {
        return if (tsCode == null) "" else tsCode!!
    }

}