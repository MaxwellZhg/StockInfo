package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.util.JsonUtil
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/23 18:17
 *    desc   : 获取股票计算交易费用规则模版
 */
class GetFeeTemplateResponse(
    val data: List<Data>
) : BaseResponse() {

    inner class Data(
        val id: String,
        val chargeType: Int,// 收费类型 1买入 2 买出
        val ruleDesc: String,// 收费规则说明
        val charger: Int,//收费方 1-卓锐证券 2-香港交易所 3-香港结算所 4-香港政府 5-香港证监会
        val feeType: Int// 费用类型 1- 佣金 2 -平台使用费 3 -交易系统使用费 4-中央系统使用费 5-印花税 6-交易费 7-交易征费
    ) {
        var chargeMode: Int? = null// 收费方式（1交易金额的百分比 2固定每笔金额 3免交易费）
        var rule: String? = null
            // 收费规则json
            set(value) {
                field = value

                if (chargeMode == 1) {
                    ruleObject = value?.let { JsonUtil.fromJson(it, FeeRule::class.java) }
                } else if (chargeMode == 2) {
                    ruleObject = value?.let { JsonUtil.fromJson(it, FixedFee::class.java) }
                }
            }
        var ruleObject: Any? = null// 收费规则对象
    }

    /**
     * 收费规则
     */
    data class FeeRule(
        val percentage: BigDecimal,// 收费百分比
        val min: BigDecimal,// 最低收费，未返回该值时，不做这一步的限定
        val max: BigDecimal// 最高收费，未返回该值时，不做这一步的限定
    )

    /**
     * 固定收费方式
     */
    data class FixedFee(val fixed: BigDecimal)
}