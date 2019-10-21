package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.base2app.network.BaseResponse

class StockSearchResponse(val data: List<SearchStockInfo>) : BaseResponse() {

}