package com.zhuorui.securities.openaccount.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    desc   : 银行卡OCR
 */
class BankOrcRequest(
    val bankCardPhoto: String, // 银行卡号(base64,大小不 超过3M)
    val id: String, // 开户id
    transaction: String
) : BaseRequest(transaction) {

    init {
        generateSign()
    }

}