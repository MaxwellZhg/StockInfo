package com.zhuorui.securities.market.ui.presenter

import android.text.TextUtils
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.model.OrderBrokerModel
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.socket.SocketApi
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicCapitalResponse
import com.zhuorui.securities.market.socket.push.StocksTopicHandicapResponse
import com.zhuorui.securities.market.socket.request.GetStockRequestBody
import com.zhuorui.securities.market.socket.response.GetCapitalResponse
import com.zhuorui.securities.market.socket.response.GetStockHandicapResponse
import com.zhuorui.securities.market.socket.vo.CapitalData
import com.zhuorui.securities.market.ui.view.MarketDetailCapitalView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailCapitalViewModel
import com.zhuorui.securities.market.util.MarketUtil

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:56
 *    desc   :
 */
class MarketDetailCapitalPresenter : AbsNetPresenter<MarketDetailCapitalView, MarketDetailCapitalViewModel>() {

    private var getCapitalReqId: String? = null
    private var mTs: String? = null
    private var mCode: String? = null
    private var stockTopic: StockTopic? = null
    private var mBmp: Boolean = false

    /**
     * 获取买卖经纪数据
     */
    fun getData(ts: String, code: String) {
        mTs = ts
        mCode = code
        getCapitalReqId = SocketClient.getInstance().postRequest(GetStockRequestBody(ts, code), SocketApi.GET_CAPITAL)
        mBmp = MarketUtil.isBMP(mTs)
        if (stockTopic != null && mBmp) {
            SocketClient.getInstance().unBindTopic(stockTopic)
        }
        view?.onTodayFundTransactionData(null)
    }


    /**
     * 获取股票盘口数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetCapital(response: GetCapitalResponse) {
        if (TextUtils.equals(getCapitalReqId, response.respId)) {
            readCapitalResponse(response?.data)
            if (!mBmp) {
                stockTopic = StockTopic(StockTopicDataTypeEnum.STOCK_PRICE, mTs.toString(), mCode.toString(), 2)
                SocketClient.getInstance().bindTopic(stockTopic)
            }
        }
    }

    private fun readCapitalResponse(data: CapitalData?) {
        data?.totalLargeSingleOutflow ?: 0.0

    }

    /**
     * 推送股票盘口数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicCapital(response: StocksTopicCapitalResponse) {

    }


    override fun destroy() {
        super.destroy()
        if (stockTopic != null) {
            SocketClient.getInstance().unBindTopic(stockTopic)
        }
    }

}