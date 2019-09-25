package com.zhuorui.securities.market.net

import com.zhuorui.securities.base2app.network.BaseRequest
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.market.net.api.SimulationTradeApi
import com.zhuorui.securities.market.net.request.*
import com.zhuorui.securities.market.net.response.*
import io.reactivex.Observable
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

    @POST(SimulationTradeApi.FUND_ACCOUNT)
    fun getFundAccount(@Body request: FundAccountRequest): Observable<FundAccountResponse>

    @POST(SimulationTradeApi.CREATE_FUND_ACCOUNT)
    fun createFundAccount(@Body request: FundAccountRequest): Observable<BaseResponse>

    @POST(SimulationTradeApi.STOCK_INFO)
    fun getStockInfo(@Body request: GetStockInfoRequest): Call<GetStockInfoResponse>

    @POST(SimulationTradeApi.UPDATE_STOCK_DATA)
    fun updateStockData(@Body request: UpdateStockDataRequest): Call<BaseResponse>

    @POST(SimulationTradeApi.FEE_COMPUTE)
    fun feeCompute(@Body request: FeeComputeRequest): Call<FeeComputeResponse>

    @POST(SimulationTradeApi.GET_POSITION)
    fun getPosition(@Body request: GetPositionRequest): Call<GetPositionResponse>

    @POST(SimulationTradeApi.ORDER_LIST)
    fun orderList(@Body request: OrderListRequest): Call<OrderListResponse>

    @POST(SimulationTradeApi.STOCKS_INFO)
    fun getStocksInfo(@Body request: BaseRequest): Call<GetStocksInfoResponse>

    @POST(SimulationTradeApi.FEE_TEMPLATE)
    fun feeTemplate(@Body request: GetFeeTemplateRequest): Call<GetFeeTemplateResponse>

    @POST(SimulationTradeApi.STOCK_BUY)
    fun stockBuy(@Body request: StockBuyRequest): Call<BaseResponse>

    @POST(SimulationTradeApi.CANCEL_BUY_TRUST)
    fun cancelBuyTrust(@Body request: CancelTrustRequest): Call<BaseResponse>

    @POST(SimulationTradeApi.CANCEL_SELL_TRUST)
    fun cancelSellTrust(@Body request: CancelTrustRequest): Call<BaseResponse>

}