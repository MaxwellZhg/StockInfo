package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/24 15:49
 *    desc   : 买入股票
 */
class StockBuyRequest(
    val accountId: String,// 账号id
    val ts: String,// 股票类型 ：SZ, SH, HK, US
    val code: String,// 股票编码
    val price: BigDecimal,// 单价，最多三位小数
    val count: Long,// 股票数量
    val commissionFee: BigDecimal,// 佣金（包含各种收费）
    val totalFee: BigDecimal,// 股票总价格和佣金之和
    transaction: String
) : BaseRequest(transaction)