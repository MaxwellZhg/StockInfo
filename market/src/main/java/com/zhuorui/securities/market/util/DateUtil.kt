package com.zhuorui.securities.market.util

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/7 15:06
 *    desc   : 格式化时间
 */
object DateUtil {

    /**
     * 格式化时间
     * @param dateStr 传入的参数必须如20190101
     */
    fun formatDate(dateStr: String): String? {
        if (dateStr.isNotEmpty() && dateStr.length == 8) {
            return dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6) + "-" + dateStr.substring(6, 8)
        }
        return dateStr
    }
}