package com.dycm.applib1.socket

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/17 17:53
 * desc   :
 */
class SocketRequest {

    var header: SocketHeader? = null
    private var body: Any? = null

    fun setBody(body: Any) {
        this.body = body
    }
}
