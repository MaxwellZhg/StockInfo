package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.market.model.STPositionData

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-19 16:09
 *    desc   :
 */
class GetPositionResponse(val data: List<STPositionData>?) : BaseResponse() {
}