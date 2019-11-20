package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.PushStockPriceData
import com.zhuorui.securities.market.socket.vo.OrderData
import com.zhuorui.securities.market.socket.vo.StockHandicapData

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-11 15:59
 *    desc   :
 */
class MarketDetailViewModel : ViewModel() {
    var mStockHandicapData: MutableLiveData<StockHandicapData?> = MutableLiveData()
    var mPushStockHandicapData: MutableLiveData<StockHandicapData?> = MutableLiveData()
    var mOrderData: MutableLiveData<OrderData?> = MutableLiveData()
}