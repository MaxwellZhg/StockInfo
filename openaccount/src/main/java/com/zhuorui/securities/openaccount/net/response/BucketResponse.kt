package com.zhuorui.securities.openaccount.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-26 17:41
 *    desc   :
 */
class BucketResponse(val data: List<Data>) : BaseResponse() {
    data class Data(
        val type: String, // 类型 1 开户
        val bucketName: String, // 储存空间名
        val endpoint: String // OSS区域地址
    )
}