package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/16 10:43
 *    desc   : 获取单支股票详情
 */
class GetStockInfoRequest(val ts: String, val code: String, transaction: String) : BaseRequest(transaction) {

    init {
        generateSign()
    }
}