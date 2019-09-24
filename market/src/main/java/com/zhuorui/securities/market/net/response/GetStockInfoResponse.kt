package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.market.model.BaseStockMarket
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/16 10:45
 *    desc   : 获取单支股票详情
 */
class GetStockInfoResponse(val data: Data) : BaseResponse() {

    data class Data(
        val id: String,
        val tsCode: String, // 代码，如： 股票代码.股票市场 例：00001.HK
        val name: String, // 证券名称
        val perShareNumber: Int, // 每手股数
        val industry: String, // 行业类别
        val market: String, // 上市板
        val realPrice: BigDecimal // 实时股价
    ) : BaseStockMarket()
}