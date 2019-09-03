package com.zhuorui.securities.openaccount.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 *    desc   : 身份证OCR
 */
class IdCardOrcResponse(val data: Data) : BaseResponse() {

    data class Data(
        val openStatus: Int, // 开户状态
        val cardFrontPhoto: String, // 身体证人像面url
        val cardBackPhoto: String // 身体证国徽面url
    )
}