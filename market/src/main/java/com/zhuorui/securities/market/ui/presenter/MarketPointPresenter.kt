package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.event.MarketPointConsEvent
import com.zhuorui.securities.market.event.StockConsEvent
import com.zhuorui.securities.market.event.StockConsPointStateEvent
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.MarketNewsListRequest
import com.zhuorui.securities.market.net.request.StockConsInfoRequest
import com.zhuorui.securities.market.net.response.MarketNewsListResponse
import com.zhuorui.securities.market.net.response.StockConsInfoResponse
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StockTopicIndexHandicapResponse
import com.zhuorui.securities.market.ui.adapter.MarketPointConsInfoAdapter
import com.zhuorui.securities.market.ui.adapter.MarketPointInfoAdapter
import com.zhuorui.securities.market.ui.view.MarketPointView
import com.zhuorui.securities.market.ui.viewmodel.MarketPointViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/22
 * Desc:
 */
class MarketPointPresenter :AbsNetPresenter<MarketPointView,MarketPointViewModel>(){
    var isFresh :Boolean =false
    var isInit:Boolean =false
    var isInfo:Boolean =false
    private var stockTopic: StockTopic? = null
    private var code:String?=null
    override fun init() {
        super.init()
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {

    }



    fun getMarketInfoAdapter(): MarketPointConsInfoAdapter {
        return MarketPointConsInfoAdapter()
    }


    fun getMarketPointInfoAdapter():MarketPointInfoAdapter{
        return  MarketPointInfoAdapter()
    }



    /**
     * 获取topbar 显示股票状态信息
     * */
    fun getTopBarStockStatusInfo() {
        val h = Integer.valueOf(TimeZoneUtil.currentTime("HH"))
        var closingTimeMillis =
            if (h < 9 || h >= 16) TimeZoneUtil.currentTimeMillis() else 0
       /* view?.upTopBarInfo(
            MarketUtil.getStockStatusTxt(stocksInfo?.ts, closingTimeMillis, true),
            topbarTxtColor
        )*/
    }

    fun getStockConsInfo(code:String,pageSize:Int,sort:Int,sortTyep:Int,isFresh:Boolean,isInit:Boolean,isInfo:Boolean){
        this.isFresh=isFresh
        this.isInit = isInit
        this.isInfo = isInfo
        val requset = StockConsInfoRequest(code, pageSize,sort,sortTyep,"HK" ,transactions.createTransaction())
        Cache[IStockNet::class.java]?.getStockConsInfo(requset)
            ?.enqueue(Network.IHCallBack<StockConsInfoResponse>(requset))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockConsInfoResponse(response: StockConsInfoResponse) {
        if (!transactions.isMyTransaction(response)) return
        val datas = response.data
        if(datas.list.isNotEmpty()){
            if(isFresh){
                if(isInfo){
                    view?.detailInfoState()
                }
                view?.refreshSuccess()
            }else{
                if(isInit){
                    view?.setLoadMoreState()
                }else {
                    view?.loadMoreSuccess()
                }
            }
            RxBus.getDefault().post(StockConsEvent(datas.list))
            view?.showAllCount(datas.total.toInt())
        }
    }

    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
        RxBus.getDefault().post(MarketPointConsEvent())
        if(isFresh){
            view?.loadConsFreshFail()
        }else{
            view?.loadConsStockFail()
        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockConsPointStateEventResponse(event: StockConsPointStateEvent) {
        view?.showStateChangeEvent(event.state)
    }

    fun bindMarketPointhandicap(ts:String,code:String){
        // 订阅股价
        this.code = code
        stockTopic = StockTopic(StockTopicDataTypeEnum.HANDICAP, ts, code, 1)
        SocketClient.getInstance().bindTopic(stockTopic)
    }


    /**
     * 订阅返回股价波动
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksTopicPriceResponse(response: StockTopicIndexHandicapResponse) {
        if(response.body!=null&&response.body?.code==code) {
            view?.getpushData(response.body!!)
        }
    }


}