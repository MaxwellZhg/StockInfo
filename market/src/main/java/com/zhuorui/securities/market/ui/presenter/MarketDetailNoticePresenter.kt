package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.MarketBaseInfoRequest
import com.zhuorui.securities.market.net.request.MarketNewsListRequest
import com.zhuorui.securities.market.net.response.MarketBaseInfoResponse
import com.zhuorui.securities.market.net.response.MarketNewsListResponse
import com.zhuorui.securities.market.ui.adapter.MarketNoticeInfoTipsAdapter
import com.zhuorui.securities.market.ui.view.MarketDetailNoticeView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailNoticeViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:56
 *    desc   :
 */
class MarketDetailNoticePresenter: AbsNetPresenter<MarketDetailNoticeView, MarketDetailNoticeViewModel>() {
    var list :ArrayList<MarketBaseInfoResponse.Source> = ArrayList()
    override fun init() {
        super.init()
    }
    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.infoList?.observe(it,
                androidx.lifecycle.Observer<List<MarketBaseInfoResponse.Source>> { t ->
                    view?.addIntoNoticeData(t)
                })
        }
    }

    fun getMarketBaseInfoData(code:String,currentPage:Int){
        val requset =  MarketBaseInfoRequest(code, currentPage, 15,transactions.createTransaction())
        requset?.let {
            Cache[IStockNet::class.java]?.getMarketBaseInfoList(it)
                ?.enqueue(Network.IHCallBack<MarketBaseInfoResponse>(requset))
        }
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onMarketNewsListResponse(response: MarketBaseInfoResponse){
        if (!transactions.isMyTransaction(response)) return
        val datas = response.data
       if(datas.sourceList.isNullOrEmpty()){
            view?.noMoreData()
        }else {
            viewModel?.infoList?.value = datas.sourceList
        }
    }

    fun getNoticeAdapter(): MarketNoticeInfoTipsAdapter {
        return MarketNoticeInfoTipsAdapter()
    }

/*    fun getModelData(){
        list.clear()
        for(i in 0..19){
            list.add(i)
        }
        viewModel?.infoList?.value = list
    }*/

}