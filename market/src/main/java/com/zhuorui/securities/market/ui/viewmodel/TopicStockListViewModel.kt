package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.ui.TopicStocksAdapter

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class TopicStockListViewModel : ViewModel() {

    var adapter: MutableLiveData<TopicStocksAdapter> = MutableLiveData()
}