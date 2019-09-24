package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.net.response.GetFeeTemplateResponse
import com.zhuorui.securities.market.net.response.GetStockInfoResponse
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/9 13:36
 *    desc   :
 */
class SimulationTradingStocksViewModel : ViewModel() {
    // 股票基础信息
    val stockInfo = MutableLiveData<SearchStockInfo>()
    // 股票价格信息
    val stockInfoData = MutableLiveData<GetStockInfoResponse.Data>()
    // 股票交易收费规则
    val stockFeeRules = MutableLiveData<Map<Int, GetFeeTemplateResponse.Data>>()
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

    fun getFee(number: BigDecimal, feeRule: GetFeeTemplateResponse.Data): BigDecimal {
        when (feeRule.chargeMode) {
            // 按百分比收费
            1 -> {
                // 计算费用
                val fee = number.multiply(feeRule.feeRuleDto.percentage)
                // 是否低于最低收费
                if (feeRule.feeRuleDto.min != null && fee.compareTo(feeRule.feeRuleDto.min) == -1) {
                    return feeRule.feeRuleDto.min
                }
                // 是否高于最高收费
                else if (feeRule.feeRuleDto.max != null && fee.compareTo(feeRule.feeRuleDto.max) == 1) {
                    return feeRule.feeRuleDto.max
                }
                return fee
            }
            // 固定收费
            2 -> {
                return feeRule.feeRuleDto.fixed
            }
            // 免费
            else -> {
                return BigDecimal.ZERO
            }
        }
    }
}