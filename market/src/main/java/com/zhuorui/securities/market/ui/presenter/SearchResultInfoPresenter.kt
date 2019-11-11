package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.commonwidget.ScreenCentralStateToast
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
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
    var preListStock = ArrayList<SearchStockInfo>()
    var history = ArrayList<Int>()
    var totalPage: Int =0
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
                    if(ts==SearchStokcInfoEnum.Stock) {
                        view?.addStockToAdapter(t, totalPage)
                    }
                })
            viewModel?.infos?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addInfoToAdapter(t,totalPage)
                })
        }
    }

    fun getData(type: SearchStokcInfoEnum?, str: String) {
        listhot.clear()
        history.clear()
        list.clear()
        getTopicStockData(str, 0,5)
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

    fun getStockData(str: String,currentPage:Int) {
        listhot.clear()
        getTopicStockData(str, currentPage,20)
    }

    fun getStockInfoData() {
        history.clear()
        for (i in 0..4) {
            history.add(i)
        }
        viewModel?.infos?.value = history

    }

    fun getTopicStockData(keyWord: String,currentPage:Int, count: Int) {
        val requset = StockSearchRequest(keyWord, count, transactions.createTransaction())
        Cache[IStockNet::class.java]?.search(requset)
            ?.enqueue(Network.IHCallBack<StockSearchResponse>(requset))
    }

    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onStockSearchResponse(response: StockSearchResponse) {
        if (!transactions.isMyTransaction(response)) return
  /*      if (response.data == null&&response.code=="000000"){
            val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                view?.showEmpty()
                emitter.onNext(true)
                emitter.onComplete()
            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe()
            disposables.add(disposable)
            return
        }*/
        val datas = response.data
       // totalPage=response.data.totalPage
        if (datas.isNullOrEmpty()){
            val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                view?.showEmpty()
                emitter.onNext(true)
                emitter.onComplete()
            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe()
            disposables.add(disposable)
            return
        }
        val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
            view?.hideEmpty()
            emitter.onNext(true)
            emitter.onComplete()
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe()
        disposables.add(disposable)
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
                var data:SearchDeafaultData
                history.clear()
                list.clear()
                for (i in 0..4) {
                    history.add(i)
                }
                data = if(datas.size>5){
                    preListStock.clear()
                    for (index in 0 until 5){
                        preListStock.add(datas[index])
                    }
                    SearchDeafaultData(preListStock, history)
                }else{
                    SearchDeafaultData(datas, history)
                }
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

    fun collectionStock(stockInfo: SearchStockInfo, isCollected: Boolean) {
        // 点击添加到自选列表
        if (LocalAccountConfig.read().isLogin()) {
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
        view?.showError()
    }

    override fun onBaseResponse(response: BaseResponse) {
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

    fun setAdapterDataNotify(stockInfo: SearchStockInfo,collect:Boolean){
        when(ts){
            SearchStokcInfoEnum.All -> {
              viewModel?.searchInfoDatas?.value.let {
                  if(it!=null){
                      for(data in it[0].hotlist){
                          if(stockInfo.code==data?.code){
                              data?.collect=collect
                          }
                      }
                  }
              }
                view?.notifyAdapter()
            }
            SearchStokcInfoEnum.Stock -> {
               viewModel?.stockdatas?.value.let {
                   if (it != null) {
                       for(data in it){
                           if(stockInfo.code==data?.code){
                               data?.collect=collect
                           }
                       }
                   }
               }
                view?.notifyAdapter()
            }
        }
    }

}