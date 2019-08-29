package com.zhuorui.securities.openaccount.ui.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/28
 * Desc:
 */
class LiveRecognRequest (val video:String,val validateCode:String?, val id:String?,  transaction: String) : BaseRequest(transaction) {
    init {
        generateSign()
    }
}