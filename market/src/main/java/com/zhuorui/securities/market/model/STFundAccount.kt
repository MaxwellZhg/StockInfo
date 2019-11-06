package com.zhuorui.securities.market.model

import android.text.TextUtils
import com.zhuorui.securities.market.customer.view.SimulationTradingFundAccountView
import java.math.BigDecimal

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-18 18:12
 *    desc   : 资金帐户数据
 */
class STFundAccountData(accountId: String, availableFund: BigDecimal){


    var totalAssets: BigDecimal = BigDecimal(0)
    var marketValue: BigDecimal = BigDecimal(0)
    var totalProfitAndLoss: Float = 0f
    var todayProfitAndLoss: Float = 0f
    var todayProfitAndLossPercentage:BigDecimal = BigDecimal(0)
    var availableFund:BigDecimal = availableFund//可用资金
    var accountId: String = accountId//资金帐户ID

    /**
     * 是否创建
     *
     * @return
     */
    fun isCreate(): Boolean {
        return !TextUtils.isEmpty(accountId)
    }

}