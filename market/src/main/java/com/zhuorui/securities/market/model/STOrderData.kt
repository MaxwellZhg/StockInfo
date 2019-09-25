package com.zhuorui.securities.market.model

import android.graphics.Color
import android.os.Parcelable
import com.zhuorui.securities.market.R
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-18 18:02
 *    desc   : 模拟炒股订单数据
 */
@Parcelize
open class STOrderData : Parcelable {
    @IgnoredOnParcel
    var id: String? = null //委托/持仓记录id
    @IgnoredOnParcel
    var ts: String? = null //股票类型(SZ-深圳,SH-上海.HK-港股,US-美股)
    @IgnoredOnParcel
    var code: String? = null //股票代码
    @IgnoredOnParcel
    var stockName: String? = null //股票名称
    @IgnoredOnParcel
    var holdStockCount: BigDecimal? = null //股票数量
    @IgnoredOnParcel
    var saleStockCount: BigDecimal? = null //可卖股票数量
    @IgnoredOnParcel
    var holeCost: BigDecimal? = null //股票价格/成本/订单价
    @IgnoredOnParcel
    var createTime: String? = null //下单日期
    @IgnoredOnParcel
    var trustType: Int? = null //委托类型1-买入 2-卖出 11-撤买入 12-撤卖出 21-申购新股
    @IgnoredOnParcel
    var status: Int? = null //交易状态(1未成交 2已成交 3部分成交 4场内撤单 5场外撤单 6 系统撤单  11撤单已成 21申购新股已报)
    @IgnoredOnParcel
    var statusName: String? = null
        get() {
            if (field == null) {
                initStatus()
            }
            return field
        }
    @IgnoredOnParcel
    var statusLogo: Int? = null
        get() {
            if (field == null) {
                initStatus()
            }
            return field
        }
    @IgnoredOnParcel
    var trustName: String? = null
        get() {
            if (field == null) {
                initTrust()
            }
            return field
        }
    @IgnoredOnParcel
    var trustColor: Int? = null
        get() {
            if (field == null) {
                initTrust()
            }
            return field
        }
    @IgnoredOnParcel
    var selected:Boolean? = null
    private fun initStatus() {
        when (status) {
            1 -> {
                statusName = "等待成交"
                statusLogo = R.mipmap.ic_history_blue_circle_small
            }
            2 -> {
                statusName = "已成交"
                statusLogo = R.mipmap.ic_checklist_green_circle
            }
            3 -> {
                statusName = "部分成交"
                statusLogo = R.mipmap.ic_history_blue_circle_small
            }
            4 -> {
                statusName = "场内撤单"
                statusLogo = R.mipmap.ic_warningic_red_circle
            }
            5 -> {
                statusName = "场外撤单"
                statusLogo = R.mipmap.ic_warningic_red_circle
            }
            6 -> {
                statusName = "系统撤单"
                statusLogo = R.mipmap.ic_warningic_red_circle
            }
            7 -> {
                statusName = " 已改单"
                statusLogo = R.mipmap.ic_warningic_red_circle
            }
            11 -> {
                statusName = "撤单已成"
                statusLogo = R.mipmap.ic_warningic_red_circle
            }
            21 -> {
                statusName = "申购新股已报"
                statusLogo = R.mipmap.ic_warningic_red_circle
            }
        }
    }

    private fun initTrust() {
        when (trustType) {
            1 -> {
                trustName = "买入"
                trustColor = Color.parseColor("#1A6ED2")
            }
            2 -> {
                trustName = "卖出"
                trustColor = Color.parseColor("#FF8E1B")
            }
            11 -> {
                trustName = "买入"
                trustColor = Color.parseColor("#1A6ED2")
            }
            12 -> {
                trustName = "卖出"
                trustColor = Color.parseColor("#FF8E1B")
            }
            21 -> {
                trustName = "申购新股"
                trustColor = Color.parseColor("#232323")
            }
        }
    }
}