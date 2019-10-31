package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.PushStockPriceData

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-11 15:59
 *    desc   :
 */
class MarketDetailViewModel : ViewModel() {
    var pushStockPriceData: MutableLiveData<PushStockPriceData> = MutableLiveData()
}