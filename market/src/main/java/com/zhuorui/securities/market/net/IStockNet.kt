package com.zhuorui.securities.market.net

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.market.net.api.StockApi
import com.zhuorui.securities.market.net.request.*
import com.zhuorui.securities.market.net.response.*
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

    @POST(StockApi.NEWS_LIST)
    fun getMarketNewsList(@Body request: MarketNewsListRequest): Call<MarketNewsListResponse>

    @POST(StockApi.BASE_INFO)
    fun getMarketBaseInfoList(@Body request: MarketBaseInfoRequest): Call<MarketBaseInfoResponse>

    @POST(StockApi.F10_BRIE)
    fun getF10BrieInfo(@Body request: F10BrieRequest): Call<F10BrieResponse>

    @POST(StockApi.SHARE_HOLDER_LIST)
    fun getShareHolderList(@Body request: F10BrieListRequest): Call<F10ShareHolderListResponse>

    @POST(StockApi.DIVIDENT_LIST)
    fun getDividentList(@Body request: F10BrieRequest): Call<F10DividentListResponse>

    @POST(StockApi.REPO_LIST)
    fun getRepoList(@Body request: F10BrieListRequest): Call<F10RepoListResponse>

    @POST(StockApi.GET_ALL_ATTACHMENT)
    fun getAllAttachment(@Body request: GetAllAttachmentRequest): Call<GetAllAttachmentResponse>

    @POST(StockApi.FINANCIAL_REPORT)
    fun getFinancialReport(@Body request: FinancialReportRequest): Call<FinancialReportResponse>

    @POST(StockApi.STOCK_CONS_INFO)
    fun getStockConsInfo(@Body request: StockConsInfoRequest): Call<StockConsInfoResponse>

    @POST(StockApi.GET_CAPITAL_FLOW_TIME)
    fun getCapitalFlowTime(@Body request: GetCapitalFlowTimeRequest):Call<BaseResponse>
}