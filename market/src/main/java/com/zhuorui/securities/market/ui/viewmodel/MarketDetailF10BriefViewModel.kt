package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.F10ManagerModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/6 13:56
 *    desc   :
 */
class MarketDetailF10BriefViewModel : ViewModel() {
    val managers = MutableLiveData<ArrayList<F10ManagerModel>>()
}