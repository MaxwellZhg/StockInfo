package com.zhuorui.securities.openaccount.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    desc   : 银行卡OCR
 */
class SubRiskDisclosureRequest(
    val id: String, // 开户id
    transaction: String
) : BaseRequest(transaction) {

    init {
        generateSign()
    }

}