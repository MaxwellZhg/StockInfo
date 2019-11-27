package com.zhuorui.securities.personal.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/15
 * Desc:查询用户信息请求
 * */
class GetUserInfoResponse(val data : Data) :BaseResponse(){
    data class Data(
        val zrNo:Int,//卓锐号
        val nickname:String,//昵称
        val profile:String,//个人简介
        val sex:String,//1男  2女 3保密
        val headPhoto:String// 头像
    )


}