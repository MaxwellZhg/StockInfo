package com.zhuorui.securities.infomation.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/21
 * Desc:
 */
class VerifForgetCodeRequest (val phone: String,val verificationCode:String,transaction: String):BaseRequest(transaction){
    init {
        generateSign()
    }
}