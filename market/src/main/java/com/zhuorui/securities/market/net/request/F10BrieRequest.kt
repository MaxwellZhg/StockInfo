package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/7 10:25
 *    desc   : F10简况
 */
class F10BrieRequest(val ts: String, val code: String, transaction: String) : BaseRequest(transaction) {
    init {
        generateSign()
    }
}