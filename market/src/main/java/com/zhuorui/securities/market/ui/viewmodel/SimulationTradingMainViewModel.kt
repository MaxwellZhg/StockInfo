package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.STFundAccountData
import com.zhuorui.securities.market.model.STPositionData
import com.zhuorui.securities.market.model.STOrderData

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 14:08
 *    desc   :
 */
class SimulationTradingMainViewModel : ViewModel() {
    var positionDatas: MutableLiveData<MutableList<STPositionData>> = MutableLiveData()
    var orderDatas: MutableLiveData<MutableList<STOrderData>> = MutableLiveData()
    var fundAccountData: MutableLiveData<STFundAccountData> = MutableLiveData()
}