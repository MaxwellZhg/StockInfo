package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/4
 * Desc:
 */
class MarketNewsListResponse(val data :Data) :BaseResponse(){
    data class Data(
     val list:ArrayList<DataList>
     )
    data class DataList(
        val newsId: Int,
        val createTime: Long,
        val newsTitle: String,
        val newsType: String
    )
}