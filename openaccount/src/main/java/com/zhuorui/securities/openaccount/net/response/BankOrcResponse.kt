package com.zhuorui.securities.openaccount.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 *    desc   : 身份证OCR
 */
class BankOrcResponse(val data: Data) : BaseResponse() {

    data class Data(
        val bankCardNo: String, // 银行卡号
        val bankCardName: String // 银行名称
    )
}