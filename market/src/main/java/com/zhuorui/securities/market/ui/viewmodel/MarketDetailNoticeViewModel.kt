package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.TestNoticeData

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:53
 *    desc   :
 */
class MarketDetailNoticeViewModel:ViewModel() {
    var infoList:MutableLiveData<MutableList<TestNoticeData>> = MutableLiveData()
}