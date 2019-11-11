package com.zhuorui.securities.market.model

import java.math.BigDecimal

/**
 * 股东变动
 */
data class F10ShareHolderModel(
    val name: String,// 股东名称
    val holdStockNumber: BigDecimal,// 持股数(股)
    val holdStockRatio: BigDecimal,// 持股比例
    val changeNumber: BigDecimal,// 变动
    val date: String,// 日期
    val changeType: Int// 变化类型(1增持/2减持)
)
