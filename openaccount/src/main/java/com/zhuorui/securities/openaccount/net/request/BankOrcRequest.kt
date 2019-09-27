package com.zhuorui.securities.openaccount.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    desc   : 银行卡OCR
 */
class BankOrcRequest(
    val fileType: String,//文件类型 1-base64 2-url ，默认为1
    val bankCardPhotoUrl: String,//图片url（大小不 超过3M），文件类型为2时必填
    val id: String, // 开户id
    transaction: String
) : BaseRequest(transaction) {

    init {
        generateSign()
    }

}