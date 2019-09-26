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
class STFundAccountData(accountId: String, availableFund: BigDecimal) :
    SimulationTradingFundAccountView.IFundAccountData {

    var totalAssets: Float? = null
    var marketValue: Float? = null
    var totalProfitAndLoss: Float? = null
    var todayProfitAndLoss: Float? = null
    var todayProfitAndLossPercentage: Float? = null
    var availableFund:BigDecimal = availableFund//可用资金
    var accountId: String = accountId//资金帐户ID

    /**
     * 是否创建
     *
     * @return
     */
    override fun isCreate(): Boolean {
        return !TextUtils.isEmpty(accountId)
    }

    /**
     * 帐户总资产
     *
     * @return
     */
    override fun getTotalAssets(): Float {
        return if (totalAssets != null) totalAssets!! else 0.00f
    }

    /**
     * 持仓市值
     *
     * @return
     */
    override fun getMarketValue(): Float {
        return if (marketValue != null) marketValue!! else 0.00f
    }

    /**
     * 可用资金
     *
     * @return
     */
    override fun getAvailableFunds(): BigDecimal {
        return availableFund
    }

    /**
     * 总盈亏金额
     *
     * @return
     */
    override fun getTotalProfitAndLoss(): Float {
        return if (totalProfitAndLoss != null) totalProfitAndLoss!! else 0.00f
    }

    /**
     * 当日盈亏
     *
     * @return
     */
    override fun getTodayProfitAndLoss(): Float {
        return if (todayProfitAndLoss != null) todayProfitAndLoss!! else 0.00f
    }

    /**
     * 当日盈亏比例
     *
     * @return
     */
    override fun getTodayProfitAndLossPercentage(): Float {
        return if (todayProfitAndLossPercentage != null) todayProfitAndLossPercentage!! else 0.00f
    }

}