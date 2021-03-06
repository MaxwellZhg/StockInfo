package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.F10FinacialPieChartData
import com.zhuorui.securities.market.net.response.FinancialReportResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/6
 * Desc:
 */
class MarketDetailF10FinancialViewModel :ViewModel(){
  var pieChartData : MutableLiveData<LinkedHashMap<Long,ArrayList<FinancialReportResponse.BusinessReport>>> =MutableLiveData()
  var crashLineData : MutableLiveData<MutableList<FinancialReportResponse.CashFlowReport>> =MutableLiveData()
  var profitData : MutableLiveData<MutableList<FinancialReportResponse.ProfitReport>> =MutableLiveData()
  var outProfitData : MutableLiveData<MutableList<FinancialReportResponse.LiabilistyReport>> =MutableLiveData()
  var modifyPieChatData:MutableLiveData<F10FinacialPieChartData> =MutableLiveData()
}