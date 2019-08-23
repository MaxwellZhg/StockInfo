package com.zhuorui.securities.infomation.net

import com.zhuorui.securities.infomation.net.api.InfomationApi
import com.zhuorui.securities.infomation.net.request.*
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

    @POST(InfomationApi.USER_REGISTER_CODE)
    fun userPwdCode(@Body request: UserLoginRegisterRequest): Call<UserLoginCodeResponse>

    @POST(InfomationApi.USER_LOGIN_OUT)
    fun userLoginOut(@Body request: UserLoginOutRequest): Call<SendLoginCodeResponse>

    @POST(InfomationApi.USER_PWD_CODE)
    fun userLoginByPwd(@Body request: UserLoginPwdRequest): Call<UserLoginCodeResponse>

    @POST(InfomationApi.SEND_FORGET_CODE)
    fun sendForgetPwdCode(@Body request: SendLoginCodeRequest): Call<SendLoginCodeResponse>

    @POST(InfomationApi.VERIFY_FORGET_CODE)
    fun verifyForgetCode(@Body request: VerifForgetCodeRequest): Call<SendLoginCodeResponse>

    @POST(InfomationApi.REST_LOGIN_PSW)
    fun restLoginPsw(@Body request: RestLoginPswRequest): Call<SendLoginCodeResponse>
}