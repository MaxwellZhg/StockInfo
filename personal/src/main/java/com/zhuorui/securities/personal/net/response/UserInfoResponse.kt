package com.zhuorui.securities.personal.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/9
 * Desc:用户信息response
 * */
class UserInfoResponse(val data : Data) :BaseResponse(){
     data class Data(
       val userinfo:UserInfo,
       val userAccount:UserAccount
     )
    data class UserInfo(
        val id :String,//用户id
        val nickname :String,//昵称
        val profile :String,//
        val sex :Int,//性别
        val headPhoto :String,//头像
        val createDate :Long,//创建日期
        val updateDate :String//更新日期
    )
    data class UserAccount(
        val id :String,//id
        val phoneArea :String,//手机号区域
        val phone :String,//手机号
        val loginPassword :String,//登录密码
        val capitalPassword :String,//
        val accountStatus :Int,//账户状态
        val isDelete :Int,
        val createDate :String,//创建日期
        val updateDate :String//更新日期
    )
}