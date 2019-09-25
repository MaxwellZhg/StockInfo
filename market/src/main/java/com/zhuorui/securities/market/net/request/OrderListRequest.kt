package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/16 11:04
 *    desc   : 计算交易费用
 */
class OrderListRequest : BaseRequest {

    var accountId: String? = null
    var startDate: String? = null
    var endDate: String? = null
    var token: String? = null
    var pageNum = 1
    var pageSize = 50

    constructor(
        accountId: String,
        startDate: String,
        endDate: String,
        token: String,
        transaction: String
    ) : super(transaction) {
        this.startDate = startDate
        this.endDate = endDate
        this.accountId = accountId
        this.token = token
        this.transaction = transaction
        generateSign()
    }

    constructor(
        accountId: String,
        startDate: String,
        endDate: String,
        token: String,
        pageNum: Int,
        pageSize: Int,
        transaction: String
    ) : super(transaction) {
        this.startDate = startDate
        this.endDate = endDate
        this.accountId = accountId
        this.token = token
        this.transaction = transaction
        this.pageNum = pageNum
        this.pageSize = pageSize
        generateSign()
    }



}