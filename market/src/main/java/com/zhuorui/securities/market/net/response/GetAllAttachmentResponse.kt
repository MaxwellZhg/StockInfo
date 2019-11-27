package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/12
 * Desc:公告返回response
 * */
class GetAllAttachmentResponse(val data:Data) :BaseResponse(){
    data class Data(
        val sourceList:ArrayList<Source>
    )
    data class Source(
        val attachmentId:String,
        val lineId:String,
        val uploadPath:String,
        val uploadFlag:String,
        val attachmentDesc:String,
        val openPath:String
    )
}