package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import java.math.BigDecimal

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/12
 * Desc:财报返回response
 */
class FinancialReportResponse (val data:Data):BaseResponse(){
    data class Data(
        val mainBusinessReport:HashMap<String,ArrayList<BusinessReport>>,
        val profitReport:ArrayList<ProfitReport>,
        val liabilistyReport:ArrayList<LiabilistyReport>,
        val cashFlowReport:ArrayList<CashFlowReport>
    )
    data class BusinessReport(
       val yearEndDate:String,
       val businessCate:String,
       val currentYearTurnover:BigDecimal,
       val currentYearTurnoverRate:BigDecimal
    )
    data class ProfitReport(
        val currency:String,
        val date:Long,
        val income:BigDecimal,
        val profit:BigDecimal,
        val profitRate:BigDecimal
    )
    data class LiabilistyReport(
        val currency:String,
        val date:Long,
        val totalAssets:BigDecimal,
        val totalLiability:BigDecimal,
        val liabilityRate:BigDecimal
    )
    data class CashFlowReport(
        val  currency:String,
        val date:Long,
        val netOperating:BigDecimal,
        val netInvestment:BigDecimal,
        val netFinancing:BigDecimal
    )
}