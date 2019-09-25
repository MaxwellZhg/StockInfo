package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/24 16:52
 *    desc   : 计算交易费用
 */
class FeeComputeResponse(val data: Data) : BaseResponse() {
    data class Data(
        val totalFee: BigDecimal// 总交易费用
    )
}