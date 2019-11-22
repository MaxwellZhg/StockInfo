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
import com.zhuorui.securities.market.event.AddTopicStockEvent
import com.zhuorui.securities.market.event.DeleteTopicStockEvent
import com.zhuorui.securities.market.event.SocketConnectEvent
import com.zhuorui.securities.market.model.OrderBrokerModel
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.CollectionStockRequest
import com.zhuorui.securities.market.net.request.DeleteStockRequest
import com.zhuorui.securities.market.socket.SocketApi
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicHandicapResponse
import com.zhuorui.securities.market.socket.push.StocksTopicOrderBrokerResponse
import com.zhuorui.securities.market.socket.push.StocksTopicOrderResponse
import com.zhuorui.securities.market.socket.request.GetStockTradeRequestBody
import com.zhuorui.securities.market.socket.response.GetStockHandicapResponse
import com.zhuorui.securities.market.socket.response.GetStocksOrderBrokerResponse
import com.zhuorui.securities.market.socket.response.GetStocksOrderResponse
import com.zhuorui.securities.market.socket.vo.OrderBrokerData
import com.zhuorui.securities.market.socket.vo.OrderData
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
    private var stockTopic: StockTopic? = null
    private var orderBrokerTopic: StockTopic? = null
    private var orderTopic: StockTopic? = null
    private var getStockHandicapReqId: String? = null
    private var getStockOrderBrokerReqId: String? = null
    private var getStockOrderReqId: String? = null
    private var mTs = ""
    private var mCode = ""
    private var mTsCode = ""
    private var mType = 0

    private var mBmp: Boolean = false

    fun setStockInfo(ts: String, code: String, type: Int) {
        mTs = ts
        mCode = code
        mType = type
        mTsCode = "$code.$ts"
    }

    /**
     * 获取数据
     */
    fun getData(isBMP: Boolean) {
        mBmp = isBMP
        checkFollow()
        getStockData()
        if (isBMP) {
            //bmp 取消订阅
            if (stockTopic != null)
                SocketClient.getInstance().unBindTopic(stockTopic)
            if (orderBrokerTopic != null)
                SocketClient.getInstance().unBindTopic(orderBrokerTopic)
            if (orderTopic != null)
                SocketClient.getInstance().unBindTopic(orderTopic)

        } else {
            getOrderBrokerData()
            getBuyingSellingFilesData()
        }
    }

    /**
     * 获取股票盘口数据
     */
    private fun getStockData() {
        getStockHandicapReqId =
            SocketClient.getInstance().postRequest(GetStockTradeRequestBody(mTs, mCode), SocketApi.GET_STOCK_HANDICAP)
        android.os.Handler().postDelayed({ viewModel?.mStockHandicapData?.value = null }, 2000)

    }

    /**
     * 获取买卖经纪数据
     */
    private fun getOrderBrokerData() {
        getStockOrderBrokerReqId = SocketClient.getInstance()
            .postRequest(GetStockTradeRequestBody(mTs, mCode), SocketApi.GET_STOCK_ORDER_BROKER)
        val datas2 = mutableListOf<OrderBrokerModel>()
        for (i: Int in 1..30) {
            datas2.add(OrderBrokerModel(i.toString(), "item$i"))
        }
        view?.upOrderBrokerData(datas2, datas2)

    }

    /**
     * 获取买卖十档数据
     */
    private fun getBuyingSellingFilesData() {
        getStockOrderReqId =
            SocketClient.getInstance().postRequest(GetStockTradeRequestBody(mTs, mCode), SocketApi.GET_STOCK_ORDER)
        val datas = mutableListOf<OrderData.AskBidModel>()
        for (i: Int in 1..10) {
            datas.add(OrderData.AskBidModel(i.toString(), i.toString(), i.toString()))
        }
        view?.upBuyingSellingFilesData(datas, datas)

    }

    /**
     * 获取topbar 显示股票价格信息
     * */
    fun getTopBarPriceInfo() {
        val priceData = viewModel?.mStockHandicapData?.value
        topBarInfoTp = 1
        if (priceData != null) {
            val price = priceData.last!!.toFloat()
            val preClosePrice = priceData.preClose!!.toFloat()
            val diffPrice = price - preClosePrice
            view?.upTopBarInfo(
                String.format("%.3f  %+.3f  %+.2f%%", price, diffPrice, diffPrice * 100 / preClosePrice),
                LocalSettingsConfig.getInstance().getUpDownColor(price, preClosePrice)
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
        var closingTimeMillis = if (h < 9 || h >= 16) TimeZoneUtil.currentTimeMillis() else 0
        view?.upTopBarInfo(MarketUtil.getStockStatusTxt(mTs, closingTimeMillis, true), topbarTxtColor)
    }

    fun getTopBarOnfoType(): Int {
        return topBarInfoTp
    }

    /**
     * 添加删除自选到自选
     */
    fun collectionStock(stockInfo: SearchStockInfo) {
        if (LocalAccountConfig.getInstance().isLogin()) {
            // 已登录
            if (isCollected) {
                //取消收藏
                val request =
                    DeleteStockRequest(transactions.createTransaction(), stockInfo, stockInfo.ts!!, stockInfo.code!!)
                Cache[IStockNet::class.java]?.delelte(request)?.enqueue(Network.IHCallBack<BaseResponse>(request))
            } else {
                //添加收藏
                val request = DeleteStockRequest(transactions.createTransaction(), stockInfo, stockInfo.ts!!, stockInfo.code!!)
                Cache[IStockNet::class.java]?.delelte(request)?.enqueue(Network.IHCallBack<BaseResponse>(request))
            }
        } else {
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
            isCollected = !isCollected
            view?.upFollow(isCollected)
        }
    }


    override fun onBaseResponse(response: BaseResponse) {
        super.onBaseResponse(response)
        if (response.request is CollectionStockRequest) {
            // 传递添加自选股事件
            RxBus.getDefault().post(AddTopicStockEvent((response.request as CollectionStockRequest).stockInfo))
            ScreenCentralStateToast.show(ResUtil.getString(R.string.add_topic_successful))
            isCollected = true
            view?.upFollow(true)
        } else if (response.request is DeleteStockRequest) {
            val request = response.request as DeleteStockRequest
            // 传递删除自选股事件
            RxBus.getDefault().post(DeleteTopicStockEvent(request.ts!!, request.codes[0]!!))
            ScreenCentralStateToast.show(ResUtil.getString(R.string.delete_successful))
            isCollected = false
            view?.upFollow(false)
        }
    }

    /**
     * 获取股票盘口数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetStockHandicap(response: GetStockHandicapResponse) {
        if (TextUtils.equals(getStockHandicapReqId, response.respId)) {
            viewModel?.mStockHandicapData?.value = response.data
            if (!mBmp) {
                stockTopic = StockTopic(StockTopicDataTypeEnum.STOCK_PRICE, mTs, mCode, mType)
                SocketClient.getInstance().bindTopic(stockTopic)
            }
        }
    }

    /**
     * 推送股票盘口数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicHandicap(response: StocksTopicHandicapResponse) {
        if (TextUtils.equals(getStockHandicapReqId, response.respId)) {
            viewModel?.mPushStockHandicapData?.value = response.body
        }
    }

    /**
     * 获取买卖经纪数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetStocksOrderBroker(response: GetStocksOrderBrokerResponse) {
        if (TextUtils.equals(getStockOrderBrokerReqId, response.respId)) {
            val data: OrderBrokerData? = response.data
            orderBrokerTopic = StockTopic(StockTopicDataTypeEnum.STOCK_ORDER_BROKER, mTs, mCode, mType)
            SocketClient.getInstance().bindTopic(orderBrokerTopic)
        }
    }

    /**
     * 推送买卖经纪数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicOrderBroker(response: StocksTopicOrderBrokerResponse) {
        if (TextUtils.equals(getStockHandicapReqId, response.respId)) {
            val data = response.body
        }
    }

    /**
     * 获取买卖十档数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetStocksOrder(response: GetStocksOrderResponse) {
        if (TextUtils.equals(getStockOrderReqId, response.respId)) {
            viewModel?.mOrderData?.value = response.data
            val data: OrderData? = response.data
            orderTopic = StockTopic(StockTopicDataTypeEnum.STOCK_ORDER, mTs, mCode, mType)
            SocketClient.getInstance().bindTopic(orderTopic)
        }
    }

    /**
     * 推送买卖十档数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicOrder(response: StocksTopicOrderResponse) {
        if (TextUtils.equals(getStockHandicapReqId, response.respId)) {
            viewModel?.mOrderData?.value = response.body
        }
    }


    /**
     * 检查关注状态
     */
    private fun checkFollow() {
        isCollected = false
        val localStocks = LocalStocksConfig.getInstance().getStocks()
        if (localStocks.isNotEmpty()) {
            for (stock in localStocks) {
                if (stock.ts.equals(mTs) && stock.code.equals(mCode)) {
                    isCollected = true
                    break
                }
            }
        }
        view?.upFollow(isCollected)
    }


    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSocketDisconnectEvent(event: SocketConnectEvent) {
        view?.updateNetworkState(event.connected)
    }

    override fun destroy() {
        super.destroy()
        if (stockTopic != null)
            SocketClient.getInstance().unBindTopic(stockTopic)
        if (orderBrokerTopic != null)
            SocketClient.getInstance().unBindTopic(orderBrokerTopic)
        if (orderTopic != null)
            SocketClient.getInstance().unBindTopic(orderTopic)
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.mStockHandicapData?.observe(it,
                androidx.lifecycle.Observer { t ->
                    view?.setData(t)
                    onUpTitleInfo()
                })
            viewModel?.mPushStockHandicapData?.observe(it,
                androidx.lifecycle.Observer { t ->
                    view?.upData(t)
                    onUpTitleInfo()
                })
            viewModel?.mOrderData?.observe(it,
                androidx.lifecycle.Observer { t ->
                    view?.upBuyingSellingFilesData(t?.asklist, t?.bidlist)
                })
        }
    }

    private fun onUpTitleInfo() {
        if (topBarInfoTp == 1) {
            getTopBarPriceInfo()
        } else {
            getTopBarStockStatusInfo()
        }
    }

}