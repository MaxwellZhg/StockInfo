package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.model.F10FinacialPieChartData
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.FinancialReportRequest
import com.zhuorui.securities.market.net.request.GetAllAttachmentRequest
import com.zhuorui.securities.market.net.response.FinancialReportResponse
import com.zhuorui.securities.market.net.response.GetAllAttachmentResponse
import com.zhuorui.securities.market.net.response.MarketNewsListResponse
import com.zhuorui.securities.market.ui.view.MarketDetailF10FinancialView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailF10FinancialViewModel
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/6
 * Desc:
 */
class MarketDetailF10FinancialPresenter :AbsNetPresenter<MarketDetailF10FinancialView,MarketDetailF10FinancialViewModel>(){
    override fun init() {
        super.init()
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.modifyPieChatData?.observe(it,
                androidx.lifecycle.Observer<F10FinacialPieChartData> { t ->
                    view?.updataBuisnessData(t)
                })
            viewModel?.crashLineData?.observe(it,
                androidx.lifecycle.Observer<List<FinancialReportResponse.CashFlowReport>> { t ->
                    view?.updataProfitListData(t)
                })
            viewModel?.profitData?.observe(it,
                androidx.lifecycle.Observer<List<FinancialReportResponse.ProfitReport>> { t ->
                    view?.updataProfitChatData(t)
                })
            viewModel?.outProfitData?.observe(it,
                androidx.lifecycle.Observer<List<FinancialReportResponse.LiabilistyReport>> { t ->
                    view?.updataOutProfitChatData(t)
                })
        }
    }

    fun getFinancialData(code:String?,ts:String?){
        val requset =  FinancialReportRequest(code,ts,transactions.createTransaction())
        requset?.let {
            Cache[IStockNet::class.java]?.getFinancialReport(requset)?.enqueue(Network.IHCallBack<FinancialReportResponse>(requset))
        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onFinancialData(response: FinancialReportResponse) {
        if (!transactions.isMyTransaction(response)) return
        val datas = response.data ?: return
        var listDate: ArrayList<String> = ArrayList()
        var pieData: F10FinacialPieChartData? = null
        var bussinessReport: ArrayList<ArrayList<FinancialReportResponse.BusinessReport>> = ArrayList()
        datas.mainBusinessReport?.forEach { (key, value) ->
            try {
                if(TimeZoneUtil.timeFormat(key, "yyyy-MM-dd").contains("12")){
                    listDate.add(TimeZoneUtil.timeFormat(key, "yyyy")+"年报")
                }else if(TimeZoneUtil.timeFormat(key, "yyyy-MM-dd").contains("06")){
                    listDate.add(TimeZoneUtil.timeFormat(key, "yyyy")+"中报")
                }
                bussinessReport.add(value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if(datas.profitReport.size>0) {
            pieData = F10FinacialPieChartData(listDate, bussinessReport,datas.profitReport[0].currency)
        }else{
            pieData = F10FinacialPieChartData(listDate, bussinessReport,"--")
        }
        viewModel?.modifyPieChatData?.value = pieData
        viewModel?.pieChartData?.value = datas.mainBusinessReport
        viewModel?.crashLineData?.value = datas.cashFlowReport
        viewModel?.profitData?.value = datas.profitReport
        viewModel?.outProfitData?.value = datas.liabilistyReport
    }

    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
         view?.updataErrorData()
    }
}