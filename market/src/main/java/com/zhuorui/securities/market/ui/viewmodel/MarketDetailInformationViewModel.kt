package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.net.response.MarketNewsListResponse

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:53
 *    desc   :
 */
class MarketDetailInformationViewModel:ViewModel() {
    var infoList : MutableLiveData<MutableList<MarketNewsListResponse.DataList>> =  MutableLiveData()
}