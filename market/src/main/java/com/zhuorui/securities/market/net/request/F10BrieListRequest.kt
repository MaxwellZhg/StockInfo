package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/8 16:34
 *    desc   : F10获取列表分页
 */
class F10BrieListRequest(
    val ts: String,
    val code: String,
    val currentPage: Int,
    transaction: String
) : BaseRequest(transaction) {
    init {
        generateSign()
    }
}