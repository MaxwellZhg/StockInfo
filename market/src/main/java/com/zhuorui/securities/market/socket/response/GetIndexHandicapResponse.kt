package com.zhuorui.securities.market.socket.response

import com.zhuorui.securities.market.socket.vo.IndexPonitHandicapData


/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/21 19:16
 *    desc   :查询指数盘口数据
 */
class GetIndexHandicapResponse : SocketResponse() {

    var data: List<IndexPonitHandicapData>? = null
}