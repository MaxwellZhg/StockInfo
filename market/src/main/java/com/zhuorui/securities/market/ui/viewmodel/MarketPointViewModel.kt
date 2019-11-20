package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.net.response.StockConsInfoResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/22
 * Desc:
 */
class MarketPointViewModel :ViewModel(){
    var infos: MutableLiveData<MutableList<StockConsInfoResponse.ListInfo>> = MutableLiveData()
    var pointInfos: MutableLiveData<MutableList<Int>> = MutableLiveData()
}