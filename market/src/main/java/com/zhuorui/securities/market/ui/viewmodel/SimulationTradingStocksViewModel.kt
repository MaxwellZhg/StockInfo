package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.net.response.GetStockInfoResponse
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
    val stockInfoData = MutableLiveData<GetStockInfoResponse.Data>()
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
    // 买入股票的价格
    val buyPrice = MutableLiveData<BigDecimal>()
    // 买入股票的最小变动价格
    val minChangesPrice = MutableLiveData<BigDecimal>()
    // 买入股票数量
    val buyCount = MutableLiveData<Long>()
    // 买入股票的金额
    val buyMoney = MutableLiveData<BigDecimal>()
    // 最大可买数量
    val maxBuyCount = MutableLiveData<Long>()
    // 是否可买
    val enableBuy = MutableLiveData<Boolean>()

    init {
        buyRate.value = BigDecimal.valueOf(50.00)
        sellRate.value = BigDecimal.valueOf(50.00)
    }
}