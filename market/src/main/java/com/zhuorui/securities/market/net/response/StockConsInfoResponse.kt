package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import java.math.BigDecimal

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:
 */
class StockConsInfoResponse (val data:Data):BaseResponse(){
    data class Data(
        val list: ArrayList<ListInfo>,
        val total:BigDecimal,
        val pageSize:BigDecimal,
        val currentPage:BigDecimal
    )
    data class ListInfo(
        val ts:String,
        val code:String,
        val name:String,
        val lastPrice:BigDecimal,
        val diffRate:BigDecimal,
        val turnover:BigDecimal
    )
}