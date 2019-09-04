package com.zhuorui.securities.openaccount.net

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.openaccount.constants.OpenAccountInfo
import com.zhuorui.securities.openaccount.net.api.OpenAccountApi
import com.zhuorui.securities.openaccount.net.request.*
import com.zhuorui.securities.openaccount.net.response.*
import io.reactivex.Observable
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

    //上传电子签名
    @POST(OpenAccountApi.SUB_SIGNATURE)
    fun subSignature(@Body request: SubSignatureRequest): Call<SubSignatureResponse>

    //身份证OCR
    @POST(OpenAccountApi.ID_CARD_OCR)
    fun idCardOcr(@Body request: IdCardOrcRequest): Observable<IdCardOrcResponse>

    //上传身份信息
    @POST(OpenAccountApi.SUB_IDENTITY)
    fun subIdentity(@Body request: SubIdentityRequest): Call<SubIdentityResponse>

    //个人风险测评资料
    @POST(OpenAccountApi.SUB_BASICS_INFO)
    fun subBasicsInfo(@Body request: SubBasicsInfoRequest): Call<SubBasicsInfoResponse>

    //银行卡OCR
    @POST(OpenAccountApi.BANK_OCR)
    fun bankOcr(@Body request: BankOrcRequest): Observable<BankOrcResponse>

    //银行卡三要素认证+一类卡认证
    @POST(OpenAccountApi.BANK_CARD_VERIFICATION)
    fun bankCardVerification(@Body request: BankCardVerificationRequest): Call<BankCardVerificationResponse>

    //上传风险披露情况接口
    @POST(OpenAccountApi.SUB_RISK_DISCLOSURE)
    fun subRiskDisclosure(@Body request: SubRiskDisclosureRequest): Call<SubRiskDisclosureResponse>


}