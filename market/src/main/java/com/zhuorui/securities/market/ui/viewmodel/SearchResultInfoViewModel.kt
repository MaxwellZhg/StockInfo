package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.ui.adapter.SeachAllofInfoAdapter
import com.zhuorui.securities.market.ui.adapter.StockAdapter
import com.zhuorui.securities.market.ui.adapter.StockInfoAdapter

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/20
 * Desc:
 */
class SearchResultInfoViewModel :ViewModel(){
    var adapter: MutableLiveData<SeachAllofInfoAdapter> = MutableLiveData()
    var stockadapter: MutableLiveData<StockAdapter> = MutableLiveData()
    var infoadapter: MutableLiveData<StockInfoAdapter> = MutableLiveData()
}