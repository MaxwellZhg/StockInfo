package com.zhuorui.securities.personal.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/10
 * Desc:
 */
class ModifyNewPhoneCodeRequest(val phone: String?,val verificationCode:String? ,val newPhone:String, val newVerificationCode:String,val oldPhoneArea:String,val newPhoneArea:String,transaction: String ) :BaseRequest(transaction){

    init {
        generateSign()
    }
}