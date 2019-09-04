package com.zhuorui.securities.openaccount.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    desc   : 银行卡三要素认证+一类卡认证
 */
class BankCardVerificationRequest(
    val bankCardNo: String, // 银行卡号
    val bankCardName: String, // 银行卡的银行
    val id: String, // 开户id
    transaction: String
) : BaseRequest(transaction) {

    init {
        generateSign()
    }

}