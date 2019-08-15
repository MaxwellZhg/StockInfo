package com.zhuorui.securities.applib1.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

class StockSearchRequest(val keyword: String, val currentPage: Int, val pageSize: Int, transaction: String) :
    BaseRequest(transaction){

    init {
        generateSign()
    }
}