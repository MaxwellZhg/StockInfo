package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.market.model.BaseStockMarket
import java.math.BigDecimal

/**
 */
class FundAccountResponse(val data: Data) : BaseResponse() {

    data class Data(
        val availableFunds: Float
    )
}