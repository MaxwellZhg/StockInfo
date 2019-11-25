package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.commonwidget.ScreenCentralStateToast
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.infra.LogInfra
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
import com.zhuorui.securities.market.event.AddTopicStockEvent
import com.zhuorui.securities.market.event.DeleteTopicStockEvent
import com.zhuorui.securities.market.event.NotifyStockCountEvent
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.StockTsEnum
import com.zhuorui.securities.market.model.TopicStockModel
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.DeleteStockRequest
import com.zhuorui.securities.market.net.request.RecommendStocklistRequest
import com.zhuorui.securities.market.net.request.StickyOnTopStockRequest
import com.zhuorui.securities.market.net.request.SynStockRequest
import com.zhuorui.securities.market.net.response.RecommendStocklistResponse
import com.zhuorui.securities.market.net.response.SynStockResponse
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.ui.view.TopicStockListView
import com.zhuorui.securities.market.ui.viewmodel.TopicStockListViewModel
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.event.LoginStateChangeEvent
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/19 17:13
 *    desc   :
 */
@Suppress("NAME_SHADOWING")
class TopicStockListPresenter : AbsNetPresenter<TopicStockListView, TopicStockListViewModel>(),
    TopicStockModel.OnChangeDataCallBack {

    private var ts: StockTsEnum? = null
    private val disposables = LinkedList<Disposable>()

    override fun init() {
        super.init()
        view?.init()
        requestStocks()
    }

    fun setType(type: StockTsEnum?) {
        ts = type
    }

    /**
     * 加载推荐自选股列表
     */
    private fun requestStocks() {
        val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
            emitter.onNext(LocalAccountConfig.getInstance().isLogin())
            emitter.onComplete()
        }).subscribeOn(Schedulers.io())
            .subscribe { isLogin ->
                // 先查看是否有缓存
                if (!LocalStocksConfig.hasCache()) {
                    // 无缓存
                    view?.notifyDataSetChanged(viewModel?.datas?.value)
                    // 加载网络数据
                    if (isLogin) {
                        loadStocklist()
                    }
                } else {
                    // 本地有缓存，读取本地缓存的自选股
                    val disposable =
                        Observable.create(ObservableOnSubscribe<MutableList<StockMarketInfo>> { emitter ->
                            when (ts) {
                                null -> emitter.onNext(LocalStocksConfig.getInstance().getStocks())
                                StockTsEnum.HK -> emitter.onNext(
                                    LocalStocksConfig.getInstance().getStocks(
                                        StockTsEnum.HK.name
                                    )
                                )
                                else -> emitter.onNext(
                                    LocalStocksConfig.getInstance().getStocks(
                                        StockTsEnum.SH.name,
                                        StockTsEnum.SZ.name
                                    )
                                )
                            }
                            emitter.onComplete()
                        }).subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe {
                                // 更新界面数据
                                viewModel?.datas?.value?.forEach { item ->
                                    // 取消订阅股价
                                    item.setOnChangeDataCallBack(null)
                                }
                                viewModel?.datas?.value?.clear()
                                val tempList = ArrayList<TopicStockModel>()
                                it.forEach { item ->
                                    val model = TopicStockModel()
                                    model.stockInfo = item
                                    // 订阅股价
                                    model.setOnChangeDataCallBack(this)
                                    tempList.add(model)
                                }
                                viewModel?.datas?.value?.addAll(tempList)
                                RxBus.getDefault()
                                    .post(NotifyStockCountEvent(ts, if (it.isNullOrEmpty()) 0 else it.size))
                                view?.notifyDataSetChanged(viewModel?.datas?.value)
                                // 加载网络数据
                                if (isLogin) {
                                    loadStocklist()
                                }
                            }
                    disposables.add(disposable)
                }
            }
        disposables.add(disposable)
    }

    fun loadStocklist() {
        val request = RecommendStocklistRequest(
            if (ts == StockTsEnum.HS) StockTsEnum.SH.name + "," + StockTsEnum.SZ.name else ts?.name,
            transactions.createTransaction()
        )
        Cache[IStockNet::class.java]?.myList(request)
            ?.enqueue(Network.IHCallBack<RecommendStocklistResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onRecommendStocklistResponse(response: RecommendStocklistResponse) {
        if (!transactions.isMyTransaction(response)) return
        view?.showRetry(false)
        val datas = response.data
        if (datas.isNullOrEmpty()) return
        viewModel?.datas?.value?.forEach { item ->
            // 取消订阅股价
            item.setOnChangeDataCallBack(null)
        }
        // 刷新数据时要清掉老数据
        viewModel?.datas?.value?.clear()
        val tempList = ArrayList<TopicStockModel>()
        datas.forEach { item ->
            val model = TopicStockModel()
            model.stockInfo = item
            // 订阅股价
            model.setOnChangeDataCallBack(this)
            tempList.add(model)
        }
        viewModel?.datas?.value?.addAll(tempList)
        RxBus.getDefault().post(NotifyStockCountEvent(ts, if (datas.isNullOrEmpty()) 0 else datas.size))
        view?.notifyDataSetChanged(viewModel?.datas?.value)
        if (ts == null) {
            // 保存本地数据
            val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                emitter.onNext(LocalStocksConfig.getInstance().update(datas))
                emitter.onComplete()
            }).subscribeOn(Schedulers.io())
                .subscribe()
            disposables.add(disposable)
        }
    }

    override fun onPriceChange(stockInfo: StockMarketInfo, position: Int) {
        // 更新界面
        view?.notifyItemChanged(position)
        // 保存本地数据
        LocalStocksConfig.getInstance().update(stockInfo)
    }

    /**
     * 添加自选股
     */
    @RxSubscribe(observeOnThread = EventThread.IO)
    fun onAddTopicStockEvent(event: AddTopicStockEvent) {
        LogInfra.Log.d(TAG, "onAddTopicStockEvent ...")
        val stockTs = event.stock.ts
        val stockCode = event.stock.code
        if (ts == null || stockTs.equals(ts?.name) || (ts == StockTsEnum.HS && (stockTs.equals(StockTsEnum.SH.name) || stockTs.equals(
                StockTsEnum.SZ.name
            )))
        ) {
            val datas = viewModel?.datas?.value
            for (item in datas!!) {
                if (item.stockInfo?.ts.equals(stockTs) && item.stockInfo?.code.equals(stockCode)) return
            }

            // 显示新添加的自选股
            val item = TopicStockModel()
            val stockInfo = StockMarketInfo()
            stockInfo.ts = stockTs
            stockInfo.code = event.stock.code
            stockInfo.name = event.stock.name
            stockInfo.type = event.stock.type
            stockInfo.tsCode = event.stock.tsCode
            item.stockInfo = stockInfo
            item.setOnChangeDataCallBack(this)
            // 添加到顶部
            datas.add(0, item)
            view?.notifyDataSetChanged(datas)
            // 刷新股票个数
            RxBus.getDefault().post(NotifyStockCountEvent(ts, datas.size))
            // 保存本地数据
            LocalStocksConfig.getInstance().add(stockInfo)
        }
    }

    /**
     * 置顶自选股
     */
    fun onStickyOnTop(item: TopicStockModel?) {
        // 判断是否登录
        if (LocalAccountConfig.getInstance().isLogin()) {
            val request = StickyOnTopStockRequest(item?.stockInfo?.id!!, transactions.createTransaction())
            Cache[IStockNet::class.java]?.stickyOnTop(request)
                ?.enqueue(Network.IHCallBack<BaseResponse>(request))
        } else {
            stickyOnTop(item)
        }
    }

    private fun stickyOnTop(item: TopicStockModel?) {
        // 更换自选股位置
        val datas = viewModel?.datas?.value ?: return
        datas.remove(item)
        item?.let { datas.add(0, it) }
        // TODO 修改本地缓存记录的顺序暂时不处理
        // 刷新界面
        view?.notifyDataSetChanged(datas)
        // 提示置顶成功
        ScreenCentralStateToast.show(ResUtil.getString(R.string.sticky_on_top_successful))
    }

    fun onDeleteStock(item: TopicStockModel?) {
        // 取消订阅股价
        item?.setOnChangeDataCallBack(null)
        // 判断是否登录
        if (LocalAccountConfig.getInstance().isLogin()) {
            val request =
                DeleteStockRequest(transactions.createTransaction(), item?.stockInfo?.ts!!, item?.stockInfo?.code!!)
            Cache[IStockNet::class.java]?.delelte(request)
                ?.enqueue(Network.IHCallBack<BaseResponse>(request))
        } else {
            // 传递删除自选股事件
            RxBus.getDefault().post(DeleteTopicStockEvent(item?.stockInfo?.ts!!, item?.stockInfo?.code!!))
            ScreenCentralStateToast.show(ResUtil.getString(R.string.delete_successful))
        }
    }

    @RxSubscribe(observeOnThread = EventThread.IO)
    fun onDeleteTopicStockEvent(event: DeleteTopicStockEvent) {
        val datas = viewModel?.datas?.value ?: return
        val stockTs = event.ts
        if (ts == null || stockTs == ts?.name || (ts == StockTsEnum.HS && (stockTs == StockTsEnum.SH.name || stockTs == StockTsEnum.SZ.name))) {
            for (element in datas) {
                if (element.stockInfo?.ts.equals(event.ts) && element.stockInfo?.code.equals(event.code)) {
                    element.setOnChangeDataCallBack(null)
                    datas.remove(element)
                    break
                }
            }
            // 刷新界面
            view?.notifyDataSetChanged(datas)
            // 删除本地缓存
            LocalStocksConfig.getInstance().remove(event.ts, event.code)
            // 更新最新自选股数目
            RxBus.getDefault().post(NotifyStockCountEvent(ts, datas.size))
        }
    }

    override fun onBaseResponse(response: BaseResponse) {
        super.onBaseResponse(response)
        if (response.request is DeleteStockRequest) {
            val request = response.request as DeleteStockRequest
            // 传递删除自选股事件
            RxBus.getDefault().post(DeleteTopicStockEvent(request.ts!!, request.codes[0]!!))
            ScreenCentralStateToast.show(ResUtil.getString(R.string.delete_successful))
        } else if (response.request is StickyOnTopStockRequest) {
            val id = (response.request as StickyOnTopStockRequest).id
            for (it in viewModel?.datas?.value!!) {
                if (it.stockInfo?.id.equals(id)) {
                    stickyOnTop(it)
                    break
                }
            }
        }
    }

    override fun onErrorResponse(response: ErrorResponse) {
        if (response.request is RecommendStocklistRequest) {
            view?.showRetry(true)
            return
        }
        super.onErrorResponse(response)
    }

    /**
     * 同步自选股完成
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onSynStockResponse(response: SynStockResponse) {
        // 同步完成，重新拉取自选股列表
        loadStocklist()
    }

    /**
     * 登录状态发生改变
     */
    @RxSubscribe(observeOnThread = EventThread.IO)
    fun onLoginStateChangeEvent(event: LoginStateChangeEvent) {
        // 已登录
        if (event.isLogin) {
            if (ts == null) {
                view?.hideRegisterNow()
                val datas = LocalStocksConfig.getInstance().getStocks()
                if (datas.isNullOrEmpty()) {
                    // 无缓存，无需同步
                    RxBus.getDefault().post(SynStockResponse())
                    return
                }
                // 同步缓存中的自选股
                val request = SynStockRequest(datas, transactions.createTransaction())
                Cache[IStockNet::class.java]?.synStock(request)
                    ?.enqueue(Network.IHCallBack<SynStockResponse>(request))
            }
        }
        // 未登录
        else {
            // 取消所有订阅
            if (ts == null) {
                SocketClient.getInstance().unBindAllTopic()
            }
            // 清空所有的缓存
            LocalStocksConfig.getInstance().clear()
            viewModel?.datas?.value?.clear()
            view?.notifyDataSetChanged(viewModel?.datas?.value)
        }
    }


    override fun destroy() {
        super.destroy()

        viewModel?.datas?.value?.forEach { it.setOnChangeDataCallBack(null) }

        // 释放disposable
        if (disposables.isNullOrEmpty()) return
        for (disposable in disposables) {
            disposable.dispose()
        }
        disposables.clear()
    }
}