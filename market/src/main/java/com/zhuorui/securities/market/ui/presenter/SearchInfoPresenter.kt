package com.zhuorui.securities.market.ui.presenter

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.zhuorui.commonwidget.ScreenCentralStateToast
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.config.LocalSearchConfig
import com.zhuorui.securities.market.config.LocalStocksConfig
import com.zhuorui.securities.market.event.*
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.SearchStokcInfoEnum
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.TestSeachDefaultData
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.CollectionStockRequest
import com.zhuorui.securities.market.net.request.DeleteStockRequest
import com.zhuorui.securities.market.ui.adapter.SearchInfoAdapter
import com.zhuorui.securities.market.ui.view.SearchInfoView
import com.zhuorui.securities.market.ui.viewmodel.SearchInfoViewModel
import com.zhuorui.securities.personal.config.LocalAccountConfig
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/19
 * Desc:
 */
class SearchInfoPresenter(context: Context) : AbsNetPresenter<SearchInfoView,SearchInfoViewModel>(){
    var list =ArrayList<TestSeachDefaultData>()
    var listhot =ArrayList<Int>()
    var history =ArrayList<SearchStockInfo>()
    override fun init() {
        super.init()
    }
    fun getData(){
        listhot.clear()
        for(i in 0..5){
            listhot.add(i)
        }
 /*       for(i inL){
            history.add(i)
        }*/
        var datas=LocalSearchConfig.getInstance().getStocks()
  /*      val localStocks = LocalStocksConfig.getInstance().getStocks()
        if (localStocks.isNotEmpty()) {
            for (item in datas) {
                for (stock in localStocks) {
                    if (stock.ts.equals(item.ts) && stock.code.equals(item.code)) {
                        item.collect = true
                        localStocks.remove(stock)
                        break
                    }
                }
            }
        }*/
        var data=TestSeachDefaultData(listhot,datas)
        viewModel?.searchInfoDatas?.value=data
    }
    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.searchInfoDatas?.observe(it,
                androidx.lifecycle.Observer<TestSeachDefaultData> { t ->
                    view?.notifyDataSetChanged(t)
                })
        }
    }
    fun getAdapter(): SearchInfoAdapter? {
        return SearchInfoAdapter(context)
    }

    fun initViewPager(str:String,enum:SearchStokcInfoEnum?){
        RxBus.getDefault().post(SelectsSearchTabEvent(str,enum))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSearchChangMoreEvent(event: ChageSearchTabEvent) {
        view?.changeTab(event)
    }

/*    fun collectionStock(stockInfo: SearchStockInfo, isCollected: Boolean) {
        // 点击添加到自选列表
  *//*      if (LocalAccountConfig.read().isLogin()) {
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
            stockInfo.collect = !isCollected
            // 未登录
            if (isCollected) {
                // 传递删除自选股事件
                RxBus.getDefault().post(DeleteTopicStockEvent(stockInfo.ts!!, stockInfo.code!!))
                ScreenCentralStateToast.show(ResUtil.getString(R.string.delete_successful))
                setAdapterDataNotify(stockInfo,stockInfo.collect)
            } else {
                // 传递添加自选股事件
                RxBus.getDefault().post(AddTopicStockEvent(stockInfo))
                ScreenCentralStateToast.show(ResUtil.getString(R.string.add_topic_successful))
                setAdapterDataNotify(stockInfo,stockInfo.collect)
            }
        }*//*
    }*/

/*    override fun onBaseResponse(response: BaseResponse) {
        super.onBaseResponse(response)
       if (response.request is CollectionStockRequest) {
            RxBus.getDefault().post(AddTopicStockEvent((response.request as CollectionStockRequest).stockInfo))
            toast(R.string.add_topic_successful)
            (response.request as CollectionStockRequest).stockInfo.collect = true
            //updateCurrentFragmentData(str)
            setAdapterDataNotify(  (response.request as CollectionStockRequest).stockInfo, (response.request as CollectionStockRequest).stockInfo.collect)
            ScreenCentralStateToast.show(ResUtil.getString(R.string.add_topic_successful))
        } else if (response.request is DeleteStockRequest) {
            val request = response.request as DeleteStockRequest
            request.stockInfo?.collect = false
            //updateCurrentFragmentData(str)
            request.stockInfo?.let { request.stockInfo?.collect?.let { it1 -> setAdapterDataNotify(it, it1) } }
            // 传递删除自选股事件
            RxBus.getDefault().post(DeleteTopicStockEvent(request.ts!!, request.code!!))
            ScreenCentralStateToast.show(ResUtil.getString(R.string.delete_successful))
        }
    }*/
/*    fun setAdapterDataNotify(stockInfo: SearchStockInfo,collect:Boolean){
        viewModel?.searchInfoDatas?.value.let {
      *//*      if(it!=null){
                for(data in it.history){
                    if(stockInfo.id==data?.id){
                        data?.collect=collect
                    }
                }
            }*//*
        }
        view?.notifyAdapter()
    }*/


    fun detailStrInfo(str:String){
        LocalSearchConfig.getInstance().remove(str)
        setAdapterDataInfoNotify(str)
    }
    fun setAdapterDataInfoNotify(str:String){
        view?.notifyAdapter()
    }

}