package com.zhuorui.securities.market.ui.presenter

import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.JsonUtil
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.model.CapitalTrendModel
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.socket.SocketApi
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicCapitalResponse
import com.zhuorui.securities.market.socket.request.GetStockDataByTsCodeRequestBody
import com.zhuorui.securities.market.socket.response.GetCapitalResponse
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
        getCapitalReqId =
            SocketClient.getInstance().postRequest(GetStockDataByTsCodeRequestBody(ts, code), SocketApi.GET_CAPITAL)
        mBmp = MarketUtil.isBMP(mTs)
        if (stockTopic != null && mBmp) {
            SocketClient.getInstance().unBindTopic(stockTopic)
        }
    }


    /**
     * 获取股票盘口数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetCapital(response: GetCapitalResponse) {
        if (TextUtils.equals(getCapitalReqId, response.respId)) {
            val capitalData = JsonUtil.fromJson(response.data.toString(), CapitalData::class.java)
            view?.onTodayFundTransactionData(capitalData)
            readCapitalResponse(capitalData)
            if (!mBmp) {
                stockTopic = StockTopic(StockTopicDataTypeEnum.STOCK_PRICE, mTs.toString(), mCode.toString(), 2)
                SocketClient.getInstance().bindTopic(stockTopic)
            }
        }
    }

    private fun readCapitalResponse(data: CapitalData?) {
        val maps = data?.maps
        val capitalTrends = mutableListOf<CapitalTrendModel>()
        if (maps != null) {
            for ((key, value) in maps) {
                capitalTrends.add(CapitalTrendModel(TimeZoneUtil.parseTime(key, "yyyyMMddHHmm"), value))
            }
        }
        viewModel?.mCapitalTrends?.value = capitalTrends
    }

    /**
     * 推送股票盘口数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicCapital(response: StocksTopicCapitalResponse) {
        val capitalData = JsonUtil.fromJson(response.body.toString(), CapitalData::class.java)
    }


    override fun destroy() {
        super.destroy()
        if (stockTopic != null) {
            SocketClient.getInstance().unBindTopic(stockTopic)
        }
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.mCapitalTrends?.observe(it,
                androidx.lifecycle.Observer { t ->
                    view?.onTodatCapitalFlowTrendData(t)
                })
        }
    }

}