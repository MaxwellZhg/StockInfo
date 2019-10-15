package com.zhuorui.securities.personal.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/15
 * Desc:
 */
class GetUserInfoDataRequest(transaction: String) :BaseRequest(transaction){
    init {
        generateSign()
    }
}