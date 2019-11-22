package com.zhuorui.securities.market.ui.presenter

import android.text.TextUtils
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
import com.zhuorui.securities.market.manager.STInfoManager
import com.zhuorui.securities.market.model.*
import com.zhuorui.securities.market.net.ISimulationTradeNet
import com.zhuorui.securities.market.net.request.FeeComputeRequest
import com.zhuorui.securities.market.net.request.GetFeeTemplateRequest
import com.zhuorui.securities.market.net.request.GetStockInfoRequest
import com.zhuorui.securities.market.net.request.StockTradRequest
import com.zhuorui.securities.market.net.response.FeeComputeResponse
import com.zhuorui.securities.market.net.response.GetFeeTemplateResponse
import com.zhuorui.securities.market.net.response.GetStockInfoResponse
import com.zhuorui.securities.market.socket.SocketApi
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicPriceResponse
import com.zhuorui.securities.market.socket.push.StocksTopicTransResponse
import com.zhuorui.securities.market.socket.request.GetStockPriceRequestBody
import com.zhuorui.securities.market.socket.response.GetStockPriceResponse
import com.zhuorui.securities.market.ui.SimulationTradingStocksFragment
import com.zhuorui.securities.market.ui.view.SimulationTradingStocksView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingStocksViewModel
import com.zhuorui.securities.market.util.MathUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.math.BigDecimal
import java.util.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/9 13:37
 *    desc   :
 */
