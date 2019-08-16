package com.zhuorui.securities.infomation.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/16
 * Desc:
 */
class UserLoginCodeResponse(val data:Data) : BaseResponse(){
    data class Data(
        val userLoginDto: UserLoginDto,
        val lastLoginDeviceVo :LastLoginDeviceVo
    )
    data class UserLoginDto(
        val token: String
    )
    data class LastLoginDeviceVo(
        val lastLogionDate:Long,
        val lastLoginId:String,
        val osType:String
    )

}