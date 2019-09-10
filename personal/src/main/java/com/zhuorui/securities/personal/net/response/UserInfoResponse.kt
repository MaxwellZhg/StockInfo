package com.zhuorui.securities.personal.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/9
 * Desc:
 */
class UserInfoResponse(val data : Data) :BaseResponse(){
     data class Data(
       val userinfo:UserInfo,
       val userAccount:UserAccount
     )
    data class UserInfo(
        val id :String,
        val nickname :String,
        val profile :String,
        val sex :Int,
        val headPhoto :String,
        val createDate :Long,
        val updateDate :String
    )
    data class UserAccount(
        val id :String,
        val phoneArea :String,
        val phone :String,
        val loginPassword :String,
        val capitalPassword :String,
        val accountStatus :Int,
        val isDelete :Int,
        val createDate :String,
        val updateDate :String
    )
}