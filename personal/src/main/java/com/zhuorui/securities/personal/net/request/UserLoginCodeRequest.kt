package com.zhuorui.securities.personal.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/16
 * Desc:用户登录码请求 */
class UserLoginCodeRequest (val phone: String?,val verificationCode:String? ,val phoneArea:String?, transaction: String):BaseRequest(transaction){
    init {
        generateSign()
    }
}