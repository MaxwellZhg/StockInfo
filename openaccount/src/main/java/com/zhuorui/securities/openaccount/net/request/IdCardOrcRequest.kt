package com.zhuorui.securities.openaccount.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    desc   : 上传身份证
 */
class IdCardOrcRequest(
    val cardType:Int,//身份证正反面标识 0：人像面 1：国徽面
    val cardPhoto: String, // 签名照片base64码
    val id: String, // 开户id
    transaction: String
) : BaseRequest(transaction) {

    init {
        generateSign()
    }

}