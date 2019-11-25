package com.zhuorui.securities.market.socket.response

import com.zhuorui.securities.market.socket.vo.CapitalData


/**
 *    author : liuwei
 *    e-mail :
 *    date   :
 *    desc   : 查询资金统计数据
 */
class GetCapitalResponse : SocketResponse() {
    var data: CapitalData? = null
}