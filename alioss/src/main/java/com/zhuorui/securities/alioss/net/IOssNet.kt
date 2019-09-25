package com.zhuorui.securities.alioss.net

import com.zhuorui.securities.alioss.net.api.OssApi
import com.zhuorui.securities.alioss.net.response.BucketNameResponse
import com.zhuorui.securities.alioss.net.response.TokenResponse
import com.zhuorui.securities.base2app.network.BaseRequest
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-24 16:59
 *    desc   :
 */
interface IOssNet {

    @POST(OssApi.TOKEN)
    fun token(@Body request:BaseRequest): Call<TokenResponse>

    @POST(OssApi.BUCKET_NAME)
    fun bucket_name(): Observable<BucketNameResponse>

}