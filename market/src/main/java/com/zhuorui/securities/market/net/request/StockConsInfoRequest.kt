package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:
 */
class StockConsInfoRequest (val code:String,val pageSize:Int,val sort:Int,val sortType:Int,val ts:String,transaction: String) :BaseRequest(transaction){
    init {
        generateSign()
    }
}