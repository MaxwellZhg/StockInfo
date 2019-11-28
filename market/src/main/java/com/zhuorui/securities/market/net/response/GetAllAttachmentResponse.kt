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
        val attachmentId:String,//附件Id
        val lineId:String,//附件所属公告Id
        val uploadPath:String,// 存在于服务器的路径
        val uploadFlag:String,//是否已经下载到服务器
        val attachmentDesc:String,//附件的标题描述
        val openPath:String//附件打开的URL地址
    )
}