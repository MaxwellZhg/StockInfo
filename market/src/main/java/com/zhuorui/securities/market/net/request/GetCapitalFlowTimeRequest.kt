package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-11-25 14:06
 *    desc   : 按时间查询资金统计数据请求参数
 */
class GetCapitalFlowTimeRequest(ts: String, code: String, dayCount: Int, transaction: String) :
    BaseRequest(transaction) {
    init {
        generateSign()
    }
}