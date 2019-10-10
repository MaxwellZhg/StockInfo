package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.SearchDeafaultData
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.TestSeachDefaultData
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
    var searchInfoDatas: MutableLiveData<MutableList<SearchDeafaultData>> = MutableLiveData()
    var stockdatas: MutableLiveData<MutableList<SearchStockInfo>> = MutableLiveData()
    var infoadatas: MutableLiveData<MutableList<TestSeachDefaultData>> = MutableLiveData()
    var infos: MutableLiveData<MutableList<Int>> = MutableLiveData()
}