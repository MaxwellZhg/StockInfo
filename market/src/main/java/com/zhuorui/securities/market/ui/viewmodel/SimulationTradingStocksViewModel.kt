package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.SearchStockInfo
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/9 13:36
 *    desc   :
 */
class SimulationTradingStocksViewModel : ViewModel() {
    // 股票信息
    val stockInfo = MutableLiveData<SearchStockInfo>()
    // 当前价格：如13.75
    val price = MutableLiveData<BigDecimal>()
    // 跌涨价格：如-1.26、+1.68
    val diffPrice = MutableLiveData<BigDecimal>()
    // 涨跌幅：如-0.0018（-0.18%）
    val diffRate = MutableLiveData<BigDecimal>()
    // 买入比例
    val buyRate = MutableLiveData<BigDecimal>()
    // 卖出比例
    val sellRate = MutableLiveData<BigDecimal>()
    // 买入股票数量
    val buyCount = MutableLiveData<Int>()

    init {
        buyRate.value = BigDecimal.valueOf(50.00)
        sellRate.value = BigDecimal.valueOf(50.00)
    }
}