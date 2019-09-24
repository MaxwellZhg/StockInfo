package com.zhuorui.securities.market.net

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.market.net.api.SimulationTradeApi
import com.zhuorui.securities.market.net.request.*
import com.zhuorui.securities.market.net.response.FeeComputeResponse
import com.zhuorui.securities.market.net.response.GetFeeTemplateResponse
import com.zhuorui.securities.market.net.response.GetStockInfoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/16 10:38
 *    desc   : 模拟炒股
 */
interface ISimulationTradeNet {

    @POST(SimulationTradeApi.STOCK_INFO)
    fun getStockInfo(@Body request: GetStockInfoRequest): Call<GetStockInfoResponse>

    @POST(SimulationTradeApi.UPDATE_STOCK_DATA)
    fun updateStockData(@Body request: UpdateStockDataRequest): Call<BaseResponse>

    @POST(SimulationTradeApi.FEE_COMPUTE)
    fun feeCompute(@Body request: FeeComputeRequest): Call<FeeComputeResponse>

    @POST(SimulationTradeApi.FEE_TEMPLATE)
    fun feeTemplate(@Body request: GetFeeTemplateRequest): Call<GetFeeTemplateResponse>

    @POST(SimulationTradeApi.STOCK_BUY)
    fun stockBuy(@Body request: StockBuyRequest): Call<BaseResponse>
}