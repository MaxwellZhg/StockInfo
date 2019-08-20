package com.zhuorui.securities.infomation.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class UserLoginrRegisterRequest (val loginPassword:String,val verificationCode: String,val phone:String ,val phoneArea:String, transaction: String):
    BaseRequest(transaction){
    init {
        generateSign()
    }
}