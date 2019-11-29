package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.net.response.MarketNewsListResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:
 */
class MarketPointConsInfoViewModel :ViewModel(){
    var infoList : MutableLiveData<MutableList<MarketNewsListResponse.DataList>> =  MutableLiveData()
}