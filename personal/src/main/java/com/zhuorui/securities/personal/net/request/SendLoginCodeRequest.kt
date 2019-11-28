package com.zhuorui.securities.personal.net.request

import com.alibaba.fastjson.annotation.JSONField
import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/15
 * Desc:发送登录验证码
 * */
class SendLoginCodeRequest(val phone: String, val countryCode: String, transaction: String) : BaseRequest(transaction) {

 /*   @JSONField(name = "isSend")
    var isSend = false*/

    init {
        generateSign()
    }
}