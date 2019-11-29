package com.zhuorui.securities.personal.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/10
 * Desc:发送旧手机号验证码
 * */
class SendOldRepalceCodeRequest(val phone: String?,val countryCode:String ,val isSend:Boolean, transaction: String) :BaseRequest(transaction){
   init {
      generateSign()
    }
}