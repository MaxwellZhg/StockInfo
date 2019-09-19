package com.zhuorui.securities.market.model

import android.graphics.Color
import com.zhuorui.securities.market.R

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-18 18:02
 *    desc   : 模拟炒股订单数据
 */
open class STOrderData {
    var id: String? = null //委托/持仓记录id
    var stockType: String? = null //股票类型(SZ-深圳,SH-上海.HK-港股,US-美股)
    var stockCode: String? = null //股票代码
    var stockName: String? = null //股票名称
    var holdStockCount: Int? = null //股票数量
    var holeCost: Float? = null //股票价格/成本/订单价
    var createDate: Long? = null //下单日期
    var trustType: Int? = null //委托类型1-买入 2-卖出 11-撤买入 12-撤卖出 21-申购新股
    var status: Int? = null //交易状态(1未成交 2已成交 3部分成交 4场内撤单 5场外撤单 6 系统撤单  11撤单已成 21申购新股已报)
    var statusName: String? = null
        get() {
            if (field == null) {
                initStatus()
            }
            return field
        }
    var statusLogo: Int? = null
        get() {
            if (field == null) {
                initStatus()
            }
            return field
        }
    var trustName: String? = null
        get() {
            if (field == null) {
                initTrust()
            }
            return field
        }
    var trustColor: Int? = null
        get() {
            if (field == null) {
                initTrust()
            }
            return field
        }
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