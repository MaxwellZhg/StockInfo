package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import java.math.BigDecimal

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/12
 * Desc:
 */
class FinancialReportResponse (val data:Data):BaseResponse(){
    data class Data(
        val mainBusinessReport:Business,
        val profitReport:ArrayList<ProfitReport>,
        val liabilistyReport:ArrayList<LiabilistyReport>,
        val cashFlowReport:ArrayList<CashFlowReport>
    )
    data class Business(
        val `20161231`: ArrayList<BusinessReport>,
        val `20170630`: ArrayList<BusinessReport>,
        val `20171231`: ArrayList<BusinessReport>,
        val `20180630`: ArrayList<BusinessReport>,
        val `20181231`: ArrayList<BusinessReport>,
        val `20190630`: ArrayList<BusinessReport>
    )
    data class BusinessReport(
       val  yearEndDate:String,
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