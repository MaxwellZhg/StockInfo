package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
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
            viewModel?.pieChartData?.observe(it,
                androidx.lifecycle.Observer<FinancialReportResponse.Business> { t ->
                    view?.updataBuisnessData(t)
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
    fun onFinancialData(response: FinancialReportResponse){
        if (!transactions.isMyTransaction(response)) return
        val datas = response.data
        viewModel?.pieChartData?.value =response.data.mainBusinessReport
    }
}