package com.zhuorui.securities.market.ui.presenter

import android.text.TextUtils
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.model.*
import com.zhuorui.securities.market.net.ISimulationTradeNet
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
    private var availableFunds: BigDecimal = BigDecimal(0)
    private var count: Int = 0


    /**
     * 查询资金账户接口
     */
    fun getFundAccount() {
        val request = FundAccountRequest(1, transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.getFundAccount(request)
            ?.subscribeOn(Schedulers.io())?.observeOn(
                AndroidSchedulers.mainThread()
            )
            ?.subscribe(io.reactivex.functions.Consumer {
                when {
                    it.isSuccess() -> {
                        val accountId = it.data.id!!
                        availableFunds = it.data.availableAmount!!
                        LocalAccountConfig.read().setAccountId(accountId)
                        count = 0
                        getPosition()
                        getTodayOrders()
                    }
                    TextUtils.equals("060003", it.code) -> {
                        view?.onUpData(null, null, STFundAccountData(null, 0f))
                    }
                    else -> {
                        view?.onGetFundAccountError(it.code, it.msg)
                    }
                }
            }, io.reactivex.functions.Consumer {
                view?.onGetFundAccountError("-1", it.message)
            })
    }

    /**
     * 创建资金账号
     */
    fun createFundAccount() {
        val request = FundAccountRequest(1, transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.createFundAccount(request)
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
                    val accountId = it.data.id!!
                    availableFunds = it.data.availableAmount!!
                    LocalAccountConfig.read().setAccountId(accountId)
                    view?.createFundAccountSuccess()
                    view?.onUpData(null, null, STFundAccountData(accountId, availableFunds.toFloat()))
                } else {
                    view?.onGetFundAccountError(it.code, it.msg)
                }
            }, io.reactivex.functions.Consumer {
                view?.onCreateFundAccountError("-1", it.message)
            })
    }

    /**
     * 获取持仓
     */
    private fun getPosition() {
        val accountInfo = LocalAccountConfig.read().getAccountInfo()
        val request = GetPositionRequest(accountInfo.token!!, accountInfo.accountId!!, transactions.createTransaction())
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
                accountInfo.accountId!!,
                todayTime,
                todayTime,
                accountInfo.token!!,
                transactions.createTransaction()
            )
        Cache[ISimulationTradeNet::class.java]?.orderList(request)
            ?.enqueue(Network.IHCallBack<OrderListResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onOrderListResponse(response: OrderListResponse) {
        count++
        orderDatas = response.data.list
        topicPrice()
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetPositionResponse(response: GetPositionResponse) {
        count++
        positionDatas = response.data
        if (positionDatas == null) positionDatas = mutableListOf()
        topicPrice()
    }

    override fun onErrorResponse(response: ErrorResponse) {
//        super.onErrorResponse(response)
        if (response.request is GetPositionRequest) {
            count++
            positionDatas = mutableListOf()
        } else if (response.request is OrderListRequest) {
            count++
            orderDatas = mutableListOf()
        }
        topicPrice()
    }

    /**
     *  发起价格订阅
     */
    private fun topicPrice() {
        if (count >= 2 && (positionDatas == null || orderDatas == null)) {
            view?.onGetFundAccountError("-1", ResUtil.getString(R.string.getdata_err))
            return
        } else if (count < 2) {
            return
        }
        stocksInfo.clear()
        val list: MutableList<StockTopic> = mutableListOf()
        //过虑持仓重复订阅股票
        for (data in positionDatas!!) {
            val tsCode = data.code!! + "." + data.ts!!
//            if (!stocksInfo.containsKey(tsCode)) {
            stocksInfo[tsCode] = PushStockPriceData()
            list.add(StockTopic(StockTopicDataTypeEnum.price, data.ts!!, data.code!!, 2))
//            }
        }
        //筛选订单需要订阅的股票
        for (data in orderDatas!!) {
            if (data.status == 2 && !stocksInfo.containsKey(data.code!! + "." + data.ts!!)) {
                list.add(StockTopic(StockTopicDataTypeEnum.price, data.ts!!, data.code!!, 2))
            }
        }
        if (list.isNullOrEmpty()) {
            calculation()
        } else {
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
                val tsCode = data.code + "." + data.ts
                val stockInfo = stocksInfo?.get(tsCode)
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
        if (orderDatas.isNullOrEmpty()) {
            for (data in orderDatas!!) {
                if (data?.status!! == 2) {
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
                            val tsCode = data.code + "." + data.ts
                            val stockInfo = stocksInfo?.get(tsCode)!!
                            val profitAndLoss = MathUtil.multiply3(
                                MathUtil.subtract3(stockInfo.price!!, data.holeCost!!),
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
            STFundAccountData(LocalAccountConfig.read().getAccountInfo().accountId, availableFunds.toFloat())
        fundAccount.marketValue = totalMarketValue.toFloat()
        fundAccount.totalAssets = totalAssets.toFloat()
        fundAccount.totalProfitAndLoss = totalProfitAndLoss.toFloat()
        fundAccount.todayProfitAndLoss = todayProfitAndLoss.toFloat()
        fundAccount.todayProfitAndLossPercentage = todayProfitAndLossPercentage.toFloat()
        view?.onUpData(positionDatas, orderDatas, fundAccount)
    }

}