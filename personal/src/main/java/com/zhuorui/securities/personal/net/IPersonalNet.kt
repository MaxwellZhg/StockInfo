package com.zhuorui.securities.personal.net

import com.zhuorui.securities.base2app.network.BaseRequest
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.personal.net.api.PersonalApi
import com.zhuorui.securities.personal.net.request.*
import com.zhuorui.securities.personal.net.response.GetUserInfoResponse
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.net.response.UserInfoResponse
import com.zhuorui.securities.personal.net.response.UserLoginCodeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/15
 * Desc:
 */
interface IPersonalNet {

    @POST(PersonalApi.SEND_LOGIN_CODE)
    fun sendLoginCode(@Body request: SendLoginCodeRequest): Call<SendLoginCodeResponse>

    @POST(PersonalApi.USER_LOGIN_CODE)
    fun userLoginCode(@Body request: UserLoginCodeRequest): Call<UserLoginCodeResponse>

    @POST(PersonalApi.USER_REGISTER_CODE)
    fun userPwdCode(@Body request: UserLoginRegisterRequest): Call<UserLoginCodeResponse>

    @POST(PersonalApi.USER_LOGIN_OUT)
    fun userLoginOut(@Body request: UserLoginOutRequest): Call<BaseResponse>

    @POST(PersonalApi.USER_PWD_CODE)
    fun userLoginByPwd(@Body request: UserLoginPwdRequest): Call<UserLoginCodeResponse>

    @POST(PersonalApi.SEND_FORGET_CODE)
    fun sendForgetPwdCode(@Body request: SendLoginCodeRequest): Call<SendLoginCodeResponse>

    @POST(PersonalApi.VERIFY_FORGET_CODE)
    fun verifyForgetCode(@Body request: VerifForgetCodeRequest): Call<SendLoginCodeResponse>

    @POST(PersonalApi.REST_LOGIN_PSW)
    fun restLoginPsw(@Body request: RestLoginPswRequest): Call<SendLoginCodeResponse>

    @POST(PersonalApi.GET_USER_INFO)
    fun getUserInfo(@Body request: UserInfoRequest): Call<UserInfoResponse>

    @POST(PersonalApi.SEND_OLD_REPLACE_CODE)
    fun sendOldPhoneRepaireCode(@Body request: SendLoginCodeRequest): Call<SendLoginCodeResponse>


    @POST(PersonalApi.MODIFY_PHONE_OLD)
    fun sendModifyOldPhone(@Body request: ModifyOldPhoneRequest): Call<SendLoginCodeResponse>

    @POST(PersonalApi.SEND_NEW_REPLACE_CODE)
    fun sendNewRepairedCode(@Body request: SendLoginCodeRequest): Call<SendLoginCodeResponse>

    @POST(PersonalApi.MODIFY_PHONE_NEW)
    fun sendModifyNewPhone(@Body request: ModifyNewPhoneCodeRequest): Call<SendLoginCodeResponse>

    @POST(PersonalApi.MODIFY_LOGIN_PASSWORD)
    fun sendModifyNewLoginPsw(@Body request: ModifyLoginPswRequest): Call<SendLoginCodeResponse>

    @POST(PersonalApi.MODIFY_CAPITAL_PASSWORD)
    fun sendModifyCapitalPsw(@Body request: ModifyCapitalPswRequest): Call<SendLoginCodeResponse>

    @POST(PersonalApi.USER_INFO)
    fun getUserInfoData(@Body request: BaseRequest): Call<GetUserInfoResponse>
}