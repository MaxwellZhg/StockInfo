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
        val ts:String,//股票市场(SZ-深圳,SH-上海,HK-港股,US-美股)
        val riseCount:Int,//上涨股票的数量
        val flatPlateCount:Int,// 平盘股票的数量
        val fallCount:Int,//下跌股票的数量
        val rangeList:ArrayList<Range>//股票涨跌分布区间((负无穷,-6],(-6,-3],(-3,0),[0,0],(0,3),[3,6),[6,正无穷))
    )
    data class Range(
        val leftPointValue:Int,//区间左端点值
        val leftPointOpen:Int,//区间左端点开闭情况(0为开，1为闭)
        val rightPointValue:Int,//区间右端点值
        val rightPointOpen:Int,//区间右端点开闭情况(0为开，1为闭)
        val stockCount:Int//涨跌幅在该区间的股票数量
    )
}