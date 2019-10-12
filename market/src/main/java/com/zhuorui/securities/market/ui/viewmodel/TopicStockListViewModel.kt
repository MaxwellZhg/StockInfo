package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.StockMarketInfo

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class TopicStockListViewModel : ViewModel() {
    val datas: MutableLiveData<ArrayList<StockMarketInfo>> = MutableLiveData()

    init {
        datas.value = ArrayList()
    }
}