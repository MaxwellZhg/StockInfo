package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.market.model.BaseStockMarket
import java.math.BigDecimal

/**
 */
class FundAccountResponse(val data: Data) : BaseResponse() {

    data class Data(
        val id: String?,
        val userId: String?,
        val stockMarket: String?,
        val type: String?,
        val status: String?,
        val riskLevel: String?,
        val currency: String?,
        val totalAmount: BigDecimal?,
        val stockTotalAmount: BigDecimal?,
        val balanceAmount: BigDecimal?,
        val desirableAmount: BigDecimal?,
        val availableAmount: BigDecimal?,
        val freezeAmount: BigDecimal?,
        val version: Int?,
        val createTime: String?,
        val updateTime: String
    )
}