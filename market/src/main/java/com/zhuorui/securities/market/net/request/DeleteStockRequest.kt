package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest
import com.zhuorui.securities.market.model.SearchStockInfo

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/23 14:19
 *    desc   : 删除自选股
 */
class DeleteStockRequest :
    BaseRequest {

    constructor(ids: Array<String?>, ts: String, code: String, transaction: String) : super(transaction){
        this.ids = ids
        this.ts = ts
        this.code = code

        generateSign()
    }

    constructor(stockInfo: SearchStockInfo, ids: Array<String?>, ts: String, code: String, transaction: String) : super(transaction){
        this.stockInfo = stockInfo
        this.ids = ids
        this.ts = ts
        this.code = code

        generateSign()
    }

    var ids: Array<String?>? = null
    var ts: String? = null
    var code: String? = null
    var stockInfo: SearchStockInfo? = null

}