package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-11-29 15:46
 *    desc   : 按时间查询资金统计返回数据
 */
class GetCapitalFlowTimeResponse(var data: List<Data>?) : BaseResponse() {

    data class Data(var str: String?)
}