package com.zhuorui.securities.market.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

/**
 * 高管信息
 */
@Parcelize
class F10ManagerModel : Parcelable {
    @IgnoredOnParcel
    var name: String? = null // 名称
    @IgnoredOnParcel
    var jobTitle: String? = null// 职务
    @IgnoredOnParcel
    var salary: BigDecimal? = null// 薪酬
    @IgnoredOnParcel
    var currency: String? = null// 币种
}