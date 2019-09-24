package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.model.SearchStokcInfoEnum
import com.zhuorui.securities.market.model.StockPageInfo
import com.zhuorui.securities.market.ui.adapter.SearchInfoAdapter

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/19
 * Desc:
 */
class SearchInfoViewModel :ViewModel(){
    var adapter: MutableLiveData<SearchInfoAdapter> = MutableLiveData()
    var fragments=ArrayList<StockPageInfo>()
    init {
        fragments.add(StockPageInfo(ResUtil.getString(R.string.stock_search_all),SearchStokcInfoEnum.All))
        fragments.add(StockPageInfo(ResUtil.getString(R.string.stock_search_topic),SearchStokcInfoEnum.Stock))
        fragments.add(StockPageInfo(ResUtil.getString(R.string.stock_search_info),SearchStokcInfoEnum.Info))
    }
}