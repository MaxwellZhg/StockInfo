package com.zhuorui.securities.openaccount.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    desc   : 上传身份证
 */
class IdCardOrcRequest(
    val cardType:Int,//身份证正反面标识 0：人像面 1：国徽面
    val fileType: String, // 文件类型 1-base64  2-url 默认为1
    val cardPhotoUrl: String, // 图片url（不超过 3M），文件类型为2时必填
    val id: String, // 开户id
    transaction: String
) : BaseRequest(transaction) {

    init {
        generateSign()
    }

}