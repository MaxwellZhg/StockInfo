package com.zhuorui.securities.openaccount.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-03 18:21
 *    desc   :
 */
class SubBasicsInfoResponse(val data: Data) : BaseResponse() {

    data class Data(
        val openStatus: Int//开户状态
    )
}