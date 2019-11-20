package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.StockConsInfoRequest
import com.zhuorui.securities.market.net.request.StockSearchRequest
import com.zhuorui.securities.market.net.response.StockConsInfoResponse
import com.zhuorui.securities.market.net.response.StockSearchResponse
import com.zhuorui.securities.market.ui.adapter.MarketPartInfoAdapter
import com.zhuorui.securities.market.ui.adapter.MarketPointConsInfoAdapter
import com.zhuorui.securities.market.ui.adapter.MarketPointInfoAdapter
import com.zhuorui.securities.market.ui.view.MarketPointView
import com.zhuorui.securities.market.ui.viewmodel.MarketPointViewModel
import com.zhuorui.securities.market.util.MarketUtil

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/22
 * Desc:
 */
class MarketPointPresenter :AbsNetPresenter<MarketPointView,MarketPointViewModel>(){
    override fun init() {
        super.init()
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {

    }



    fun getMarketInfoAdapter(): MarketPointConsInfoAdapter {
        return MarketPointConsInfoAdapter()
    }


    fun getMarketPointInfoAdapter():MarketPointInfoAdapter{
        return  MarketPointInfoAdapter()
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


}