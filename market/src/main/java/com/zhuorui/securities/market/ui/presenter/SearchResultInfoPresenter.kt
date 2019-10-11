package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
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
import com.zhuorui.securities.market.model.SearchDeafaultData
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.SearchStokcInfoEnum
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.CollectionStockRequest
import com.zhuorui.securities.market.net.request.DeleteStockRequest
import com.zhuorui.securities.market.net.request.StockSearchRequest
import com.zhuorui.securities.market.net.response.StockSearchResponse
import com.zhuorui.securities.market.ui.adapter.SeachAllofInfoAdapter
import com.zhuorui.securities.market.ui.adapter.StockAdapter
import com.zhuorui.securities.market.ui.adapter.StockInfoAdapter
import com.zhuorui.securities.market.ui.view.SearchResultInfoView
import com.zhuorui.securities.market.ui.viewmodel.SearchResultInfoViewModel
import com.zhuorui.securities.personal.config.LocalAccountConfig
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlin.collections.ArrayList

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/20
 * Desc:
 */
class SearchResultInfoPresenter : AbsNetPresenter<SearchResultInfoView, SearchResultInfoViewModel>() {
    private val disposables = ArrayList<Disposable>()
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

    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onStockSearchResponse(response: StockSearchResponse) {
        if (!transactions.isMyTransaction(response)) return
        if (response.data == null) return
        val datas = response.data.datas
        if (datas.isNullOrEmpty()) return
        val localStocks = LocalStocksConfig.getInstance().getStocks()
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
        }
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
                val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                    viewModel?.searchInfoDatas?.value = list
                    emitter.onNext(true)
                    emitter.onComplete()
                }).subscribeOn(AndroidSchedulers.mainThread()).subscribe()
                disposables.add(disposable)
            }

            20 -> {
                datas.let {
                    val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                        viewModel?.stockdatas?.value = it as MutableList<SearchStockInfo>
                        emitter.onNext(true)
                        emitter.onComplete()
                    }).subscribeOn(AndroidSchedulers.mainThread()).subscribe()
                    disposables.add(disposable)
                }
            }

        }

    }

    fun collectionStock(stockInfo: SearchStockInfo, collect: Boolean) {
        // 点击添加到自选列表
        if (LocalAccountConfig.read().isLogin()) {
            // 已登录
            if (collect) {
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
            } else {
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
            }

        } else {
                // 未登录
                if (collect) {
                    // 传递删除自选股事件
                    RxBus.getDefault().post(DeleteTopicStockEvent(stockInfo.ts!!, stockInfo.code!!))
                    toast(R.string.delete_successful)
                } else {
                    // 传递添加自选股事件
                    RxBus.getDefault().post(AddTopicStockEvent(stockInfo))
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

    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
       }


        override fun onBaseResponse(response: BaseResponse) {
                super.onBaseResponse(response)
                if (response.request is CollectionStockRequest) {
                    RxBus.getDefault().post(AddTopicStockEvent((response.request as CollectionStockRequest).stockInfo))
                    toast(R.string.add_topic_successful)
                    (response.request as CollectionStockRequest).stockInfo.collect = true
                    updateCurrentFragmentData(str)
                } else if (response.request is DeleteStockRequest) {
                    val request = response.request as DeleteStockRequest
                    request.stockInfo?.collect = false
                    updateCurrentFragmentData(str)
                    // 传递删除自选股事件
                    RxBus.getDefault().post(DeleteTopicStockEvent(request.ts!!, request.code!!))
                }
            }

            fun updateCurrentFragmentData(str: String?) {
                when (ts) {
                    SearchStokcInfoEnum.All -> {
                        str?.let { view?.detailInfo(it) }
                    }
                    SearchStokcInfoEnum.Stock -> {
                        str?.let { view?.detailStock(it) }
                    }
                }
            }

            override fun destroy() {
                super.destroy()
                if (disposables.isNullOrEmpty()) return
                for (disposable in disposables) {
                    disposable.dispose()
                }
                disposables.clear()
            }

        }