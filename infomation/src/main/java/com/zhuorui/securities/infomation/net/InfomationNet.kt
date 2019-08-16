package com.zhuorui.securities.infomation.net

import com.zhuorui.securities.infomation.net.api.InfomationApi
import com.zhuorui.securities.infomation.net.request.SendLoginCodeRequest
import com.zhuorui.securities.infomation.net.request.UserLoginCodeRequest
import com.zhuorui.securities.infomation.net.response.SendLoginCodeResponse
import com.zhuorui.securities.infomation.net.response.UserLoginCodeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/15
 * Desc:
 */
interface InfomationNet {
    @POST(InfomationApi.SEND_LOGIN_CODE)
    fun sendLoginCode(@Body request: SendLoginCodeRequest): Call<SendLoginCodeResponse>
    @POST(InfomationApi.USER_LOGIN_CODE)
    fun userLoginCode(@Body request: UserLoginCodeRequest): Call<UserLoginCodeResponse>
}