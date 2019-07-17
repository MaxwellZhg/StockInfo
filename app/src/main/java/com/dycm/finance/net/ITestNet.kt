package com.dycm.finance.net

import com.dycm.finance.net.api.TestApi
import com.dycm.finance.net.request.TestDotradeRequset
import com.dycm.finance.net.response.TestDotradeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ITestNet {

    @POST(TestApi.DOTRADE)
    fun dotrade(@Body request: TestDotradeRequset): Call<TestDotradeResponse>

}