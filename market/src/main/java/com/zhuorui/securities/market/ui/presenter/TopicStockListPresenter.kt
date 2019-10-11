package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.commonwidget.ScreenCentralStateToast
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.network.BaseResponse
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
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.model.StockTsEnum
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.DeleteStockRequest
import com.zhuorui.securities.market.net.request.RecommendStocklistRequest
import com.zhuorui.securities.market.net.request.StickyOnTopStockRequest
import com.zhuorui.securities.market.net.request.SynStockRequest
import com.zhuorui.securities.market.net.response.RecommendStocklistResponse
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicPriceResponse
import com.zhuorui.securities.market.ui.view.TopicStockListView
import com.zhuorui.securities.market.ui.viewmodel.TopicStockListViewModel
import com.zhuorui.securities.market.util.MathUtil
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.event.LoginStateChangeEvent
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/19 17:13
 *    desc   :
 */
@Suppress("NAME_SHADOWING")
class TopicStockListPresenter : AbsNetPresenter<TopicStockListView, TopicStockListViewModel>() {

    private var ts: StockTsEnum? = null
    private val disposables = LinkedList<Disposable>()

    override fun init() {
        super.init()
        view?.init()
    }

    fun setType(type: StockTsEnum?) {
        ts = type
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.datas?.observe(it,
                androidx.lifecycle.Observer<List<StockMarketInfo>> { t ->
                    RxBus.getDefault().post(NotifyStockCountEvent(ts, t.size))
                    view?.notifyDataSetChanged(t)
                })
        }
    }

    /**
     * 加载推荐自选股列表
     */
    fun requestStocks(currentPage: Int, pageSize: Int) {
        val request = RecommendStocklistRequest(
            if (ts == StockTsEnum.HS) StockTsEnum.SH.name + "," + StockTsEnum.SZ.name else ts?.name,
            currentPage,
            pageSize,
            transactions.createTransaction()
        )

        val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
            emitter.onNext(LocalAccountConfig.read().isLogin())
            emitter.onComplete()
        }).subscribeOn(Schedulers.io())
            .subscribe { isLogin ->
                if (isLogin) {
                    // 已登录
                    Cache[IStockNet::class.java]?.myList(request)
                        ?.enqueue(Network.IHCallBack<RecommendStocklistResponse>(request))
                } else {
                    // 未登录
                    // 先查看是否有缓存
                    if (!LocalStocksConfig.hasCache()) {
                        // 本地无缓存，拉取网络数据
                        Cache[IStockNet::class.java]?.list(request)
                            ?.enqueue(Network.IHCallBack<RecommendStocklistResponse>(request))
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
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    // 发起订阅
                                    topicPrice(it)
                                    // 更新界面数据
                                    viewModel?.datas?.value = it
                                }
                        disposables.add(disposable)
                    }
                }
            }
        disposables.add(disposable)
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onRecommendStocklistResponse(response: RecommendStocklistResponse) {
        if (!transactions.isMyTransaction(response)) return
        val datas = response.data?.datas
        if (datas.isNullOrEmpty()) return
        viewModel?.datas?.value = datas

        // 订阅价格
        var disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
            emitter.onNext(topicPrice(datas))
            emitter.onComplete()
        }).subscribeOn(Schedulers.io())
            .subscribe()
        disposables.add(disposable)

        if (ts == null) {
            // 保存本地数据
            disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                emitter.onNext(LocalStocksConfig.getInstance().replaceAll(datas))
                emitter.onComplete()
            }).subscribeOn(Schedulers.io())
                .subscribe()
            disposables.add(disposable)
        }
    }

    /**
     *  发起价格订阅
     */
    private fun topicPrice(datas: MutableList<StockMarketInfo>): Boolean {
        for (item in datas) {
            val stockTopic = StockTopic(StockTopicDataTypeEnum.price, item.ts!!, item.code!!, item.type!!)
            SocketClient.getInstance().bindTopic(stockTopic)
        }
        return true
    }

    @RxSubscribe(observeOnThread = EventThread.IO)
    fun onStocksTopicPriceResponse(response: StocksTopicPriceResponse) {

        val datas = viewModel?.datas?.value
        if (datas.isNullOrEmpty()) return

        val stockPriceDatas = response.body

        for (index in datas.indices) {
            val item = datas[index]
            for (sub in stockPriceDatas) {
                if (item.ts == sub.ts && item.code == sub.code) {
                    // 更新数据
                    val item = datas[index]
                    item.price = sub.price!!
                    item.diffPrice = sub.price!!.subtract(sub.openPrice)
                    item.diffRate = MathUtil.divide2(
                        item.diffPrice!!.multiply(BigDecimal.valueOf(100)),
                        sub.openPrice!!
                    )
                    view?.notifyItemChanged(index)
                    break
                }
            }
        }
        // 保存本地数据
        LocalStocksConfig.getInstance().replaceAll(datas)
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
            var datas = viewModel?.datas?.value
            if (datas == null) {
                datas = ArrayList()
            }

            for (item in datas) {
                if (item.ts.equals(stockTs) && item.code.equals(stockCode)) return
            }

            // 显示新添加的自选股
            val stock = StockMarketInfo()
            stock.id = event.stock.id
            stock.ts = stockTs
            stock.code = event.stock.code
            stock.name = event.stock.name
            stock.type = event.stock.type
            stock.tsCode = event.stock.tsCode
            // TODO 添加到顶部
            datas.add(0, stock)
            if (viewModel?.datas?.value.isNullOrEmpty()) {
                val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                    viewModel?.datas?.value = datas
                    emitter.onNext(true)
                    emitter.onComplete()
                }).subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe()
                disposables.add(disposable)
            } else {
                view?.notifyDataSetChanged(datas)
            }
            // 发起订阅价格
            val stockTopic = StockTopic(StockTopicDataTypeEnum.price, stockTs!!, stock.code!!, stock.type!!)
            SocketClient.getInstance().bindTopic(stockTopic)
            // 刷新股票个数
            RxBus.getDefault().post(NotifyStockCountEvent(ts, datas.size))
            // 保存本地数据
            LocalStocksConfig.getInstance().add(stock)
        }
    }

    /**
     * 置顶自选股
     */
    fun onStickyOnTop(item: StockMarketInfo?) {
        // 判断是否登录
        if (LocalAccountConfig.read().isLogin()) {
            val request = StickyOnTopStockRequest(item?.id!!, transactions.createTransaction())
            Cache[IStockNet::class.java]?.stickyOnTop(request)
                ?.enqueue(Network.IHCallBack<BaseResponse>(request))
        } else {
            stickyOnTop(item)
        }
    }

    private fun stickyOnTop(item: StockMarketInfo?) {
        // 更换自选股位置
        val datas = viewModel?.datas?.value ?: return
        datas.remove(item)
        item?.let { datas.add(0, it) }
        // 修改本地缓存
        val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
            emitter.onNext(LocalStocksConfig.getInstance().replaceAll(datas))
            emitter.onComplete()
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe()
        disposables.add(disposable)
        // 刷新界面
        view?.notifyDataSetChanged(datas)
        // 提示置顶成功
        ScreenCentralStateToast.show(ResUtil.getString(R.string.sticky_on_top_successful))
    }

    fun onDeleteStock(item: StockMarketInfo?) {
        // 判断是否登录
        if (LocalAccountConfig.read().isLogin()) {
            val ids = arrayOf(item?.id)
            val request = DeleteStockRequest(ids, item?.ts!!, item.code!!, transactions.createTransaction())
            Cache[IStockNet::class.java]?.delelte(request)
                ?.enqueue(Network.IHCallBack<BaseResponse>(request))
        } else {
            // 传递删除自选股事件
            RxBus.getDefault().post(DeleteTopicStockEvent(item?.ts!!, item.code!!))
        }
    }

    @RxSubscribe(observeOnThread = EventThread.IO)
    fun onDeleteTopicStockEvent(event: DeleteTopicStockEvent) {
        val datas = viewModel?.datas?.value ?: return
        val stockTs = event.ts
        if (ts == null || stockTs == ts?.name || (ts == StockTsEnum.HS && (stockTs == StockTsEnum.SH.name || stockTs == StockTsEnum.SZ.name))) {
            for (element in datas) {
                if (element.ts.equals(event.ts) && element.code.equals(event.code)) {
                    datas.remove(element)
                    // 取消订阅
                    val stockTopic = StockTopic(
                        StockTopicDataTypeEnum.price, element.ts!!, element.code!!,
                        element.type!!
                    )
                    SocketClient.getInstance().unBindTopic(stockTopic)
                    break
                }
            }
            // 刷新界面
            view?.notifyDataSetChanged(datas)
            // 删除本地缓存
            LocalStocksConfig.getInstance().remove(event.ts, event.code)
            // 更新最新自选股数目
            RxBus.getDefault().post(NotifyStockCountEvent(ts, datas.size))
            // 提示删除成功
            val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                ScreenCentralStateToast.show(ResUtil.getString(R.string.delete_successful))
                emitter.onNext(true)
                emitter.onComplete()
            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe()
            disposables.add(disposable)
        }
    }

    override fun onBaseResponse(response: BaseResponse) {
        super.onBaseResponse(response)
        if (response.request is DeleteStockRequest) {
            val request = response.request as DeleteStockRequest
            // 传递删除自选股事件
            RxBus.getDefault().post(DeleteTopicStockEvent(request.ts!!, request.code!!))
        } else if (response.request is StickyOnTopStockRequest) {
            val id = (response.request as StickyOnTopStockRequest).id
            for (it in viewModel?.datas?.value!!) {
                if (it.id.equals(id)) {
                    stickyOnTop(it)
                    break
                }
            }
        } else if (response.request is SynStockRequest) {
            // 同步完成，重新拉取自选股列表
            view?.requestStocks()
        }
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
                // 同步缓存中的自选股
                val datas = LocalStocksConfig.getInstance().getStocks()
                if (datas.isNullOrEmpty()) return
                val request = SynStockRequest(datas, transactions.createTransaction())
                Cache[IStockNet::class.java]?.synStock(request)
                    ?.enqueue(Network.IHCallBack<BaseResponse>(request))
            }
        }
        // 未登录
        else {
            // 清空所有的缓存
            LocalStocksConfig.clear()
            // 重新拉取未登录状态的自选股列表
            view?.requestStocks()
        }
    }

    /**
     * 长链接连接状态发生改变
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onSocketAuthCompleteEvent(event: SocketAuthCompleteEvent) {
        viewModel?.datas?.value?.let { topicPrice(it) }
    }

    override fun destroy() {
        super.destroy()

        // 释放disposable
        if (disposables.isNullOrEmpty()) return
        for (disposable in disposables) {
            disposable.dispose()
        }
        disposables.clear()
    }
}