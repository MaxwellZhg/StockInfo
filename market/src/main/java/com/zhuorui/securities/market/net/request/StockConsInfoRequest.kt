package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:成分股请求
 * */
class StockConsInfoRequest (val code:String,val pageSize:Int,val sort:Int,//排序id
                            val sortType:Int,//涨跌类型
                            val ts:String,transaction: String) :BaseRequest(transaction){
    init {
        generateSign()
    }
}