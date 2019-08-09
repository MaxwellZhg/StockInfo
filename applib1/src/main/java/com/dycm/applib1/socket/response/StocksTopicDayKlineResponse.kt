package com.dycm.applib1.socket.response

import com.dycm.applib1.socket.vo.kline.DayKlineData

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/23 15:49
 * desc   : 日K数据返回对象
 */
class StocksTopicDayKlineResponse : SocketResponse() {

    var data: ArrayList<DayKlineData>? = null
}