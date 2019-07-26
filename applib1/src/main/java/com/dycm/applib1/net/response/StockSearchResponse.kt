package com.dycm.applib1.net.response

import com.dycm.applib1.model.NetStockInfo
import com.dycm.base2app.network.BaseResponse

class StockSearchResponse(val data: List<NetStockInfo>) : BaseResponse()