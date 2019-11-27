package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.model.F10FinacialPieChartData
import com.zhuorui.securities.market.net.response.FinancialReportResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/6
 * Desc:
 */
interface MarketDetailF10FinancialView :AbsView{
  fun updataBuisnessData(data: F10FinacialPieChartData)
  fun updataProfitListData(profitList: List<FinancialReportResponse.CashFlowReport>)
  fun updataProfitChatData(profitList: List<FinancialReportResponse.ProfitReport>)
  fun updataOutProfitChatData(profitList: List<FinancialReportResponse.LiabilistyReport>)
  fun updataErrorData()
}