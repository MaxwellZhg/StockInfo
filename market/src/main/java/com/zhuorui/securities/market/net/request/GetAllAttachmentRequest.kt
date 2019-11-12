package com.zhuorui.securities.market.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/12
 * Desc:
 */
class GetAllAttachmentRequest(val lineId:String,transaction:String) :BaseRequest(transaction){

    init {
        generateSign()
    }
}