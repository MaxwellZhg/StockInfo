package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.GetAllAttachmentRequest
import com.zhuorui.securities.market.net.request.MarketNewsListRequest
import com.zhuorui.securities.market.net.response.GetAllAttachmentResponse
import com.zhuorui.securities.market.net.response.MarketNewsListResponse
import com.zhuorui.securities.market.ui.view.MarketDetailNoticeDetailView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailNoticeDetailViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/12
 * Desc:
 */
class MarketDetailNoticeDetailPresenter :AbsNetPresenter<MarketDetailNoticeDetailView,MarketDetailNoticeDetailViewModel>(){
    override fun init() {
        super.init()
    }
   fun getAllAttachment(lineId:String){
       val requset =  GetAllAttachmentRequest(lineId,transactions.createTransaction())
       requset?.let {
           Cache[IStockNet::class.java]?.getAllAttachment(requset)?.enqueue(Network.IHCallBack<GetAllAttachmentResponse>(requset))
       }

   }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onMarketNoticeDetail(response: GetAllAttachmentResponse){
        if (!transactions.isMyTransaction(response)) return
        val datas = response.data
    }

}