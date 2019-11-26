package com.zhuorui.securities.market.ui.presenter

import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.JsonUtil
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.model.CapitalTrendModel
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.GetCapitalFlowTimeRequest
import com.zhuorui.securities.market.net.response.StockConsInfoResponse
import com.zhuorui.securities.market.socket.SocketApi
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicCapitalResponse
import com.zhuorui.securities.market.socket.request.GetStockDataByTsCodeRequestBody
import com.zhuorui.securities.market.socket.response.GetCapitalResponse
import com.zhuorui.securities.market.socket.vo.CapitalData
import com.zhuorui.securities.market.ui.view.MarketDetailCapitalView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailCapitalViewModel
import com.zhuorui.securities.market.util.MarketUtil
import java.math.BigDecimal

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
    private var mDay: Int = 5;

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
     * 查询资金统计数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetCapital(response: GetCapitalResponse) {
        if (TextUtils.equals(getCapitalReqId, response.respId)) {
            readCapitalData(response.data)
            readCapitalTrends(response.data?.maps)
            getCapitalFlowTime(mDay)
            if (!mBmp) {
                stockTopic = StockTopic(StockTopicDataTypeEnum.STOCK_PRICE, mTs.toString(), mCode.toString(), 2)
                SocketClient.getInstance().bindTopic(stockTopic)
            }
        }
    }

    /**
     * 推送资金统计数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicCapital(response: StocksTopicCapitalResponse) {
        val data = response.body
        readCapitalData(data)
        if (data?.latestTrends != null){
            addCapitalTrend(data.cacheDay.toString(),data.latestTrends!!)
        }
    }

    /**
     * 查询资金流向
     */
    fun getCapitalFlowTime(day: Int) {
        mDay = day
        val requset = GetCapitalFlowTimeRequest(mTs.toString(), mCode.toString(), day, transactions.createTransaction())
        Cache[IStockNet::class.java]?.getCapitalFlowTime(requset)
            ?.enqueue(Network.IHCallBack<BaseResponse>(requset))
    }

    /**
     * 处理成交分成数据
     */
    private fun readCapitalData(data:CapitalData?){
        val largeIn = data?.totalLargeSingleInflow ?: BigDecimal(0)
        val largeOut = data?.totalLargeSingleOutflow ?: BigDecimal(0)
        val mediumIn = data?.totalMediumInflow ?: BigDecimal(0)
        val mediumOut = data?.totalMediumOutflow ?: BigDecimal(0)
        val smallIn = data?.totalSmallInflow ?: BigDecimal(0)
        val smallOut = data?.totalSmallOutflow ?: BigDecimal(0)
        viewModel?.mCapitalData?.value = CapitalData(
            data?.cacheDay,
            null,
            largeIn,
            largeOut,
            mediumIn,
            mediumOut,
            smallIn,
            smallOut,
            null)

    }

    /**
     * 处理资金趋势数据
     */
    private fun readCapitalTrends(maps: Map<String, BigDecimal>?) {
        val capitalTrends = mutableListOf<CapitalTrendModel>()
        if (maps != null) {
            for ((key, value) in maps) {
                capitalTrends.add(CapitalTrendModel(TimeZoneUtil.parseTime(key, "yyyyMMddHHmm"), value))
            }
        }
        viewModel?.mCapitalTrends?.value = capitalTrends
    }

    /**
     * 追加资金趋势数据
     */
    private fun addCapitalTrend(time: String, value: BigDecimal) {
        var capitalTrends = viewModel?.mCapitalTrends?.value
        if (capitalTrends == null) {
            capitalTrends = mutableListOf<CapitalTrendModel>()
        }
        capitalTrends.add(CapitalTrendModel(TimeZoneUtil.parseTime(time, "yyyyMMddHHmm"), value))
        viewModel?.mCapitalTrends?.value = capitalTrends
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
            viewModel?.mCapitalData?.observe(it,
                androidx.lifecycle.Observer { t ->
                    view?.onTodayFundTransactionData(t)
                })
            viewModel?.mCapitalTrends?.observe(it,
                androidx.lifecycle.Observer { t ->
                    view?.onTodatCapitalFlowTrendData(t)
                })
        }
    }

}