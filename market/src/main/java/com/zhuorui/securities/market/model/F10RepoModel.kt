package com.zhuorui.securities.market.model

import java.math.BigDecimal

/**
 * 公司回购
 */
data class F10RepoModel(
    val date: String,// 回购日
    val number: BigDecimal,// 数量(股)
    val avgPrice: BigDecimal,// 均价
    val repoRatio: BigDecimal// 股份回购占总成交量比例
)