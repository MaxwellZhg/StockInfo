package com.zhuorui.securities.market.ui.presenter

import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.event.NotifyStockCountEvent
import com.zhuorui.securities.market.manager.STInfoManager
import com.zhuorui.securities.market.model.*
import com.zhuorui.securities.market.net.ISimulationTradeNet
import com.zhuorui.securities.market.net.request.CancelTrustRequest
import com.zhuorui.securities.market.net.request.FundAccountRequest
import com.zhuorui.securities.market.net.request.GetPositionRequest
import com.zhuorui.securities.market.net.request.OrderListRequest
import com.zhuorui.securities.market.net.response.*
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicPriceResponse
import com.zhuorui.securities.market.ui.view.SimulationTradingMainView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingMainViewModel
import com.zhuorui.securities.market.util.MathUtil
import com.zhuorui.securities.personal.config.LocalAccountConfig
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.RuntimeException
import java.math.BigDecimal
import kotlin.collections.HashMap

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 16:03
 *    desc   :
 */
class SimulationTradingMainPresenter : AbsNetPresenter<SimulationTradingMainView, SimulationTradingMainViewModel>() {

    /**
     * 服务端获取的持仓原始数据
     */
    private var positionDatas: List<STPositionData>? = null
    /**
     * 服务端获取的订单原始数据
     */
    private var orderDatas: List<STOrderData>? = null
    /**
     * 服务端推送股票价格实时数据
     */
    private val stocksInfo: HashMap<String, PushStockPriceData> = HashMap()
    private var stockTopics: MutableList<StockTopic>? = null
    private var availableFunds: BigDecimal = BigDecimal(0)
    private var count: Int = 0
    private var accountId: String = ""
    private var disposable: Disposable? = null

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.fundAccount?.observe(it,
                androidx.lifecycle.Observer<STFundAccountData> { t ->
                    view?.onUpData(viewModel?.positionDatas?.value, viewModel?.orderDatas?.value, t)
                })
        }
    }

    /**
     * 查询资金账户接口
     */
    fun getFundAccount() {
        if (disposable != null && !disposable!!.isDisposed) disposable?.dispose()
        view?.showLoading()
        val request = FundAccountRequest(1, transactions.createTransaction())
        disposable = Cache[ISimulationTradeNet::class.java]?.getFundAccount(request)
            ?.subscribeOn(Schedulers.io())?.observeOn(
                AndroidSchedulers.mainThread()
            )
            ?.subscribe(io.reactivex.functions.Consumer {
                when {
                    it.isSuccess() -> {
                        accountId = it.data.id!!
                        availableFunds = it.data.availableAmount!!
                        count = 0
                        getPosition()
                        getTodayOrders()
                    }
                    TextUtils.equals("060003", it.code) -> {
                        view?.hideLoading()
                        viewModel?.fundAccount?.value = STFundAccountData("", BigDecimal(0))
                    }
                    else -> {
                        view?.hideLoading()
                        view?.onGetFundAccountError(it.code, it.msg)
                    }
                }
            }, io.reactivex.functions.Consumer {
                view?.hideLoading()
                view?.onGetFundAccountError("-1", it.message)
            })
    }

    /**
     * 创建资金账号
     */
    fun createFundAccount() {
        if (disposable != null && !disposable!!.isDisposed) disposable?.dispose()
        view?.showLoading()
        val request = FundAccountRequest(1, transactions.createTransaction())
        disposable = Cache[ISimulationTradeNet::class.java]?.createFundAccount(request)
            ?.flatMap { t ->
                if (t.isSuccess()) {
                    val request = FundAccountRequest(1, transactions.createTransaction())
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
                    accountId = it.data.id!!
                    availableFunds = it.data.availableAmount!!
                    calculation()
                    view?.createFundAccountSuccess()
                } else {
                    view?.hideLoading()
                    view?.onGetFundAccountError(it.code, it.msg)
                }
            }, io.reactivex.functions.Consumer {
                view?.hideLoading()
                view?.onCreateFundAccountError("-1", it.message)
            })
    }

    /**
     * 获取持仓
     */
    private fun getPosition() {
        val accountInfo = LocalAccountConfig.read().getAccountInfo()
        val request = GetPositionRequest(accountInfo.token!!, accountId, transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.getPosition(request)
            ?.enqueue(Network.IHCallBack<GetPositionResponse>(request))
    }

    /**
     * 获取今日订单
     */
    private fun getTodayOrders() {
        val accountInfo = LocalAccountConfig.read().getAccountInfo()
        val todayTime = TimeZoneUtil.currentTime("yyyy-MM-dd")
        val request =
            OrderListRequest(
                accountId,
                todayTime,
                todayTime,
                accountInfo.token!!,
                transactions.createTransaction()
            )
        Cache[ISimulationTradeNet::class.java]?.orderList(request)
            ?.enqueue(Network.IHCallBack<OrderListResponse>(request))
    }

    /**
     * 撤销买入订单
     */
    fun cancelBuyTrust(trustId: String) {
        view?.showLoading()
        val request = CancelTrustRequest(trustId, transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.cancelBuyTrust(request)
            ?.enqueue(Network.IHCallBack<BaseResponse>(request))
    }

    /**
     * 撤销卖出订单
     */
    fun cancelSellTrust(trustId: String) {
        view?.showLoading()
        val request = CancelTrustRequest(trustId, transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.cancelSellTrust(request)
            ?.enqueue(Network.IHCallBack<BaseResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetPositionResponse(response: GetPositionResponse) {
        count++
        positionDatas = response.data
        if (positionDatas == null) positionDatas = mutableListOf()
        topicPrice()
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onOrderListResponse(response: OrderListResponse) {
        if (!transactions.isMyTransaction(response)) return
        count++
        orderDatas = response.data.list
        topicPrice()
    }

    override fun onBaseResponse(response: BaseResponse) {
        super.onBaseResponse(response)
        when (response.request) {
            is CancelTrustRequest -> {
                getFundAccount()
                view?.cancelTrustSuccess()
            }
        }
    }

    override fun onErrorResponse(response: ErrorResponse) {
        when (response.request) {
            is GetPositionRequest -> {
                count++
                topicPrice()
            }
            is OrderListRequest -> {
                count++
                topicPrice()
            }
            is CancelTrustRequest -> {
                view?.hideLoading()
                view?.cancelTrustError(response.msg)
            }

        }
    }

    /**
     *  发起价格订阅
     */
    private fun topicPrice() {
        if (count >= 2 && (positionDatas == null || orderDatas == null)) {
            view?.hideLoading()
            view?.onGetFundAccountError("-1", ResUtil.getString(R.string.getdata_err))
            return
        } else if (count < 2) {
            return
        }
        stocksInfo.clear()
        val list: MutableList<StockTopic> = mutableListOf()
        for (data in positionDatas!!) {
            stocksInfo[data.getTsCode()] = PushStockPriceData()
            list.add(StockTopic(StockTopicDataTypeEnum.price, data.ts!!, data.code!!, 2))
        }
        //过虑订单需要订阅的股票
        for (data in orderDatas!!) {
            val tsCode = data.getTsCode()
            if (!stocksInfo.containsKey(tsCode)) {
                stocksInfo[tsCode] = PushStockPriceData()
                list.add(StockTopic(StockTopicDataTypeEnum.price, data.ts!!, data.code!!, 2))
            }
        }
        if (list.isNullOrEmpty()) {
            stockTopics = null
            calculation()
        } else {
            stockTopics = list
            SocketClient.getInstance().bindTopic(*list.toTypedArray())
        }
    }

    /**
     * 股票价格变化
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicPriceResponse(response: StocksTopicPriceResponse) {
        if (stocksInfo.isNullOrEmpty()) return
        val prices: List<PushStockPriceData> = response.body
        var change = false
        for (price in prices) {
            val tsCode = price.code + "." + price.ts
            if (stocksInfo.containsKey(tsCode)) {
                stocksInfo[tsCode] = price
                change = true
            }
        }
        if (change) {
            calculation()
        }
    }

    /**
     * 计算
     */
    private fun calculation() {
        var totalMarketValue = BigDecimal(0)//总市值
        var totalProfitAndLoss = BigDecimal(0)//总盈亏 ∑个股持仓盈亏金额+卖出股票的持仓盈亏金额
        var todayProfitAndLoss = BigDecimal(0)//今日盈亏 (今日市值 - 昨日收盘市值）+（今日卖出成交额 - 今日买入成交额）
        if (!positionDatas.isNullOrEmpty()) {
            for (data in positionDatas!!) {
                val stockInfo = stocksInfo?.get(data.getTsCode())
                data.presentPrice = stockInfo?.price!!
                //持仓市值=现价*持仓数
                data.marketValue = MathUtil.multiply3(data.presentPrice!!, data?.holdStockCount!!)
                //持仓盈亏金额=（现价-成本价) * 持有数量
                data.profitAndLoss = MathUtil.multiply3(
                    MathUtil.subtract3(data.presentPrice!!, data.holeCost!!), data.holdStockCount!!
                )
                //持仓盈亏比例=盈亏金额/（成本价 * 持有数量）
                data.profitAndLossPercentage = MathUtil.divide3(
                    data.profitAndLoss!!,
                    MathUtil.multiply3(data.holeCost!!, data.holdStockCount!!)
                )
                totalMarketValue = MathUtil.add3(data.marketValue!!, totalMarketValue)
                //总盈亏 -- 累计持仓盈亏金额
                totalProfitAndLoss = MathUtil.add3(data.profitAndLoss!!, totalProfitAndLoss)
                //今日盈亏 -- 累计持仓今日市值变化 (今日市值 - 昨日收盘市值）
                todayProfitAndLoss = MathUtil.add3(
                    MathUtil.subtract3(
                        data.marketValue!!,
                        MathUtil.multiply3(stockInfo.preClosePrice!!, data?.holdStockCount!!)
                    ), todayProfitAndLoss
                )
            }
        }
        //账户总资产=持仓市值+可用资金
        val totalAssets: BigDecimal = MathUtil.add3(totalMarketValue, availableFunds)
        if (!orderDatas.isNullOrEmpty()) {
            for (data in orderDatas!!) {
                if (data?.majorStatus == 2) {
                    val amt = MathUtil.multiply3(data.holdStockCount!!, data.holeCost!!)
                    when (data.trustType) {
                        1 -> {
                            //今日盈亏 -- 减今日买入成交额
                            todayProfitAndLoss = MathUtil.subtract3(todayProfitAndLoss, amt)
                        }
                        2 -> {
                            //今日盈亏 -- 加今日卖出成交额
                            todayProfitAndLoss = MathUtil.add3(todayProfitAndLoss, amt)
                            //总盈亏 -- 加卖出股票的持仓盈亏金额
                            val stockInfo = stocksInfo?.get(data.getTsCode())
                            val profitAndLoss = MathUtil.multiply3(
                                MathUtil.subtract3(stockInfo?.price!!, data.holeCost!!),
                                data.holdStockCount!!
                            )
                            totalProfitAndLoss = MathUtil.add3(totalProfitAndLoss, profitAndLoss)
                        }
                    }
                }
            }
        }
        //当日盈亏百分比=盈亏金额/(当日盈亏金额绝对值+账户总资产）
        val todayProfitAndLossPercentage =
            MathUtil.divide3(todayProfitAndLoss, MathUtil.add3(todayProfitAndLoss.abs(), totalAssets))
        val fundAccount =
            STFundAccountData(accountId, availableFunds)
        fundAccount.marketValue = totalMarketValue.toFloat()
        fundAccount.totalAssets = totalAssets.toFloat()
        fundAccount.totalProfitAndLoss = totalProfitAndLoss.toFloat()
        fundAccount.todayProfitAndLoss = todayProfitAndLoss.toFloat()
        fundAccount.todayProfitAndLossPercentage = todayProfitAndLossPercentage.toFloat()
        STInfoManager.getInstance().setSTFundAccountData(fundAccount)
        view?.hideLoading()
        viewModel?.positionDatas?.value = positionDatas
        viewModel?.orderDatas?.value = orderDatas
        viewModel?.fundAccount?.value = fundAccount
    }

    override fun destroy() {
        super.destroy()
        // 取消订阅
        if (stockTopics != null) {
            SocketClient.getInstance().unBindTopic(*stockTopics!!.toTypedArray())
        }
        STInfoManager.getInstance().destroy()
        if (disposable != null && !disposable!!.isDisposed) disposable?.dispose()
    }


}