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
        val mainBusinessReport:LinkedHashMap<Long,ArrayList<BusinessReport>>?,//主营业务年度营业报表数据
        val profitReport:ArrayList<ProfitReport>,//利润报表数据
        val liabilistyReport:ArrayList<LiabilistyReport>,//资产负债报表数据
        val cashFlowReport:ArrayList<CashFlowReport>//现金流量报表数据
    )
    data class BusinessReport(
       val yearEndDate:String,// 年报日
       val businessCate:String,//业务分类名称
       val currentYearTurnover:BigDecimal,//业务年度营业额
       val currentYearTurnoverRate:BigDecimal//业务年度营业额占总营业额比例
    )
    data class ProfitReport(
        val currency:String,// 货币
        val date:Long,// 报告日期
        val income:BigDecimal,// 收入
        val profit:BigDecimal,//除税后溢利
        val profitRate:BigDecimal//净利润率
    )
    data class LiabilistyReport(
        val currency:String,// 货币
        val date:Long,//报告日期
        val totalAssets:BigDecimal,// 总资产
        val totalLiability:BigDecimal,//总负债
        val liabilityRate:BigDecimal//资产负债率
    )
    data class CashFlowReport(
        val  currency:String,//货币
        val date:Long,//报告日期
        val netOperating:BigDecimal,//经营净额
        val netInvestment:BigDecimal,//投资净额
        val netFinancing:BigDecimal//融资净额
    )
}