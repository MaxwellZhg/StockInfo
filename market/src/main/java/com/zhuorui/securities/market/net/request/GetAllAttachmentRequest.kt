package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/12
 * Desc:公共请求
 * */
class GetAllAttachmentRequest(val lineId:String,//公告id
                              transaction:String) :BaseRequest(transaction){

    init {
        generateSign()
    }
}