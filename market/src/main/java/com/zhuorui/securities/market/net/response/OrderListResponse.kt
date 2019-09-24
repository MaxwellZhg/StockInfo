package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.market.model.STOrderData

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-19 16:11
 *    desc   :
 */
class OrderListResponse : BaseResponse() {
    var records: List<STOrderData>? = null
    var total: Int? = null//总数
    var size: Int? = null//返回的数据大小
    var current: Int? = null//起始页数
    var pages: Int? = null//一页查询多少条
    var searchCount: Boolean? = null
}