package com.zhuorui.securities.personal.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/11
 * Desc:修改登录密码
 * */
class ModifyLoginPswRequest (val oldLoginPassword: String?,val newLoginPassword:String , transaction: String):BaseRequest(transaction){
    init {
        generateSign()
    }
}