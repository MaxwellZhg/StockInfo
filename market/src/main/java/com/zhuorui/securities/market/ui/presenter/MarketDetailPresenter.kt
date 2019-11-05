package com.zhuorui.securities.market.ui.presenter

import android.graphics.Color
import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import com.zhuorui.commonwidget.ScreenCentralStateToast
import com.zhuorui.commonwidget.config.LocalSettingsConfig
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.config.LocalStocksConfig
import com.zhuorui.securities.market.customer.view.StockDetailView
import com.zhuorui.securities.market.event.AddTopicStockEvent
import com.zhuorui.securities.market.event.DeleteTopicStockEvent
import com.zhuorui.securities.market.model.*
import com.zhuorui.securities.market.event.MarketDetailInfoEvent
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.CollectionStockRequest
import com.zhuorui.securities.market.net.request.DeleteStockRequest
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicPriceResponse
import com.zhuorui.securities.market.ui.view.MarketDetailView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailViewModel
import com.zhuorui.securities.market.util.MarketUtil
import com.zhuorui.securities.personal.config.LocalAccountConfig

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-11 16:10
 *    desc   :
 */
class MarketDetailPresenter : AbsNetPresenter<MarketDetailView, MarketDetailViewModel>() {

    private val topbarTxtColor = Color.parseColor("#ffA4B2CB")
    private var topBarInfoTp = 0 // 0 状态，1 价格
    private var isCollected: Boolean = false
    private var stocksInfo: SearchStockInfo? = null
    private var stockTopic: StockTopic? = null


    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.pushStockPriceData?.observe(it,
                androidx.lifecycle.Observer<PushStockPriceData> { t ->
                    val data = object : StockDetailView.IStockDatailData {
                        /**
                         * 当前价
                         *
                         * @return
                         */
                        override fun getPrice(): Float {
                            return t.last!!.toFloat()
                        }

                        /**
                         * 开盘价
                         *
                         * @return
                         */
                        override fun getOpenPrice(): Float {
                            return t.open!!.toFloat()
                        }

                        /**
                         * 昨收价
                         *
                         * @return
                         */
                        override fun getPreClosePrice(): Float {
                            return t.preClose!!.toFloat()
                        }

                        /**
                         * 最低价
                         *
                         * @return
                         */
                        override fun getLowPrice(): Float {
                            return 0f
                        }

                        /**
                         * 最高价
                         *
                         * @return
                         */
                        override fun getHighPrice(): Float {
                            return 0f
                        }

                    }
                    view?.upData(data)
                    if (topBarInfoTp == 1) {
                        getTopBarPriceInfo()
                    } else {
                        getTopBarStockStatusInfo()
                    }
                })
        }
    }

    /**
     * 获取topbar 显示股票价格信息
     * */
    fun getTopBarPriceInfo() {
        val priceData = viewModel?.pushStockPriceData?.value
        topBarInfoTp = 1
        if (priceData != null) {
            val price = priceData.last!!.toFloat()
            val preClosePrice = priceData.preClose!!.toFloat()
            val diffPrice = price - preClosePrice
            view?.upTopBarInfo(
                String.format("%.3f %+.3f %+.2f%%", price, diffPrice, diffPrice * 100 / preClosePrice),
                LocalSettingsConfig.read().getUpDownColor(price, preClosePrice)
            )
        } else {
            view?.upTopBarInfo("--- -- --", topbarTxtColor)
        }
    }

    /**
     * 获取topbar 显示股票状态信息
     * */
    fun getTopBarStockStatusInfo() {
        topBarInfoTp = 0
        val h = Integer.valueOf(TimeZoneUtil.currentTime("HH"))
        var closingTimeMillis =
            if (h < 9 || h >= 16) TimeZoneUtil.currentTimeMillis() else 0
        view?.upTopBarInfo(
            MarketUtil.getStockStatusTxt(stocksInfo?.ts, closingTimeMillis, true),
            topbarTxtColor
        )
    }

    fun getTopBarOnfoType(): Int {
        return topBarInfoTp
    }

    /**
     * 添加删除自选到自选
     */
    fun collectionStock(stockInfo: SearchStockInfo) {
        if (LocalAccountConfig.read().isLogin()) {
            // 已登录
            if (isCollected) {
                //取消收藏
                val ids = arrayOf(stockInfo.id)
                val request =
                    DeleteStockRequest(
                        stockInfo,
                        ids,
                        stockInfo.ts!!,
                        stockInfo.code!!,
                        transactions.createTransaction()
                    )
                Cache[IStockNet::class.java]?.delelte(request)
                    ?.enqueue(Network.IHCallBack<BaseResponse>(request))
            } else {
                //添加收藏
                val requset =
                    CollectionStockRequest(
                        stockInfo,
                        stockInfo.type!!,
                        stockInfo.ts!!,
                        stockInfo.code!!,
                        0,
                        transactions.createTransaction()
                    )
                Cache[IStockNet::class.java]?.collection(requset)
                    ?.enqueue(Network.IHCallBack<BaseResponse>(requset))
            }
        } else {
            isCollected = !isCollected
            // 未登录
            if (isCollected) {
                // 传递删除自选股事件
                RxBus.getDefault().post(DeleteTopicStockEvent(stockInfo.ts!!, stockInfo.code!!))
                ScreenCentralStateToast.show(ResUtil.getString(R.string.delete_successful))
            } else {
                // 传递添加自选股事件
                RxBus.getDefault().post(AddTopicStockEvent(stockInfo))
                ScreenCentralStateToast.show(ResUtil.getString(R.string.add_topic_successful))
            }
            view?.upFollow(isCollected)
        }
    }


    override fun onBaseResponse(response: BaseResponse) {
        super.onBaseResponse(response)
        if (response.request is CollectionStockRequest) {
            // 传递添加自选股事件
            RxBus.getDefault().post(AddTopicStockEvent((response.request as CollectionStockRequest).stockInfo))
            //updateCurrentFragmentData(str)
            ScreenCentralStateToast.show(ResUtil.getString(R.string.add_topic_successful))
            isCollected = true
            view?.upFollow(true)
        } else if (response.request is DeleteStockRequest) {
            val request = response.request as DeleteStockRequest
            // 传递删除自选股事件
            RxBus.getDefault().post(DeleteTopicStockEvent(request.ts!!, request.code!!))
            ScreenCentralStateToast.show(ResUtil.getString(R.string.delete_successful))
            isCollected = false
            view?.upFollow(false)
        }
    }

    fun getData(stockInfo: SearchStockInfo) {
        stocksInfo = stockInfo;
        isCollected = false
        val localStocks = LocalStocksConfig.getInstance().getStocks()
        if (localStocks.isNotEmpty()) {
            for (stock in localStocks) {
                if (stock.ts.equals(stockInfo.ts) && stock.code.equals(stockInfo.code)) {
                    isCollected = true
                    break
                }
            }
        }
        view?.upFollow(isCollected)
        val datas = mutableListOf<Int>()
        for (i: Int in 1..10) {
            datas.add(i)
        }
        view?.upBuyingSellingFilesData(7458f, 2442f, datas, datas)
        val datas2 = mutableListOf<String>()
        for (i: Int in 1..30) {
            datas2.add("item$i")
        }
        view?.upOrderBrokerData(datas2, datas2)
        stockTopic = StockTopic(StockTopicDataTypeEnum.STOCK_PRICE, stockInfo.ts!!, stockInfo.code!!, 2)
        SocketClient.getInstance().bindTopic(stockTopic)
    }

    /**
     * 股票价格变化
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicPriceResponse(response: StocksTopicPriceResponse) {
        if (stocksInfo == null) return
        val prices: List<PushStockPriceData> = response.body
        for (price in prices) {
            val tsCode = price.code + "." + price.ts
            if (TextUtils.equals(stocksInfo?.tsCode, tsCode)) {
                viewModel?.pushStockPriceData?.value = price
                break
            }
        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onChangeInfoTypeEvent(event: MarketDetailInfoEvent) {
        view?.changeInfoTypeData(event)
    }

    override fun destroy() {
        super.destroy()
        if (stockTopic != null) {
            SocketClient.getInstance().unBindTopic(stockTopic)
        }
    }

}