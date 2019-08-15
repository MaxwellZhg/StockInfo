package com.zhuorui.securities.applib1.net.response

import com.zhuorui.securities.applib1.model.StockMarketInfo
import com.zhuorui.securities.base2app.network.BaseResponse

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/8 13:55
 *    desc   : 获取自选股列表推荐数据
 */
class RecommendStocklistResponse : BaseResponse() {
    var data: Data? = null

    data class Data(
        val currentPage: Int,
        val datas: List<StockMarketInfo>,
        val pageSize: Int,
        val totalPage: Int,
        val totalRecord: Int
    )
}