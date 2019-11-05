package com.zhuorui.securities.market.socket.vo

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/4 16:59
 *    desc   : 买卖经纪席位数据(买卖盘席位)
 */
class OrderBrokerData {
    var ts: String? = null
    var type: Int? = null
    var code: String? = null
    var tradeMark: Int? = null // 1-买方经纪 2-卖方经纪
    var content: List<String>? = null // 经纪商
    var recordId: String? = null // 记录唯一id 32位字符串
}