class SimulationTradingStocksPresenter(val fragment: SimulationTradingStocksFragment) :
    AbsNetPresenter<SimulationTradingStocksView, SimulationTradingStocksViewModel>() {

    private var stockTopicPrice: StockTopic? = null
    private var stockTopicTrans: StockTopic? = null
    private val disposables = LinkedList<Disposable>()

    override fun init() {
        super.init()

        view?.updateMaxBuyNum(null)
        view?.updateMaxBuySell(null)

        // 监听购买数量变化
        viewModel?.buyCount?.observe(fragment, androidx.lifecycle.Observer<Int> { buyCount ->
            calculateBuyMoney()

            val maxBuyCount = viewModel?.maxBuyCount?.value?.toInt()
            val maxSaleCount = viewModel?.maxSaleCount?.value?.toInt()
            if (buyCount != null) {
                // 是否超出可买
                val moreBuyCount = maxBuyCount != null && buyCount > maxBuyCount
                // 是否超出可卖
                val moreSaleCount = maxSaleCount != null && buyCount > maxSaleCount
                if (moreBuyCount && moreSaleCount) {
                    ToastUtil.instance.toastCenter(R.string.count_more_than_max)
                } else if (moreBuyCount) {
                    ToastUtil.instance.toastCenter(R.string.buy_count_more_than_max)
                } else if (moreSaleCount && maxSaleCount!! > 0) {
                    ToastUtil.instance.toastCenter(R.string.sell_count_more_than_max)
                }

                // 是否与每手股数不匹配
                viewModel?.stockInfoData?.value?.hands?.let {
                    if (buyCount % it != 0) {
                        ToastUtil.instance.toastCenter(R.string.count_invalid)
                        viewModel?.enableBuy?.value = false
                        viewModel?.enableSell?.value = false
                    }
                }
            }
        })
        // 监听购买价格变化
        viewModel?.buyPrice?.observe(fragment, androidx.lifecycle.Observer<BigDecimal> {
            calculateBuyMoney()
        })

//        setTradType()
    }

    /**
     * 根据交易类型设置界面
     */
    fun setTradType() {
        // 获取传递订单参数
        val arguments = fragment.arguments
        val orderData = arguments?.getParcelable<STOrderData>(STOrderData::class.java.simpleName)
        val tradType = arguments?.getInt(SimulationTradingStocksFragment.TRAD_TYPE_KEY)
        val stockInfo =
            if (orderData == null) arguments?.getParcelable<SearchStockInfo>(SearchStockInfo::class.java.simpleName) else null
        if (orderData != null) {
            // 设置股票信息
            val searchStockInfo = SearchStockInfo()
            searchStockInfo.ts = orderData.ts
            searchStockInfo.code = orderData.code
            searchStockInfo.tsCode = orderData.code + "." + orderData.ts
            searchStockInfo.name = orderData.stockName
            searchStockInfo.type = 2
            setStock(searchStockInfo)

            // 已持仓
            if (tradType == SimulationTradingStocksFragment.TRAD_TYPE_DEFAULT) {
                // 更新最大可卖
                updateMaxBuySell(orderData.saleStockCount!!.toLong())
                // 显示已持仓状态
                view?.changeTrustType(3)
            } else {
                // 买入未成交或买入已撤单
                if (orderData.trustType == 1) {
                    // 买入未成交
                    if (orderData.majorStatus == 1) {
                        viewModel?.updateOrderId?.value = orderData.id
                        viewModel?.updateType?.value = 1
                        // 显示订单委托价格、委托股数
                        viewModel?.buyPrice?.value = MathUtil.rounded3(orderData.holdCost!!)
                        viewModel?.buyCount?.value = orderData.holdStockCount?.toInt()
                        // 显示买入改单状态
                        view?.changeTrustType(1)
                    }
                    // 买入已撤单
                    else if (orderData.majorStatus == 4 || orderData.majorStatus == 5) {
                        // 是否已持仓
                        val saleStockCount = orderData.saleStockCount
                        if (orderData.saleStockCount != null) {
                            // 更新最大可卖
                            updateMaxBuySell(saleStockCount!!.toLong())
                            // 显示已持仓状态
                            view?.changeTrustType(3)
                        } else {
                            // 未持仓，显示未持仓购买
                            view?.changeTrustType(4)
                        }
                    }
                }
                // 卖出未成交或卖出已撤单
                else if (orderData.trustType == 2) {
                    // 卖出未成交
                    if (orderData.majorStatus == 1) {
                        viewModel?.updateOrderId?.value = orderData.id
                        viewModel?.updateType?.value = 2
                        // 显示订单委托价格、委托股数
                        viewModel?.buyPrice?.value = MathUtil.rounded3(orderData.holdCost!!)
                        viewModel?.buyCount?.value = orderData.holdStockCount?.toInt()
                        updateMaxBuySell(orderData.saleStockCount!!.toLong())
                        // 显示卖出改单状态
                        view?.changeTrustType(2)
                    }
                    // 卖出已撤单
                    else if (orderData.majorStatus == 4 || orderData.majorStatus == 5) {
                        // 是否已持仓
                        val saleStockCount = orderData.saleStockCount
                        if (orderData.saleStockCount != null) {
                            // 更新最大可卖
                            updateMaxBuySell(saleStockCount!!.toLong())
                            // 显示已持仓状态
                            view?.changeTrustType(3)
                        } else {
                            // 未持仓，显示未持仓购买
                            view?.changeTrustType(4)
                        }
                    }
                }
            }
        } else if (stockInfo != null) {
            // 显示默认购买状态
            view?.changeTrustType(0)
            setStock(stockInfo)
        } else {
            // 显示默认购买状态
            view?.changeTrustType(0)
        }
    }

    /**
     * 更新已持仓股数
     * @param count 已持仓的股数
     */
    private fun updateMaxBuySell(count: Long) {
        viewModel?.maxSaleCount?.value = count
        viewModel?.enableSell?.value = true
        // 显示最大可卖
        view?.updateMaxBuySell(count)
    }

    /**
     * 计算金额和最大可买数量
     */
    private fun calculateBuyMoney() {
        viewModel?.stockFeeRules?.value?.let { stockFeeRules ->
            viewModel?.buyCount?.value?.let { buyCount ->
                viewModel?.buyPrice?.value?.let { buyPrice ->
                    // 交易金额
                    val buyMoney = BigDecimal.valueOf(buyCount.toLong()).multiply(buyPrice)
                    viewModel?.buyMoney?.value = buyMoney
                    // 判断改单类型
                    val updateType = viewModel?.updateType?.value
                    // 默认买入或修改买入订单
                    if (updateType == null || updateType == 1) {
                        // 印花税
                        val stampTax = viewModel?.getFee(buyMoney, stockFeeRules.getValue(5))
                        // 交易佣金
                        val commission = viewModel?.getFee(buyMoney, stockFeeRules.getValue(1))
                        // 平台使用费
                        val platformFee = viewModel?.getFee(buyMoney, stockFeeRules.getValue(2))
                        // 交易征费
                        val tradingRequisitionFee =
                            viewModel?.getFee(buyMoney, stockFeeRules.getValue(7))
                        // 交易系统使用费
                        val tradingSystemFee = viewModel?.getFee(buyMoney, stockFeeRules.getValue(3))
                        // 交易费
                        val tradingFee = viewModel?.getFee(buyMoney, stockFeeRules.getValue(6))
                        // 中央结算系统交收费
                        val systemPaysFee = viewModel?.getFee(buyMoney, stockFeeRules.getValue(4))
                        // 一手股数
                        val perShareNumber = viewModel?.stockInfoData?.value?.hands
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        // 一、最大可买手 = 可用资金 / 委托价格 / 每手股数
                        val accountMoney = STInfoManager.getInstance().getSTFundAccountData().availableFund.toDouble()
                        var maxBuyCount = (accountMoney / buyPrice.toDouble() / perShareNumber!!).toLong()
                        // 二、判断可用资金是否够交易费用 + 手续费
                        while (true) {
                            if (
                                accountMoney
                                - maxBuyCount * buyPrice.toDouble() * perShareNumber
                                - commission!!.toDouble()
                                - platformFee!!.toDouble()
                                - stampTax!!.toDouble()
                                - tradingRequisitionFee!!.toDouble()
                                - tradingSystemFee!!.toDouble()
                                - tradingFee!!.toDouble()
                                - systemPaysFee!!.toDouble()
                                >= 0
                            ) {
                                break
                            }
                            maxBuyCount -= 1
                        }

                        maxBuyCount *= perShareNumber
                        viewModel?.maxBuyCount?.value = maxBuyCount
                        // 判断是否超过最大可买
                        viewModel?.enableBuy?.value = buyCount in 1..maxBuyCount
                        // 更新界面最大可买数量
                        view?.updateMaxBuyNum(viewModel?.maxBuyCount?.value!!.toLong())
                    }
                    // 判断是否超过最大可卖
                    viewModel?.maxSaleCount?.value?.let { viewModel?.enableSell?.value = buyCount in 1..it }
                }
            }
        }
    }

    /**
     * 手动输入价格
     */
    fun onEditBuyPrice(price: String?) {
        if (TextUtils.isEmpty(price) || price?.endsWith(".")!!) return
        viewModel?.buyPrice?.value = price.toBigDecimal()
    }

    /**
     * 手动输入数量
     */
    fun onEditBuyCount(count: String?) {
        if (TextUtils.isEmpty(count)) {
            viewModel?.buyCount?.value = null
        } else {
            viewModel?.buyCount?.value = count?.toInt()
        }
    }

    /**
     * 加减价格
     * @param type 1：加 2：减
     */
    fun addOrSubBuyPrice(type: Int) {
        val buyPrice = viewModel?.buyPrice?.value
        if (buyPrice == null) {
            ToastUtil.instance.toastCenter(R.string.stock_code_input_hint)
            return
        }
        if (type == 1) {
            viewModel?.buyPrice?.value = buyPrice.add(viewModel?.minChangesPrice?.value)
        } else {
            val value = buyPrice.subtract(viewModel?.minChangesPrice?.value)
            // 判断是否小于0
            if (value.compareTo(BigDecimal.ZERO) == -1) {
                viewModel?.buyPrice?.value = BigDecimal.ZERO
            } else {
                viewModel?.buyPrice?.value = value
            }
        }
        view?.clearBuyPriceFocus()
    }

    /**
     * 加减数量
     * @param type 1：加 2：减
     */
    fun addOrSubBuyCount(type: Int) {
        var count = viewModel?.buyCount?.value
        if (count == null) {
            ToastUtil.instance.toastCenter(R.string.stock_code_input_hint)
            return
        }
        if (type == 1) {
            viewModel?.buyCount?.value = count + viewModel?.stockInfoData?.value?.hands!!
        } else {
            if (count == 0) return
            count -= viewModel?.stockInfoData?.value?.hands!!
            if (count < 0) count = 0
            viewModel?.buyCount?.value = count
        }
        view?.clearBuyCountFocus()
    }

    fun toggleKline() {
        view?.toggleKline()
    }

    /**
     * 设置选择的自选股
     */
    fun setStock(stockInfo: SearchStockInfo) {
        viewModel?.stockInfo?.value = stockInfo
        viewModel?.stockInfoData?.value = null
        viewModel?.buyPrice?.value = null
        viewModel?.minChangesPrice?.value = null
        viewModel?.buyCount?.value = null
        viewModel?.buyMoney?.value = null
        viewModel?.maxBuyCount?.value = null
        viewModel?.enableBuy?.value = false
        viewModel?.enableSell?.value = false
        view?.initKline(
            stockInfo.ts!!,
            stockInfo.code!!,
            stockInfo.tsCode ?: stockInfo.code!! + "." + stockInfo.ts!!,
            stockInfo.type!!
        )
        // 清除上一次的价格信息
        view?.updateStockPrice(null, null, null, 0)
        // 取消上一次的订阅
        if (stockTopicPrice != null) {
            SocketClient.getInstance().unBindTopic(stockTopicPrice)
        }
        // 查询价格
        getStockPrice()
        if (stockTopicTrans != null) {
            SocketClient.getInstance().unBindTopic(stockTopicTrans)
        }
        // 重新订阅当前的自选股
        stockTopicTrans =
            StockTopic(StockTopicDataTypeEnum.STOCK_ORDER, stockInfo.ts!!, stockInfo.code!!, stockInfo.type!!)
        SocketClient.getInstance().bindTopic(stockTopicTrans)
        // 获取股票计算交易费用规则模版，股票市场（1-港股 2-美股 3-A股）
        val getFeeTemplateRequest = GetFeeTemplateRequest(
            "1",
            STInfoManager.getInstance().getSTFundAccountData().accountId,
            1,
            transactions.createTransaction()
        )
        Cache[ISimulationTradeNet::class.java]?.feeTemplate(getFeeTemplateRequest)
            ?.enqueue(Network.IHCallBack<GetFeeTemplateResponse>(getFeeTemplateRequest))
    }

    private fun getStockPrice() {
        val stockInfo = viewModel?.stockInfo?.value ?: return
        // 查询价格
        SocketClient.getInstance().postRequest(
            GetStockPriceRequestBody(
                GetStockPriceRequestBody.StockVo(
                    stockInfo.ts!!,
                    stockInfo.code!!,
                    stockInfo.type!!
                )
            ), SocketApi.GET_STOCK_PRICE
        )
    }

    /**
     * 查询价格
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onGetStockPriceResponse(response: GetStockPriceResponse) {
        val stockInfo = viewModel?.stockInfo?.value ?: return
        val stockPriceDatas = response.data ?: return
        updatePrice(stockInfo, stockPriceDatas[0])
        // 订阅价格
        stockTopicPrice = StockTopic(StockTopicDataTypeEnum.STOCK_PRICE, stockInfo.ts!!, stockInfo.code!!, 2)
        SocketClient.getInstance().bindTopic(stockTopicPrice)
    }

    /**
     * 订阅返回股票价格波动
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onStocksTopicPriceResponse(response: StocksTopicPriceResponse) {
        val stockInfo = viewModel?.stockInfo?.value ?: return
        val stockPriceDatas = response.body
        updatePrice(stockInfo, stockPriceDatas)
    }

    private fun updatePrice(stockInfo: SearchStockInfo, pushPriceDatas: PushStockPriceData?) {
        if (pushPriceDatas == null) {
            return
        }
        if (stockInfo.ts == pushPriceDatas.ts && stockInfo.code == pushPriceDatas.code) {
            // 在主线程更新数据
            val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                emitter.onNext(true)
                emitter.onComplete()
            }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewModel?.price?.value = MathUtil.rounded3(pushPriceDatas.last!!)
                    // 计算跌涨价格
                    val diffPrice = MathUtil.rounded3(pushPriceDatas.last!!.subtract(pushPriceDatas.open!!))
                    viewModel?.diffPrice?.value = diffPrice
                    // 计算跌涨幅百分比
                    viewModel?.diffRate?.value =
                        MathUtil.divide2(diffPrice.multiply(BigDecimal.valueOf(100)), pushPriceDatas.open!!)
                    // 更新界面
                    view?.updateStockPrice(
                        pushPriceDatas.last,
                        diffPrice,
                        viewModel?.diffRate?.value,
                        pushPriceDatas.pctTag
                    )
                }
            disposables.add(disposable)
        }
    }

    /**
     * 订阅返回盘口波动
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicTransResponse(response: StocksTopicTransResponse) {
        val transData = response.body
        if (transData.ts.equals(stockTopicTrans?.ts) && transData.code.equals(stockTopicTrans?.code)) {
            // 买入挂单比例 = 买一挂单量 / 买一挂单量 + 卖一挂单量
            viewModel?.buyRate?.value =
                MathUtil.divide2(
                    transData.bid1Volume!!,
                    transData.bid1Volume!!.add(transData.ask1Volume)
                ).multiply(BigDecimal.valueOf(100))
            // 卖出挂单比例 = 100 - 买入挂单比例
            viewModel?.sellRate?.value = BigDecimal.valueOf(100).subtract(viewModel?.buyRate?.value!!)
            // 更新界面
            view?.updateStockTrans(
                transData,
                viewModel?.buyRate?.value!!.toDouble(),
                viewModel?.sellRate?.value!!.toDouble()
            )
        }
    }

    /**
     * 返回当支股票信息
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetStockInfoResponse(response: GetStockInfoResponse) {
        val stockInfoData = response.data
        viewModel?.stockInfoData?.value = stockInfoData
        viewModel?.minChangesPrice?.value = StockPrice.getMinChangesPrice(stockInfoData.last)
        // 当改单时不取最新的实时股价和每手股数
        if (viewModel?.updateOrderId?.value == null) {
            viewModel?.buyPrice?.value = MathUtil.rounded3(stockInfoData.last)
            viewModel?.buyCount?.value = stockInfoData.hands
        } else {
            calculateBuyMoney()
        }
    }

    /**
     * 返回股票计算交易费用规则
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetFeeTemplateResponse(response: GetFeeTemplateResponse) {
        viewModel?.stockFeeRules?.value = response.data

        // 获取当支股票信息
        val stockInfo = viewModel?.stockInfo?.value
        val getStockInfoRequest =
            GetStockInfoRequest(stockInfo?.ts!!, stockInfo.code!!, transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.getStockInfo(getStockInfoRequest)
            ?.enqueue(Network.IHCallBack<GetStockInfoResponse>(getStockInfoRequest))
    }

    /**
     * 买入股票
     * @param chargeType 1买入 2卖出
     */
    fun transactionStocks(chargeType: Int) {
        // 计算交易费用
        val request =
            FeeComputeRequest(
                1,
                STInfoManager.getInstance().getSTFundAccountData().accountId,
                chargeType,
                viewModel?.buyMoney?.value!!,
                transactions.createTransaction()
            )
        Cache[ISimulationTradeNet::class.java]?.feeCompute(request)
            ?.enqueue(Network.IHCallBack<FeeComputeResponse>(request))
    }

    /**
     * 返回计算交易费用
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onFeeComputeResponse(response: FeeComputeResponse) {
        viewModel?.totalFee?.value = response.data.totalFee

        // 获取当支股票信息
        val stockInfo = viewModel?.stockInfo?.value
        // 展示交易明细
        val chargeType = (response.request as FeeComputeRequest).chargeType
        view?.showTradingStocksOrderDetail(
            "1",
            chargeType,
            stockInfo?.name!!,
            stockInfo.tsCode!!,
            viewModel!!.buyPrice.value.toString() + "（港元）",
            MathUtil.convertToString(viewModel!!.buyCount.value!!.toBigDecimal()),
            response.data.totalFee.toDouble(),
            if (chargeType == 1) MathUtil.convertToString(viewModel?.buyMoney?.value?.add(response.data.totalFee)!!) else MathUtil.convertToString(
                viewModel?.buyMoney?.value?.subtract(response.data.totalFee)!!
            ) + "（港元）"
        )
    }

    /**
     * 确定买入、卖出股票
     * @param chargeType 1买入 2卖出
     */
    fun confirmTradingStocks(chargeType: Int) {
        // 获取当支股票信息
        val stockInfo = viewModel?.stockInfo?.value
        val updateOrderId = viewModel?.updateOrderId?.value
        val request = StockTradRequest(
            STInfoManager.getInstance().getSTFundAccountData().accountId,
            updateOrderId,
            stockInfo?.ts!!,
            stockInfo.code!!,
            viewModel?.buyPrice?.value!!,
            viewModel?.buyCount?.value?.toLong()!!,
            viewModel?.totalFee?.value!!,
            if (chargeType == 1) viewModel?.buyMoney?.value?.add(viewModel?.totalFee?.value!!)!!
            else viewModel?.buyMoney?.value?.subtract(viewModel?.totalFee?.value!!)!!,
            chargeType,
            transactions.createTransaction()
        )

        if (chargeType == 1) {
            if (updateOrderId == null) {
                // 买入股票
                Cache[ISimulationTradeNet::class.java]?.stockBuy(request)
                    ?.enqueue(Network.IHCallBack<BaseResponse>(request))
            } else {
                // 修改买入订单
                Cache[ISimulationTradeNet::class.java]?.updateBuyTrust(request)
                    ?.enqueue(Network.IHCallBack<BaseResponse>(request))
            }
        } else {
            if (updateOrderId == null) {
                // 卖出股票
                Cache[ISimulationTradeNet::class.java]?.stockSell(request)
                    ?.enqueue(Network.IHCallBack<BaseResponse>(request))
            } else {
                // 修改卖出订单
                Cache[ISimulationTradeNet::class.java]?.updateSellTrust(request)
                    ?.enqueue(Network.IHCallBack<BaseResponse>(request))
            }
        }
    }

    /**
     * 取消改单
     */
    fun cancelChangeOrders() {
        view?.exit()
    }

    override fun onBaseResponse(response: BaseResponse) {
        if (response.request is StockTradRequest) {
            // 买入/卖出/修改买入/修改卖出股票成功
            view?.tradStocksSuccessful()
        }
    }

    /**
     * 长链接连接状态发生改变
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onSocketAuthCompleteEvent(event: SocketAuthCompleteEvent) {
        // 恢复订阅
        if (stockTopicTrans != null) {
            SocketClient.getInstance().bindTopic(stockTopicTrans)
        }
        // 查询价格
        getStockPrice()
    }

    override fun destroy() {
        super.destroy()
        // 取消订阅
        if (stockTopicPrice != null) {
            SocketClient.getInstance().unBindTopic(stockTopicPrice)
        }
        if (stockTopicTrans != null) {
            SocketClient.getInstance().unBindTopic(stockTopicTrans)
        }

        // 释放disposable
        if (disposables.isNullOrEmpty()) return
        for (disposable in disposables) {
            disposable.dispose()
        }
        disposables.clear()
    }
}