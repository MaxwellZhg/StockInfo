package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/4
 * Desc: 个股行情资讯
 */
class MarketNewsListRequest (val code:String,val currentPage:Int,val pageSize:Int, transaction: String):BaseRequest(transaction){
    init {
        generateSign()
    }
}