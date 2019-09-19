package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.model.*
import com.zhuorui.securities.market.net.ISimulationTradeNet
import com.zhuorui.securities.market.net.request.FundAccountRequest
import com.zhuorui.securities.market.net.request.GetPositionRequest
import com.zhuorui.securities.market.net.request.OrderListRequest
import com.zhuorui.securities.market.net.response.*
import com.zhuorui.securities.market.ui.view.SimulationTradingMainView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingMainViewModel
import com.zhuorui.securities.market.util.MathUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.RuntimeException

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 16:03
 *    desc   :
 */
class SimulationTradingMainPresenter : AbsNetPresenter<SimulationTradingMainView, SimulationTradingMainViewModel>() {

    var positionDatas: List<STPositionData>? = null
    var stocksInfo: HashMap<String, GetStockInfoResponse.Data>? = null

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.positionDatas?.observe(it,
                androidx.lifecycle.Observer<List<STPositionData>> { t ->
                    view?.onPositionDatas(t)
                })
            viewModel?.orderDatas?.observe(it,
                androidx.lifecycle.Observer<List<STOrderData>> { t ->
                    view?.onOrderDatas(t)
                })
            viewModel?.fundAccountData?.observe(it,
                androidx.lifecycle.Observer<STFundAccountData> { t ->
                    view?.onFundAccountData(t)
                })
        }
    }

    /**
     * 查询资金账户接口
     */
    fun getFundAccount() {
        val request = FundAccountRequest("", transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.getFundAccount(request)
            ?.subscribeOn(Schedulers.io())?.observeOn(
                AndroidSchedulers.mainThread()
            )
            ?.subscribe(io.reactivex.functions.Consumer {
                if (it.isSuccess()) {
                    getPosition()
                } else {
                }
            }, io.reactivex.functions.Consumer {

            })

    }

    /**
     * 创建资金账号
     */
    fun createFundAccount() {
        val request = FundAccountRequest("", transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.createFundAccount(request)
            ?.flatMap { t ->
                if (t.isSuccess()) {
                    val request = FundAccountRequest("", transactions.createTransaction())
                    Cache[ISimulationTradeNet::class.java]?.getFundAccount(request)
                } else {
                    Observable.create(ObservableOnSubscribe<FundAccountResponse> { emitter ->
                        emitter.onError(RuntimeException(t.msg))
                        emitter.onComplete()
                    })
                }
            }?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(io.reactivex.functions.Consumer {
                if (it.isSuccess()) {
                    getPosition()
                    view?.createFundAccountSuccess()
                } else {

                }
            }, io.reactivex.functions.Consumer {

            })

    }

    /**
     * 获取持仓
     */
    fun getPosition() {
        val request = GetPositionRequest("", "", transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.getPosition(request)
            ?.enqueue(Network.IHCallBack<GetPositionResponse>(request))
        view?.createFundAccountSuccess()
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetPositionResponse(response: GetPositionResponse) {
        positionDatas = response.data
        getStocksInfo()
    }

    /**
     * 获取批量股票详情
     */
    fun getStocksInfo() {
        val request = GetPositionRequest("", "", transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.getStocksInfo(request)
            ?.enqueue(Network.IHCallBack<GetStocksInfoResponse>(request))
        view?.createFundAccountSuccess()

    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetStocksInfoResponse(response: GetStocksInfoResponse) {
        stocksInfo = HashMap()
        val datas: List<GetStockInfoResponse.Data> = response.data
        for (data in datas) {
            stocksInfo?.put(data.tsCode, data)
        }
        var totalMarketValue: Float = 0.0f
        for (data in positionDatas!!) {
            val tsCode = data.stockCode + "." + data.stockType
            val stockInfo: GetStockInfoResponse.Data? = stocksInfo?.get(tsCode)
            //data.marketValue =stockInfo?.realPrice * data?.holdStockCount
        }
        var totalAssets: Float = 0.0f
    }

    /**
     * 获取今日订单
     */
    fun getTodayOrders() {
        val request = OrderListRequest("", "", "", "", transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.orderList(request)
            ?.enqueue(Network.IHCallBack<OrderListResponse>(request))
        view?.createFundAccountSuccess()
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onOrderListResponse(response: OrderListResponse) {

    }

}