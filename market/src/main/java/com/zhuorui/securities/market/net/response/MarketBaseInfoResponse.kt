package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/6
 * Desc:公告消息返回
 * */
class MarketBaseInfoResponse(val data:Data) :BaseResponse(){
    data class Data(
     val sourceList:ArrayList<Source>
    )

    data class Source(
        val lineId:String,
        val attachmentId:String,
        val headLine:String,
        val publishDate:Long,
        val languageId:Int
    )

}