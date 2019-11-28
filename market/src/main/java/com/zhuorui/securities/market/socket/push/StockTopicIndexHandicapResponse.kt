package com.zhuorui.securities.market.socket.push

import com.zhuorui.securities.market.model.PushIndexHandicapData
import com.zhuorui.securities.market.socket.request.SocketHeader
import com.zhuorui.securities.market.socket.response.SocketResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/22
 * Desc:股票推送返回数据response
 * */
class StockTopicIndexHandicapResponse :SocketResponse(){
    var header: SocketHeader? = null
    var body: PushIndexHandicapData? = null
}