package com.zhuorui.securities.market.ui.presenter

import android.graphics.Color
import com.zhuorui.commonwidget.ScreenCentralStateToast
import com.zhuorui.commonwidget.config.LocalSettingsConfig
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.config.LocalStocksConfig
import com.zhuorui.securities.market.event.AddTopicStockEvent
import com.zhuorui.securities.market.event.DeleteTopicStockEvent
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.CollectionStockRequest
import com.zhuorui.securities.market.net.request.DeleteStockRequest
import com.zhuorui.securities.market.ui.view.MarketDetailView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailViewModel
import com.zhuorui.securities.personal.config.LocalAccountConfig

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-11 16:10
 *    desc   :
 */
class MarketDetailPresenter : AbsNetPresenter<MarketDetailView, MarketDetailViewModel>() {

    private var topBarInfoTp = 0 // 0 状态，1 价格
    private var isCollected:Boolean = false

    /**
     * 获取topbar 显示股票价格信息
     * */
    fun getTopBarPriceInfo() {
        topBarInfoTp = 1
        view?.upTopBarInfo(
            String.format("%+.3f %+.3f %+.2f%%", 123.43f, -23f, 23.45f),
            LocalSettingsConfig.read().getUpDownColor(123.0f, 3455.0f)
        )
    }

    /**
     * 获取topbar 显示股票状态信息
     * */
    fun getTopBarStockStatusInfo() {
        topBarInfoTp = 0
        view?.upTopBarInfo("交易中  08-30  09:45:19", Color.parseColor("#ffA4B2CB"))
    }

    fun getTopBarOnfoType(): Int {
        return topBarInfoTp
    }

    /**
     * 添加删除自选到自选
     */
    fun collectionStock(stockInfo: SearchStockInfo) {
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
            isCollected = !isCollected
            // 未登录
            if (isCollected) {
                // 传递删除自选股事件
                RxBus.getDefault().post(DeleteTopicStockEvent(stockInfo.ts!!, stockInfo.code!!))
                ScreenCentralStateToast.show(ResUtil.getString(R.string.delete_successful))
            } else {
                // 传递添加自选股事件
                RxBus.getDefault().post(AddTopicStockEvent(stockInfo))
                ScreenCentralStateToast.show(ResUtil.getString(R.string.add_topic_successful))
            }
            view?.upFollow(isCollected)
        }
    }


    override fun onBaseResponse(response: BaseResponse) {
        super.onBaseResponse(response)
        if (response.request is CollectionStockRequest) {
            // 传递添加自选股事件
            RxBus.getDefault().post(AddTopicStockEvent((response.request as CollectionStockRequest).stockInfo))
            //updateCurrentFragmentData(str)
            ScreenCentralStateToast.show(ResUtil.getString(R.string.add_topic_successful))
            isCollected = true
            view?.upFollow(true)
        } else if (response.request is DeleteStockRequest) {
            val request = response.request as DeleteStockRequest
            // 传递删除自选股事件
            RxBus.getDefault().post(DeleteTopicStockEvent(request.ts!!, request.code!!))
            ScreenCentralStateToast.show(ResUtil.getString(R.string.delete_successful))
            isCollected = false
            view?.upFollow(false)
        }
    }

    fun getData(stockInfo: SearchStockInfo) {
        isCollected = false
        val localStocks = LocalStocksConfig.getInstance().getStocks()
        if (localStocks.isNotEmpty()) {
            for (stock in localStocks) {
                if (stock.ts.equals(stockInfo.ts) && stock.code.equals(stockInfo.code)) {
                    isCollected = true
                    break
                }
            }
        }
        view?.upFollow(isCollected)
        val datas = mutableListOf<Int>()
        for (i: Int in 1..10) {
            datas.add(i)
        }
        view?.upBuyingSellingFilesData(7458f, 2442f, datas, datas)
        val datas2 = mutableListOf<String>()
        for (i: Int in 1..30) {
            datas2.add("item$i")
        }
        view?.upOrderBrokerData(datas2, datas2)
    }


}