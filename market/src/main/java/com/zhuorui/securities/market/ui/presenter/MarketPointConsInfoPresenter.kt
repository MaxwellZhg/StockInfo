package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.MarketNewsListRequest
import com.zhuorui.securities.market.net.response.MarketNewsListResponse
import com.zhuorui.securities.market.ui.adapter.MarketPointConsInfoAdapter
import com.zhuorui.securities.market.ui.adapter.MarketPointInfoAdapter
import com.zhuorui.securities.market.ui.view.MarketPointConsInfoView
import com.zhuorui.securities.market.ui.viewmodel.MarketPointConsInfoViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:
 */
class MarketPointConsInfoPresenter :AbsNetPresenter<MarketPointConsInfoView, MarketPointConsInfoViewModel>(){
    var info =ArrayList<Int>()
    override fun init() {
        super.init()
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.infoList?.observe(it,
                androidx.lifecycle.Observer<List<MarketNewsListResponse.DataList>> { t ->
                    view?.addIntoInfoData(t)
                })
        }
    }


    fun getMarketPointInfoAdapter(): MarketPointInfoAdapter {
        return  MarketPointInfoAdapter()
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
            view?.noMoreData()
        }else {
            viewModel?.infoList?.value = datas.list
        }
    }

    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
        view?.loadFailData()
    }
}