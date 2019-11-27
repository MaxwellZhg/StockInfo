package com.zhuorui.securities.market.socket.response

import com.zhuorui.securities.market.socket.vo.IndexPonitHandicapData

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/22
 * Desc:获取指数消息查询response
 * */
class GetIndexPonitInfoResponse :SocketResponse(){
    var data: List<IndexPonitHandicapData>?= null
}