package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.market.model.STOrderData

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-19 16:11
 *    desc   :
 */
class OrderListResponse(val data: Data) : BaseResponse(){

    data class Data(
        val list: List<STOrderData>?,
        val total: Int?,//总数
        val size: Int?,//返回的数据大小
        val current: Int?,//起始页数
        val pages: Int?,//一页查询多少条
        val searchCount: Boolean?
    )
}