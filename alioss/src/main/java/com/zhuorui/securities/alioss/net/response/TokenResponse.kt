package com.zhuorui.securities.alioss.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-24 17:07
 *    desc   :
 */
class TokenResponse(val data: Data) : BaseResponse() {

    data class Data(
        val accessKeyId: String,
        val securityToken: String,
        val accessKeySecret: String,
        val durationSeconds: Long
    )
}