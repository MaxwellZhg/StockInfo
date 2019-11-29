package com.zhuorui.securities.market.model

import com.zhuorui.securities.market.net.response.FinancialReportResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/27
 * Desc:F10财报饼图model
 */

class F10FinacialPieChartData (
    var listDate:ArrayList<String>,
    var listPieChart :ArrayList<ArrayList<FinancialReportResponse.BusinessReport>>,
    var currency:String// 货币
)