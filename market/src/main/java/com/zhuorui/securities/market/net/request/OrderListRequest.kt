package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/16 11:04
 *    desc   : 计算交易费用
 */
class OrderListRequest(
    accountId: String,
    startDate: String,
    endDate: String,
    token: String,
    transaction: String
) : BaseRequest(transaction) {

    val accountId = accountId
    val startDate = startDate
    val endDate = endDate
    val token = token
    var pageNum = 1
    var pageSize = 50

    constructor(
        accountId: String,
        startDate: String,
        endDate: String,
        token: String,
        pageNum: Int,
        pageSize: Int,
        transaction: String
    ) : this(accountId, startDate, endDate, token, transaction) {
        this.pageNum = pageNum
        this.pageSize = pageSize
    }

    init {
        generateSign()
    }


}