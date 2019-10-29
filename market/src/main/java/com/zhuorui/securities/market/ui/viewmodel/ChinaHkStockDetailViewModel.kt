package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/29
 * Desc:
 */
class ChinaHkStockDetailViewModel :ViewModel(){
    var namelist: MutableLiveData<MutableList<Int>> = MutableLiveData()
    var containerlist: MutableLiveData<MutableList<Int>> = MutableLiveData()
}