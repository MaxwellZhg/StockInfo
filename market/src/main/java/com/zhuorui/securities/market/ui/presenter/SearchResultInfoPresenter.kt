package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.event.AddTopicStockEvent
import com.zhuorui.securities.market.event.SelectsSearchTabEvent
import com.zhuorui.securities.market.event.TabPositionEvent
import com.zhuorui.securities.market.event.TopicStockEvent
import com.zhuorui.securities.market.model.SearchDeafaultData
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.SearchStokcInfoEnum
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.CollectionStockRequest
import com.zhuorui.securities.market.net.request.StockSearchRequest
import com.zhuorui.securities.market.net.response.StockSearchResponse
import com.zhuorui.securities.market.ui.adapter.SeachAllofInfoAdapter
import com.zhuorui.securities.market.ui.adapter.StockAdapter
import com.zhuorui.securities.market.ui.adapter.StockInfoAdapter
import com.zhuorui.securities.market.ui.view.SearchResultInfoView
import com.zhuorui.securities.market.ui.viewmodel.SearchResultInfoViewModel
import com.zhuorui.securities.personal.config.LocalAccountConfig

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/20
 * Desc:
 */
class SearchResultInfoPresenter : AbsNetPresenter<SearchResultInfoView, SearchResultInfoViewModel>() {
    var ts: SearchStokcInfoEnum? = null
    var str: String? = null
    var list = ArrayList<SearchDeafaultData>()
    var listhot = ArrayList<SearchStockInfo>()
    var history = ArrayList<Int>()
    override fun init() {
        super.init()
    }

    fun setType(type: SearchStokcInfoEnum?) {
        ts = type
    }


    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.searchInfoDatas?.observe(it,
                androidx.lifecycle.Observer<List<SearchDeafaultData>> { t ->
                    view?.addAllToAdapter(t)
                })
            viewModel?.stockdatas?.observe(it,
                androidx.lifecycle.Observer<List<SearchStockInfo>> { t ->
                    view?.addStockToAdapter(t)
                })
            viewModel?.infos?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addInfoToAdapter(t)
                })
        }
    }

    fun getData(type: SearchStokcInfoEnum?, str: String) {
        listhot.clear()
        history.clear()
        list.clear()
        getTopicStockData(str, 5)
    }


    fun getAdapter(): SeachAllofInfoAdapter? {
        return SeachAllofInfoAdapter(context)
    }

    fun getStockAdapter(): StockAdapter? {
        return StockAdapter()
    }

    fun getStockInfoAdapter(): StockInfoAdapter? {
        return StockInfoAdapter()
    }

    fun getStockData(str: String) {
        listhot.clear()
        getTopicStockData(str, 20)
    }

    fun getStockInfoData() {
        history.clear()
        for (i in 0..4) {
            history.add(i)
        }
        viewModel?.infos?.value = history

    }

    fun getTopicStockData(keyWord: String, count: Int) {
        val requset = StockSearchRequest(keyWord, 0, count, transactions.createTransaction())
        Cache[IStockNet::class.java]?.search(requset)
            ?.enqueue(Network.IHCallBack<StockSearchResponse>(requset))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockSearchResponse(response: StockSearchResponse) {
        if (!transactions.isMyTransaction(response)) return
        val datas = response.data.datas
        if (datas.isNullOrEmpty()) return
        when ((response.request as StockSearchRequest).pageSize) {
            5 -> {
                history.clear()
                list.clear()
                for (i in 0..4) {
                    history.add(i)
                }
                var data = SearchDeafaultData(datas, history)
                list.add(data)
                list.add(data)
                viewModel?.searchInfoDatas?.value = list
            }
            20 -> {
                datas.let {
                    viewModel?.stockdatas?.value = it as MutableList<SearchStockInfo>
                }
            }

        }

    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSearchTypeEvent(event: TopicStockEvent) {
        if (ts == event.enum) {
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
                RxBus.getDefault().post(AddTopicStockEvent(event.info!!))
                toast(R.string.add_topic_successful)
            }
        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSearchTabChangeEvent(event: SelectsSearchTabEvent) {
        if (ts == event.enum) {
            when (event.enum) {
                SearchStokcInfoEnum.All -> {
                    view?.detailInfo(event.str)
                    str = event.str
                }
                SearchStokcInfoEnum.Stock -> {
                    view?.detailStock(event.str)
                    str = event.str
                }
                SearchStokcInfoEnum.Info -> {
                    view?.detailStockInfo(event.str)
                    str = event.str
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

    override fun onBaseResponse(response: BaseResponse) {
        super.onBaseResponse(response)
        if (response.request is CollectionStockRequest) {
            RxBus.getDefault().post(AddTopicStockEvent((response.request as CollectionStockRequest).stockInfo))
            toast(R.string.add_topic_successful)
            updateCurrentFragmentData(str)
        }
    }

    private fun updateCurrentFragmentData(str: String?) {
        when (ts) {
            SearchStokcInfoEnum.All -> {
                str?.let { view?.detailInfo(it) }
            }
            SearchStokcInfoEnum.Stock -> {
                str?.let { view?.detailStock(it) }
            }
            else -> {
            }
        }
    }
}