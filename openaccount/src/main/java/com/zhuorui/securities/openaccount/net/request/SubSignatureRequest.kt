package com.zhuorui.securities.openaccount.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/29 18:05
 *    desc   : 上传电子签名
 */
class SubSignatureRequest(
    val fileType: String, // 文件类型 1-base64 2-url， 默认为1
    val signaturePhotoUrl: String, // 图片url（大小不超过3M），文件类型为2时必填
    val id: String, // 开户id
    transaction: String
) : BaseRequest(transaction) {

    init {
        generateSign()
    }

}