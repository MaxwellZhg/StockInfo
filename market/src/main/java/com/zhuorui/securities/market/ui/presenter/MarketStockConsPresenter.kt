package com.zhuorui.securities.market.ui.presenter

import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import com.google.gson.reflect.TypeToken
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.JsonUtil
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.event.MarketPointConsEvent
import com.zhuorui.securities.market.event.SocketAuthCompleteEvent
import com.zhuorui.securities.market.event.StockConsEvent
import com.zhuorui.securities.market.event.StockConsStateEvent
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.StockConsInfoRequest
import com.zhuorui.securities.market.net.response.StockConsInfoResponse
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicHandicapResponse
import com.zhuorui.securities.market.socket.vo.IndexPonitHandicapData
import com.zhuorui.securities.market.socket.vo.StockHandicapData
import com.zhuorui.securities.market.ui.adapter.MarketPointConsInfoAdapter
import com.zhuorui.securities.market.ui.adapter.MarketPointInfoAdapter
import com.zhuorui.securities.market.ui.view.MarketStockConsView
import com.zhuorui.securities.market.ui.viewmodel.MarketStockConsViewModel
import com.zhuorui.securities.market.util.MathUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:
 */
class MarketStockConsPresenter :AbsNetPresenter<MarketStockConsView,MarketStockConsViewModel>(){
    private val disposables = LinkedList<Disposable>()
    override fun init() {
        super.init()
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.infos?.observe(it,
                androidx.lifecycle.Observer<List<StockConsInfoResponse.ListInfo>> { t ->
                    view?.addInfoToAdapter(t)
                })
        }
    }

    fun getMarketInfoAdapter(): MarketPointConsInfoAdapter {
        return MarketPointConsInfoAdapter()
    }



    /**
     * 获取topbar 显示股票状态信息
     * */
    fun getTopBarStockStatusInfo() {
        val h = Integer.valueOf(TimeZoneUtil.currentTime("HH"))
        var closingTimeMillis =
            if (h < 9 || h >= 16) TimeZoneUtil.currentTimeMillis() else 0
        /* view?.upTopBarInfo(
             MarketUtil.getStockStatusTxt(stocksInfo?.ts, closingTimeMillis, true),
             topbarTxtColor
         )*/
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockConsEventResponse(event: StockConsEvent) {
        if(event.list.isNotEmpty()) {
            // 刷新数据时要清掉老数据
            viewModel?.infos?.value?.clear()
            viewModel?.infos?.value = event.list
            // 订阅价格
            var disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                emitter.onNext(topicPrice(event.list as MutableList<StockConsInfoResponse.ListInfo>))
                emitter.onComplete()
            }).subscribeOn(Schedulers.computation())
                .subscribe()
            disposables.add(disposable)
        }
    }

    private fun topicPrice(list: MutableList<StockConsInfoResponse.ListInfo>): Boolean {
        for (item in list) {
            val stockTopic = StockTopic(StockTopicDataTypeEnum.HANDICAP, item.ts!!, item.code!!,2)
            SocketClient.getInstance().bindTopic(stockTopic)
        }
        return true
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockConsStateEventResponse(event: StockConsStateEvent) {
            view?.showStateInfo(event.state)
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStockConsErrorEventResponse(event: MarketPointConsEvent) {
        view?.showErrorState()
    }

    /**
     * 长链接连接状态发生改变
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onSocketAuthCompleteEvent(event: SocketAuthCompleteEvent) {
        viewModel?.infos?.value?.let {
            //使用克隆数据，防止在迭代时，数据发生改变而奔溃
            topicPrice(it.clone() as ArrayList<StockConsInfoResponse.ListInfo>)
        }
    }

    /**
     * 推送股票盘口数据回调
     */
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksListTopicHandicap(response: StocksTopicHandicapResponse) {
        val datas = viewModel?.infos?.value
        if (datas.isNullOrEmpty()) return
    /*    val listType = object : TypeToken<List<StockHandicapData>>() {}.type
        val datalist: List<StockHandicapData> = JsonUtil.fromJson(response.body.toString(), listType)*/
        val stockPriceDatas = response.body?.get(0)
       for (index in datas.indices) {
            val item = datas[index]
                if (item.ts == stockPriceDatas?.ts && item.code == stockPriceDatas?.code) {
                    // 更新数据
                    val item = datas[index]
                    item.lastPrice = stockPriceDatas?.last!!.toBigDecimal()
                    item.diffRate = stockPriceDatas.diffRate!!.toBigDecimal()
                    item.turnover = stockPriceDatas.turnover!!
                    view?.notifyItemChanged(index)
                    break
            }
        }
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