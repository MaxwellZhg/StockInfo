package com.zhuorui.securities.openaccount.ui.net

import com.zhuorui.securities.openaccount.ui.net.api.OpenAccountApi
import com.zhuorui.securities.openaccount.ui.net.request.LiveCodeRequest
import com.zhuorui.securities.openaccount.ui.net.request.LiveRecognRequest
import com.zhuorui.securities.openaccount.ui.net.request.OpenInfoRequest
import com.zhuorui.securities.openaccount.ui.net.response.LiveCodeResponse
import com.zhuorui.securities.openaccount.ui.net.response.LiveRecognResponse
import com.zhuorui.securities.openaccount.ui.net.response.OpenInfoResponse
import com.zhuorui.securities.personal.net.api.PersonalApi
import com.zhuorui.securities.personal.net.request.SendLoginCodeRequest
import com.zhuorui.securities.personal.net.request.UserLoginCodeRequest
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.net.response.UserLoginCodeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/28
 * Desc:
 */
interface IOpenAccountNet {
    //获取开户信息
    @POST(OpenAccountApi.GET_OPEN_INFO)
    fun getOpenInfo(@Body request: OpenInfoRequest): Call<OpenInfoResponse>
    //获取视频验证码
    @POST(OpenAccountApi.GET_LIVE_CODE)
    fun getLiveCode(@Body request: LiveCodeRequest): Call<LiveCodeResponse>
    //获取上传活体检测
    @POST(OpenAccountApi.LIVENESS_RECOGNITION)
    fun getLiveRecogn(@Body request: LiveRecognRequest): Call<LiveRecognResponse>
}