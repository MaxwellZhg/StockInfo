package com.zhuorui.securities.personal.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/15
 * Desc:
 */
class GetUserInfoResponse(val data : Data) :BaseResponse(){
    data class Data(
        val zrNo:Int,
        val nickname:String,
        val profile:String,
        val sex:String,
        val headPhoto:String
    )


}