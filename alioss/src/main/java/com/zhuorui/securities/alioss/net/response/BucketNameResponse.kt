package com.zhuorui.securities.alioss.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-24 17:08
 *    desc   :
 */
class BucketNameResponse(val data: List<Data>) : BaseResponse() {

    data class Data(
        val type: String,
        val bucketName: String,
        val endpoint: String
    )
}