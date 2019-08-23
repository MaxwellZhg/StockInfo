package com.zhuorui.securities.market.net

import com.zhuorui.securities.market.net.api.StockApi
import com.zhuorui.securities.market.net.request.RecommendStocklistRequest
import com.zhuorui.securities.market.net.request.StockSearchRequest
import com.zhuorui.securities.market.net.response.RecommendStocklistResponse
import com.zhuorui.securities.market.net.response.StockSearchResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IStockNet {

    @POST(StockApi.LIST)
    fun list(@Body request: RecommendStocklistRequest): Call<RecommendStocklistResponse>

    @POST(StockApi.SEARCH_TOPIC)
    fun search(@Body request: StockSearchRequest): Call<StockSearchResponse>

    @POST(StockApi.ADD)
    fun add(@Body request: StockSearchRequest): Call<StockSearchResponse>

}