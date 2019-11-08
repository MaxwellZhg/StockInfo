package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/6
 * Desc: 公告列表
 */
class MarketBaseInfoRequest (val secCode:String,val currentPage:Int,val pageNum:Int,  transaction: String):BaseRequest(transaction){
    init {
        generateSign()
    }
}