package com.zhuorui.securities.market.ui.presenter

import android.text.TextUtils
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.StockPrice
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.net.ISimulationTradeNet
import com.zhuorui.securities.market.net.request.GetFeeTemplateRequest
import com.zhuorui.securities.market.net.request.GetStockInfoRequest
import com.zhuorui.securities.market.net.response.GetFeeTemplateResponse
import com.zhuorui.securities.market.net.response.GetStockInfoResponse
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicPriceResponse
import com.zhuorui.securities.market.socket.push.StocksTopicTransResponse
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
        // 监听购买数量变化
        viewModel?.buyCount?.observe(fragment, androidx.lifecycle.Observer<Long> {
            val maxBuyCount = viewModel?.maxBuyCount?.value?.toLong()
            if (it != null && maxBuyCount != null && it > maxBuyCount) {
                ToastUtil.instance.toastCenter(R.string.buy_count_more_than_max)
            }
            calculateBuyMoney()
        })
        // 监听购买价格变化
        viewModel?.buyPrice?.observe(fragment, androidx.lifecycle.Observer<BigDecimal> {
            calculateBuyMoney()
        })
        view?.updateMaxBuyNum(null)
    }

    /**
     * 计算金额和最大可买数量
     */
    private fun calculateBuyMoney() {
        viewModel?.buyCount?.value?.let { it1 ->
            viewModel?.buyPrice?.value?.let { it2 ->
                // 交易金额
                val buyMoney = BigDecimal.valueOf(it1).multiply(it2)
                viewModel?.buyMoney?.value = buyMoney
                // 印花税
                var stampTax = MathUtil.rounded(buyMoney.multiply(BigDecimal.valueOf(0.001))!!)
                if (stampTax.compareTo(BigDecimal.ONE) == -1) {
                    stampTax = BigDecimal.ONE
                }
                // 交易佣金
                var commission = buyMoney?.multiply(BigDecimal.valueOf(0.0003))
                // 最低佣金为3元
                if (commission!!.toLong() < 3) {
                    commission = BigDecimal.valueOf(3)
                }
                // 平台使用费
                val platformFee = BigDecimal.valueOf(15)
                // 交易征费
                var tradingRequisitionFee = buyMoney.multiply(BigDecimal.valueOf(0.000027))!!
                // 最低交易征费为0.01元
                val minTradingRequisitionFee = BigDecimal.valueOf(0.01)
                // -1表示小于 0表示等于 1表示大于
                if (tradingRequisitionFee.compareTo(minTradingRequisitionFee) == -1) {
                    tradingRequisitionFee = minTradingRequisitionFee
                }
                // 交易系统使用费
                val tradingSystemFee = BigDecimal.valueOf(0.5)
                // 交易费
                var tradingFee = buyMoney.multiply(BigDecimal.valueOf(0.00005))!!
                // 最低交易费为0.01元
                val minTradingFee = BigDecimal.valueOf(0.01)
                // -1表示小于 0表示等于 1表示大于
                if (tradingFee.compareTo(minTradingFee) == -1) {
                    tradingFee = minTradingFee
                }
                // 中央结算系统交收费
                var systemPaysFee = buyMoney.multiply(BigDecimal.valueOf(0.00002))!!
                // 中央结算系统交收费最低为2元，最高为100元
                if (systemPaysFee.toLong() < 2) {
                    systemPaysFee = BigDecimal.valueOf(2)
                } else if (systemPaysFee.toLong() >= 100) {
                    systemPaysFee = BigDecimal.valueOf(100)
                }
                // 一手价格
                val buyPrice = viewModel?.buyPrice?.value
                // 一手股数
                val perShareNumber = viewModel?.stockInfoData?.value?.perShareNumber
                ///////////////////////////////////////////////////////////////////////////////////////////
                // 一、最大可买手 = 可用资金 / 委托价格 / 每手股数
                val accountMoney = 1000000.00
                var maxBuyCount = (accountMoney / buyPrice!!.toDouble() / perShareNumber!!).toLong()
                // 二、判断可用资金是否够交易费用 + 手续费
                while (true) {
                    if (
                        accountMoney
                        - maxBuyCount * buyPrice.toDouble() * perShareNumber
                        - commission!!.toDouble()
                        - platformFee.toInt()
                        - stampTax.toDouble()
                        - tradingRequisitionFee.toDouble()
                        - tradingSystemFee.toDouble()
                        - tradingFee.toDouble()
                        - systemPaysFee.toDouble()
                        >= 0
                    ) {
                        break
                    }
                    maxBuyCount -= 1
                }

                maxBuyCount *= perShareNumber
                viewModel?.maxBuyCount?.value = maxBuyCount
                viewModel?.enableBuy?.value = it1 in 1..maxBuyCount
                // 更新界面最大可买数量
                view?.updateMaxBuyNum(viewModel?.maxBuyCount?.value!!.toLong())
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
            viewModel?.buyCount?.value = count?.toLong()
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
        val count = viewModel?.buyCount?.value
        if (count == null) {
            ToastUtil.instance.toastCenter(R.string.stock_code_input_hint)
            return
        }
        if (type == 1) {
            viewModel?.buyCount?.value = count + viewModel?.stockInfoData?.value?.perShareNumber!!
        } else {
            if (count == 0L) return
            viewModel?.buyCount?.value = count - viewModel?.stockInfoData?.value?.perShareNumber!!
        }
        view?.clearBuyCountFocus()
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
        // 清除上一次的价格信息
        view?.updateStockPrice(BigDecimal.valueOf(0.00), BigDecimal.valueOf(0.00), BigDecimal.valueOf(0.00))
        // 取消上一次的订阅
        if (stockTopicPrice != null && stockTopicTrans != null) {
            SocketClient.getInstance().unBindTopic(stockTopicPrice, stockTopicTrans)
        }
        // 订阅当前的自选股
        stockTopicPrice = StockTopic(StockTopicDataTypeEnum.price, stockInfo.ts!!, stockInfo.code!!, stockInfo.type!!)
        stockTopicTrans = StockTopic(StockTopicDataTypeEnum.trans, stockInfo.ts!!, stockInfo.code!!, stockInfo.type!!)
        SocketClient.getInstance().bindTopic(stockTopicPrice, stockTopicTrans)
        // 获取当支股票信息
        val getStockInfoRequest =
            GetStockInfoRequest(stockInfo.ts!!, stockInfo.code!!, transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.getStockInfo(getStockInfoRequest)
            ?.enqueue(Network.IHCallBack<GetStockInfoResponse>(getStockInfoRequest))
        // 获取股票计算交易费用规则模版，股票市场（1-港股 2-美股 3-A股） TODO accountId暂时写1
        val getFeeTemplateRequest = GetFeeTemplateRequest("1", "1", transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.feeTemplate(getFeeTemplateRequest)
            ?.enqueue(Network.IHCallBack<GetFeeTemplateResponse>(getFeeTemplateRequest))
    }

    /**
     * 订阅返回股票价格波动
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onStocksTopicPriceResponse(response: StocksTopicPriceResponse) {
        val stockInfo = viewModel?.stockInfo?.value ?: return
        val stockPriceDatas = response.body
        for (sub in stockPriceDatas) {
            if (stockInfo.ts == sub.ts && stockInfo.code == sub.code) {
                // 在主线程更新数据
                val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                    emitter.onNext(true)
                    emitter.onComplete()
                }).subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        viewModel?.price?.value = MathUtil.rounded3(sub.price!!)
                        // 计算跌涨价格
                        val diffPrice = MathUtil.rounded3(sub.price!!.subtract(sub.openPrice!!))
                        viewModel?.diffPrice?.value = diffPrice
                        // 计算跌涨幅百分比
                        viewModel?.diffRate?.value =
                            MathUtil.divide2(diffPrice.multiply(BigDecimal.valueOf(100)), sub.openPrice!!)
                        // 更新界面
                        view?.updateStockPrice(sub.price!!, diffPrice, viewModel?.diffRate?.value!!)
                    }
                disposables.add(disposable)
                break
            }
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

        viewModel?.buyPrice?.value = MathUtil.rounded3(stockInfoData.realPrice)
        viewModel?.minChangesPrice?.value = StockPrice.getMinChangesPrice(stockInfoData.realPrice)
        viewModel?.buyCount?.value = stockInfoData.perShareNumber.toLong()
    }

    override fun destroy() {
        super.destroy()
        // 取消订阅
        if (stockTopicPrice != null && stockTopicTrans != null) {
            SocketClient.getInstance().unBindTopic(stockTopicPrice, stockTopicTrans)
        }

        // 释放disposable
        if (disposables.isNullOrEmpty()) return
        for (disposable in disposables) {
            disposable.dispose()
        }
        disposables.clear()
    }
}