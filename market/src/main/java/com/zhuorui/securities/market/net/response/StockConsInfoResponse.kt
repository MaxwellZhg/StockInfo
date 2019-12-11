package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import java.math.BigDecimal

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:自选股返回response
 * 
 * */
class StockConsInfoResponse (val data:Data):BaseResponse(){
    data class Data(
        val list: ArrayList<ListInfo>,
        val total:BigDecimal,//总条数
        val pageSize:BigDecimal,//查询条数
        val currentPage:BigDecimal//当前页
    )
    data class ListInfo(
        val ts:String,//属于的股票市场(SZ-深圳,SH-上海,HK-港股,US-美股)
        val code:String,//股票代码
        val name:String,//股票名称
        var last:BigDecimal,//最新价
        var diffRate:BigDecimal,//涨跌幅
        var turnover:BigDecimal//成交额
    )
}