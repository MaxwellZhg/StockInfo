package com.zhuorui.securities.market.socket.request

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/6 16:31
 *    desc   : 查询股票价格数据
 */
class GetStockPriceRequestBody(vararg stockVo: StockVo) {

    val stockVos = stockVo

    class StockVo(
        val ts: String, // 股票ts代码，HK，SH，SZ，US
        val code: String,// 股票代码
        val type: Int// 股票类型，1：指数，2：股票
    )
}