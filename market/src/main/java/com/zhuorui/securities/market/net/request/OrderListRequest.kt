package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/16 11:04
 *    desc   : 计算交易费用
 */
class OrderListRequest : BaseRequest {

    init {
        generateSign()
    }

    constructor(accountId:String,
                startDate:String,
                endDate:String,
                token:String,
                transaction: String) : super(transaction)

    constructor(accountId:String,
                startDate:String,
                endDate:String,
                token:String,
                pageNum:Int,
                pageSize:Int,
                transaction: String) : super(transaction)

}