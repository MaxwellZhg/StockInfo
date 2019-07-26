package com.dycm.applib1.net.request

import com.dycm.base2app.network.BaseRequest

class StockSearchRequset(val keyword: String, val currentPage: Int, val pageSize: Int, transaction: String) :
    BaseRequest(transaction)