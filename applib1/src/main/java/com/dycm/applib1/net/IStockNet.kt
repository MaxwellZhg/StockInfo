package com.dycm.applib1.net

import com.dycm.applib1.net.api.StockApi
import com.dycm.applib1.net.request.RecommendStocklistRequest
import com.dycm.applib1.net.request.StockSearchRequest
import com.dycm.applib1.net.response.RecommendStocklistResponse
import com.dycm.applib1.net.response.StockSearchResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IStockNet {

    @POST(StockApi.LIST)
    fun list(@Body request: RecommendStocklistRequest): Call<RecommendStocklistResponse>

    @POST(StockApi.SEARCH)
    fun search(@Body request: StockSearchRequest): Call<StockSearchResponse>

}