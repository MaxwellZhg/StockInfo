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
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.net.ISimulationTradeNet
import com.zhuorui.securities.market.net.request.GetStockInfoRequest
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
import java.math.RoundingMode
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
        viewModel?.buyCount?.observe(fragment, androidx.lifecycle.Observer<Long> {
            calculateBuyMoney()
        })
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
                val stampTax = MathUtil.rounded(buyMoney.multiply(BigDecimal.valueOf(0.01))!!)
                // 佣金
                var commission = MathUtil.rounded(buyMoney?.multiply(BigDecimal.valueOf(0.03))!!)
                // 最低佣金为3元
                if (commission.toLong() < 3) {
                    commission = BigDecimal.valueOf(3)
                }
                // 平台使用费
                val platformFee = BigDecimal.valueOf(15)
                /* 代收费 */
                // 交易征费
                var tradingRequisitionFee = buyMoney.multiply(BigDecimal.valueOf(0.000027))!!
                // 最低交易征费为0.01元
                val minTradingRequisitionFee = BigDecimal.valueOf(0.01)
                // -1表示小于 0表示等于 1表示大于
                if (tradingRequisitionFee.compareTo(minTradingRequisitionFee) == -1) {
                    tradingRequisitionFee = minTradingRequisitionFee
                }
                // 交易系统使用费
                val tradingSystemFee = BigDecimal.valueOf(0.05)
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
                // 代收费 = 交易征费 + 交易系统使用费 + 交易费 + 中央结算系统交收费
                val agentFee = tradingRequisitionFee.add(tradingSystemFee).add(tradingFee).add(systemPaysFee)
                /* 代收费 */
                /* 最大可买 */
                // 一手价格
                val buyPrice = viewModel?.buyPrice?.value
                // 一手股数
                val perShareNumber = viewModel?.stockInfoData?.value?.perShareNumber
                // 最大可卖 =（（可用资金 - 印花税 - 佣金 - 平台使用费 - 代收费）/ 一手价格）* 一手股数
                // TODO 可用资金测试为1000000
                viewModel?.maxBuyCount?.value =
                    ((BigDecimal.valueOf(1000000).subtract(stampTax).subtract(commission).subtract(platformFee).subtract(
                        agentFee
                    )).divide(buyPrice, 0, RoundingMode.DOWN)).multiply(perShareNumber).toLong()
                /* 最大可买 */
                // 更新界面最大可买数量
                view?.updateMaxBuyNum(viewModel?.maxBuyCount?.value!!.toLong())
            }
        }
    }

    /**
     * 收到输入价格
     */
    fun onEditBuyPrice(price: String?) {
        if (TextUtils.isEmpty(price)) return
        viewModel?.buyPrice?.value = price?.toBigDecimal()
    }

    /**
     * 手动输入数量
     */
    fun onEditBuyCount(count: String?) {
        if (TextUtils.isEmpty(count)) return
        viewModel?.buyCount?.value = count?.toLong()
    }

    /**
     * 加减数量
     * @param type 1：加 2：减
     */
    fun addOrSubBuyCount(type: Int) {
        if (viewModel?.stockInfo?.value == null) {
            ToastUtil.instance.toast(R.string.stock_code_input_hint)
            return
        }
        var count = viewModel?.buyCount?.value
        if (count == null) {
            count = 0
        }
        if (type == 1) {
            viewModel?.buyCount?.value = count + viewModel?.stockInfoData?.value?.perShareNumber?.toInt()!!
        } else {
            if (count == 0L) return
            viewModel?.buyCount?.value = count - viewModel?.stockInfoData?.value?.perShareNumber?.toInt()!!
        }
    }

    /**
     * 设置选择的自选股
     */
    fun setStock(stockInfo: SearchStockInfo) {
        viewModel?.stockInfo?.value = stockInfo
        viewModel?.stockInfoData?.value = null
        viewModel?.buyPrice?.value = null
        viewModel?.buyCount?.value = null
        viewModel?.buyMoney?.value = null
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
        val request = GetStockInfoRequest(stockInfo.ts!!, stockInfo.code!!, transactions.createTransaction())
        Cache[ISimulationTradeNet::class.java]?.getStockInfo(request)
            ?.enqueue(Network.IHCallBack<GetStockInfoResponse>(request))
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
                        viewModel?.price?.value = sub.price!!
                        // 计算跌涨价格
                        val diffPrice = sub.price!!.subtract(sub.openPrice!!)
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

        viewModel?.buyPrice?.value = stockInfoData.realPrice
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