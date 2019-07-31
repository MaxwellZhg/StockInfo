package com.dycm.applib1.socket

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/17 17:46
 * desc   : 定义 websocket 响应请求
 */
class SocketResponse {

    var code: String? = null// 0代表成功
    var msg: String? = null
    var path: String? = null
    var resp_id: String? = null

    val isSuccessful: Boolean
        get() = code != null && code == "0"
}
