package com.dycm.finance.net.response

import com.dycm.base2app.network.BaseResponse

class TestDotradeResponse(val data: Data) : BaseResponse()

data class Data(
    val transaction_no: String
)