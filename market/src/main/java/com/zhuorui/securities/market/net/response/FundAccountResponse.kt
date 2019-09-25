package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import java.math.BigDecimal
import java.util.*

/**
 */
class FundAccountResponse(val data: Data) : BaseResponse() {

    data class Data(
        val id: String?,
        val userId: String?,
        val stockMarket: String?,//股票市场（1-港股 2-美股 3-A股）
        val type: String?,// 账户类型（1-普通 2-融资）
        val status: String?,//账户状态 （1正常 2冻结 3销户）
        val riskLevel: String?,//风险级别(默认为3) 1-C1保守型 2-C2稳健型 3-C3平衡型 4-C4增长型 5-C5进取型
        val currency: String?,//币种（HKD USD CNY）
        val totalAmount: BigDecimal?,//总资产
        val stockTotalAmount: BigDecimal?,//股票总资产
        val balanceAmount: BigDecimal?,//资金余额
        val desirableAmount: BigDecimal?,//可取资金
        val availableAmount: BigDecimal?,//可用资金
        val freezeAmount: BigDecimal?,//冻结资金
        val version: Int?,//版本号
        val cypherFlagTime: Date?,//资金计算日期(标记定时总资产等是否计算)
        val createTime: Date?,//创建时间
        val updateTime: Date//最后更新时间
    )
}