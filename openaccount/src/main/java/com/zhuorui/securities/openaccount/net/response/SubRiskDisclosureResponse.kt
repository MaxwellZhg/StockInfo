package com.zhuorui.securities.openaccount.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 *    desc   : 身份证OCR
 */
class SubRiskDisclosureResponse(val data: Data) : BaseResponse() {

    data class Data(
        val openStatus: Int//开户状态
    )
}