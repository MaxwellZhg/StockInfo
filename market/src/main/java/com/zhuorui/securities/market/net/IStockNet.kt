package com.zhuorui.securities.market.net

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.market.net.api.StockApi
import com.zhuorui.securities.market.net.request.*
import com.zhuorui.securities.market.net.response.RecommendStocklistResponse
import com.zhuorui.securities.market.net.response.StockSearchResponse
import com.zhuorui.securities.market.net.response.SynStockResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IStockNet {

    @POST(StockApi.MY_LIST)
    fun myList(@Body request: RecommendStocklistRequest): Call<RecommendStocklistResponse>

    @POST(StockApi.SEARCH_TOPIC)
    fun search(@Body request: StockSearchRequest): Call<StockSearchResponse>

    @POST(StockApi.ADD)
    fun collection(@Body request: CollectionStockRequest): Call<BaseResponse>

    @POST(StockApi.DEL)
    fun delelte(@Body request: DeleteStockRequest): Call<BaseResponse>

    @POST(StockApi.TOP)
    fun stickyOnTop(@Body request: StickyOnTopStockRequest): Call<BaseResponse>

    @POST(StockApi.SYN)
    fun synStock(@Body request: SynStockRequest): Call<SynStockResponse>
}