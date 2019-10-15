package com.zhuorui.securities.market.ui.kline.viewmodel

import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.customer.view.kline.dataManage.TimeDataManage
import com.zhuorui.securities.market.model.StockTopic

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 9:48
 *    desc   : 分时数据
 */
class OneDayKlineViewModel : ViewModel() {
    var stockTopic: StockTopic? = null
    var kDataManager: TimeDataManage? = null
}