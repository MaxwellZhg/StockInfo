package com.zhuorui.securities.market.model

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/25 18:36
 *    desc   : 搜索返回的自选股信息
 */
class SearchStockInfo : BaseStockMarket(), IStocks {

    var id: String? = null
    var tsCode: String? = null
    var name: String? = null

    override fun getIName(): String {
        return if (name == null) "" else name!!
    }

    override fun getICode(): String {
        return if (code == null) "" else code!!
    }

    override fun getITs(): String {
        return if (ts == null) "" else ts!!
    }

    override fun getITsCode(): String {
        return if (tsCode == null) "" else tsCode!!
    }

}