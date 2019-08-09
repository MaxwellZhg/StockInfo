package com.dycm.applib1.socket.request

import java.util.*

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/25 15:51
 * desc   : 获取日K
 */
open class StockKlineGetDaily(
    val ts: String,// 属于的股票市场(SZ-深圳,SH-上海,HK-港股,US-美股)
    val code: String, // 股票代码
    val startTime: Long, // 开始时间
    val endTime: Long, // 结束时间
    val pageSize: Int // 查询条数
) {
    val uuid = UUID.randomUUID().toString()
}