package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.GlobalStockInfo

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/29
 * Desc:
 */
class GlobalStockViewModel :ViewModel(){
    var coustomList: MutableLiveData<MutableList<Int>> = MutableLiveData()
    var usaList: MutableLiveData<MutableList<Int>> = MutableLiveData()
    var enuList: MutableLiveData<MutableList<Int>> = MutableLiveData()
    var asiaList: MutableLiveData<MutableList<Int>> = MutableLiveData()
}