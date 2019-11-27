package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/26
 * Desc:市场涨跌概况返回
 * */
class MarketStatisticsResponse (val data :Data) :BaseResponse(){
    data class Data(
        val ts:String,
        val riseCount:Int,
        val flatPlateCount:Int,
        val fallCount:Int,
        val rangeList:ArrayList<Range>
    )
    data class Range(
        val leftPointValue:Int,
        val leftPointOpen:Int,
        val rightPointValue:Int,
        val rightPointOpen:Int,
        val stockCount:Int
    )
}