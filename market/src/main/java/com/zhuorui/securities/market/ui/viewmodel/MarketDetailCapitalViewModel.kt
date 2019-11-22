package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.CapitalTrendModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:53
 *    desc   :
 */
class MarketDetailCapitalViewModel : ViewModel() {
    var mCapitalTrends: MutableLiveData<MutableList<CapitalTrendModel>> = MutableLiveData()

}