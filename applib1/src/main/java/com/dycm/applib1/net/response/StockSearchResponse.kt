package com.dycm.applib1.net.response

import com.dycm.applib1.model.NetStockInfo
import com.dycm.base2app.network.BaseResponse

class StockSearchResponse(val data: Data) : BaseResponse() {
    data class Data(
        val currentPage: Int,
        val datas: List<NetStockInfo>,
        val pageSize: Int,
        val totalPage: Int,
        val totalRecord: Int
    )
}