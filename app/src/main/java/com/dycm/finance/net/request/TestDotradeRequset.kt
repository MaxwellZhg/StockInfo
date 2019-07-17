package com.dycm.finance.net.request

import com.dycm.base2app.network.BaseRequest

class TestDotradeRequset(transaction: String) : BaseRequest(transaction){
    val stockCode = "600001"
    val price = 35.00
    val timeStamp = 1563180576876
    val sign = "dHI8fZN7HJZG4E5AIhYDtAIBp12ByxnF9dg3+j3A0hXlZbMkcFwZpPMbT9TbmGOIEJHd1PgpgXh0rtqD19isrQ=="
}