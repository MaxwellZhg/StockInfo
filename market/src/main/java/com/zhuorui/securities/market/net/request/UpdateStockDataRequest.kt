package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/16 10:58
 *    desc   : 更新股票数据
 */
class UpdateStockDataRequest(
    val id: String, // id
    val ts: String, // 属于的股票市场(SZ-深圳,SH-上海,HK-港股,US-美股)
    val code: String, // 股票代码
    val tsCode: String, // 代码，如： 股票代码.股票市场 例：00001.HK
    val name: String, // 证券名称
    val perShareNumber: BigDecimal, // 每手股数
    val industry: String, // 行业类别
    val market: String, // 上市板
    transaction: String
) : BaseRequest(transaction) {

    init {
        generateSign()
    }
}