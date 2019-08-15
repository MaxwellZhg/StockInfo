package com.zhuorui.securities.applib1.net.response

import com.zhuorui.securities.applib1.model.SearchStockInfo
import com.zhuorui.securities.base2app.network.BaseResponse

class StockSearchResponse(val data: Data) : BaseResponse() {
    data class Data(
        val currentPage: Int,
        val datas: List<SearchStockInfo>,
        val pageSize: Int,
        val totalPage: Int,
        val totalRecord: Int
    )
}