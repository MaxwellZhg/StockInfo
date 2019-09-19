package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicPriceResponse
import com.zhuorui.securities.market.socket.push.StocksTopicTransResponse
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
class SimulationTradingStocksPresenter :
    AbsNetPresenter<SimulationTradingStocksView, SimulationTradingStocksViewModel>() {

    private var stockTopicPrice: StockTopic? = null
    private var stockTopicTrans: StockTopic? = null
    private val disposables = LinkedList<Disposable>()

    /**
     * 设置选择的自选股
     */
    fun setStock(stockInfo: SearchStockInfo) {
        viewModel?.stockInfo?.value = stockInfo
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
    }

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