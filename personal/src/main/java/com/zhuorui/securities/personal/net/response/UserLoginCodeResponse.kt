package com.zhuorui.securities.personal.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/16
 * Desc:用户登录response
 * */
class UserLoginCodeResponse(val data:Data) : BaseResponse(){
    data class Data(
        val userId: String,//用户标识
        val token :String,//token
        val phone:String,//手机号码
        val loginCount: Int//剩余登录次数(目前此参数一直为空，code为010005改为弹框提示)   注：不需要处理此字段。
    )


}