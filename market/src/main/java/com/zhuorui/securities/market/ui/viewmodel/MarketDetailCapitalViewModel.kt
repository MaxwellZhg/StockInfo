package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.CapitalTrendModel
import com.zhuorui.securities.market.socket.vo.CapitalData

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:53
 *    desc   :
 */
class MarketDetailCapitalViewModel : ViewModel() {
    var mCapitalTrends: MutableLiveData<MutableList<CapitalTrendModel>> = MutableLiveData()
    var mCapitalData: MutableLiveData<CapitalData> = MutableLiveData()

}