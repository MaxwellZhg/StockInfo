package com.dycm.applib1.net

import com.dycm.applib1.net.api.StockApi
import com.dycm.applib1.net.request.StockSearchRequset
import com.dycm.applib1.net.response.StockSearchResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IStockNet {

    @POST(StockApi.SEARCH)
    fun search(@Body request: StockSearchRequset): Call<StockSearchResponse>

}