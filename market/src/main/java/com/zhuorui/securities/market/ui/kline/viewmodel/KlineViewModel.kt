package com.zhuorui.securities.market.ui.kline.viewmodel

import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.customer.view.kline.dataManage.KLineDataManage
import com.zhuorui.securities.market.model.StockTopic

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 11:02
 *    desc   :
 */
class KlineViewModel : ViewModel() {

    var stockTopic: StockTopic? = null
    var kDataManager: KLineDataManage? = null
}