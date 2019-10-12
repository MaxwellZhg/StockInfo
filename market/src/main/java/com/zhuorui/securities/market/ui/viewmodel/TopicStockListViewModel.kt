package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.StockMarketInfo

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class TopicStockListViewModel : ViewModel() {
    var datas: MutableList<StockMarketInfo>? = null
}