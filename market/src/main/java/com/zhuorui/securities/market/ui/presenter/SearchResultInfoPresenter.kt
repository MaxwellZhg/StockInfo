package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.config.LocalStocksConfig
import com.zhuorui.securities.market.event.*
import com.zhuorui.securities.market.model.*
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.CollectionStockRequest
import com.zhuorui.securities.market.net.request.StockSearchRequest
import com.zhuorui.securities.market.net.response.StockSearchResponse
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.ui.adapter.SeachAllofInfoAdapter
import com.zhuorui.securities.market.ui.adapter.StockAdapter
import com.zhuorui.securities.market.ui.adapter.StockInfoAdapter
import com.zhuorui.securities.market.ui.view.SearchResultInfoView
import com.zhuorui.securities.market.ui.viewmodel.SearchResultInfoViewModel
import com.zhuorui.securities.personal.config.LocalAccountConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/20
 * Desc:
 */
class SearchResultInfoPresenter : AbsNetPresenter<SearchResultInfoView, SearchResultInfoViewModel>() {
    var ts :SearchStokcInfoEnum?=null
    var list = ArrayList<SearchDeafaultData>()
    var listhot = ArrayList<SearchStockInfo>()
    var history = ArrayList<Int>()
    override fun init() {
        super.init()
    }

    fun setType(type: SearchStokcInfoEnum?) {
           ts = type
    }



    fun getData(type: SearchStokcInfoEnum?,str:String) {
        listhot.clear()
        history.clear()
        list.clear()
        getTopicStockData(str, 5)
    }


    fun getAdapter(): SeachAllofInfoAdapter? {
        if (viewModel?.adapter?.value == null) {
            viewModel?.adapter?.value = SeachAllofInfoAdapter(context)
        }
        return viewModel?.adapter?.value
    }

    fun getStockAdapter(): StockAdapter? {
        if (viewModel?.stockadapter?.value == null) {
            viewModel?.stockadapter?.value = StockAdapter()
        }
        return viewModel?.stockadapter?.value
    }

    fun getStockInfoAdapter(): StockInfoAdapter? {
        if (viewModel?.infoadapter?.value == null) {
            viewModel?.infoadapter?.value = StockInfoAdapter()
        }
        return viewModel?.infoadapter?.value
    }

    fun getStockData(str:String){
        listhot.clear()
        getTopicStockData(str,20)
    }
    fun getStockInfoData(){
        history.clear()
        for (i in 0..4) {
            history.add(i)
        }
        viewModel?.infoadapter?.value?.clearItems()
        if (viewModel?.infoadapter?.value?.items == null) {
            viewModel?.infoadapter?.value?.items = ArrayList()
        }
        viewModel?.infoadapter?.value?.addItems(history)
        LogUtils.e("tttttt---info----" + viewModel?.infoadapter?.value?.items?.size.toString())
    }

    fun getTopicStockData(keyWord: String, count: Int) {
        val requset = StockSearchRequest(keyWord, 0, count, transactions.createTransaction())
        Cache[IStockNet::class.java]?.search(requset)
            ?.enqueue(Network.IHCallBack<StockSearchResponse>(requset))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockSearchResponse(response: StockSearchResponse) {
        val datas = response.data?.datas
        if (datas.isNullOrEmpty()) return
        when((response.request as StockSearchRequest).pageSize){
              5->{
                history.clear()
                list.clear()
                for (i in 0..4) {
                    history.add(i)
                }
                var data = SearchDeafaultData(datas, history)
                list.add(data)
                list.add(data)
                viewModel?.adapter?.value?.clearItems()
                if (viewModel?.adapter?.value?.items == null) {
                    viewModel?.adapter?.value?.items = ArrayList()
                }
                viewModel?.adapter?.value?.addItems(list)
                LogUtils.e("tttttt-----all----" + viewModel?.adapter?.value?.items?.size.toString())
                LogUtils.e(viewModel?.adapter?.value?.items?.size.toString())
            }
            20->{
                viewModel?.stockadapter?.value?.clearItems()
                if (viewModel?.stockadapter?.value?.items == null) {
                    viewModel?.stockadapter?.value?.items = ArrayList()
                }
                viewModel?.stockadapter?.value?.addItems(datas)
                viewModel?.stockadapter?.value?.notifyDataSetChanged()
                LogUtils.e("tttttt-----stock----" + viewModel?.stockadapter?.value?.items?.size.toString())
            }

        }

    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSearchTypeEvent(event: TopicStockEvent) {
        if(ts==event.enum) {
            // 点击添加到自选列表
            if (LocalAccountConfig.read().isLogin()) {
                // 已登录
                val requset =
                    CollectionStockRequest(
                        event.info!!,
                        event.info.type!!,
                        event.info.ts!!,
                        event.info.code!!,
                        0,
                        transactions.createTransaction()
                    )
                Cache[IStockNet::class.java]?.collection(requset)
                    ?.enqueue(Network.IHCallBack<BaseResponse>(requset))
            } else {
                // 未登录
                event.info?.let { AddTopicStockEvent(it) }?.let {
                    RxBus.getDefault().post(it)
                    toast(R.string.add_topic_successful)
                }
            }
        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSearchTabChangeEvent(event: SelectsSearchTabEvent) {
        if(ts==event.enum) {
            when (event.enum) {
                SearchStokcInfoEnum.All -> {
                    view?.detailInfo(event.str)
                }
                SearchStokcInfoEnum.Stock -> {
                    view?.detailStock(event.str)
                }
                SearchStokcInfoEnum.Info -> {
                    view?.detailStockInfo(event.str)
                }
            }
        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSearchTabPositionEvent(event: TabPositionEvent) {
        when (event.pos) {
                0 -> {
                    setType(SearchStokcInfoEnum.All)
                    view?.initonlazy()
                }
                1 -> {
                    setType(SearchStokcInfoEnum.Stock)
                    view?.initonlazy()
                }
               2 -> {
                   setType(SearchStokcInfoEnum.Info)
                   view?.initonlazy()

                }
            }

    }

    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
    }



}