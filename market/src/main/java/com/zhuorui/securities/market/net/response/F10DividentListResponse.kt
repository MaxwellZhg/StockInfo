package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.market.model.F10DividendModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/8 16:25
 *    desc   : F10获取分红派息列表
 */
class F10DividentListResponse : BaseResponse() {

    var data: List<F10DividendModel>? = null
}