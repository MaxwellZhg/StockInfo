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
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.MarketNewsListRequest
import com.zhuorui.securities.market.net.request.StockConsInfoRequest
import com.zhuorui.securities.market.net.response.MarketNewsListResponse
import com.zhuorui.securities.market.net.response.StockConsInfoResponse
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

    fun getStockConsInfo(code:String,pageSize:Int,sort:Int,sortTyep:Int,isFresh:Boolean,isInit:Boolean){
        this.isFresh=isFresh
        this.isInit = isInit
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
        view?.loadConsStockFail()
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockConsPointStateEventResponse(event: StockConsPointStateEvent) {
        view?.showStateChangeEvent(event.state)
    }

    fun getNewsListData(code:String,currentPage:Int){
        val requset =  MarketNewsListRequest(code, currentPage, 15,transactions.createTransaction())
        requset?.let {
            Cache[IStockNet::class.java]?.getMarketNewsList(it)
                ?.enqueue(Network.IHCallBack<MarketNewsListResponse>(requset))
        }
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onMarketNewsListResponse(response: MarketNewsListResponse){
        if (!transactions.isMyTransaction(response)) return
        val datas = response.data
        if(datas.list.isNullOrEmpty()){
            //view?.noMoreData()
        }else {
           // viewModel?.infoList?.value = datas.list
        }
    }



}