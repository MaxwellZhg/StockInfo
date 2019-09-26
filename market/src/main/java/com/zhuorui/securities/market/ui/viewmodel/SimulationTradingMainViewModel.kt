package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.STFundAccountData
import com.zhuorui.securities.market.model.STOrderData
import com.zhuorui.securities.market.model.STPositionData

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 14:08
 *    desc   :
 */
class SimulationTradingMainViewModel : ViewModel() {

    var positionDatas: MutableLiveData<List<STPositionData>> = MutableLiveData()
    var orderDatas: MutableLiveData<List<STOrderData>> = MutableLiveData()
    var fundAccount:MutableLiveData<STFundAccountData> = MutableLiveData()

}