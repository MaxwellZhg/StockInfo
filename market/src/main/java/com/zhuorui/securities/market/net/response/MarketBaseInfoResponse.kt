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
     val sourceList:ArrayList<Source>//公告list
    )

    data class Source(
        val lineId:String,//公告编号
        val attachmentId:String,//附件编号
        val headLine:String,//公告标题
        val publishDate:Long,// 发布日期
        val languageId:Int//语言编号(0-簡體中文  1-英語  2-香港繁體中文  3-台灣繁體中文)
    )

}