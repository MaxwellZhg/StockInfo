package com.dycm.applib1.socket.request

import java.util.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/6 16:31
 *    desc   : 拉取webSocket自选股K线分时数据
 */
class StockMinuteKline(val ts: String, val code: String, val type: Int) {
    val uuid = UUID.randomUUID().toString()
}