package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/4
 * Desc:资讯列表返回response
 * */
class MarketNewsListResponse(val data :Data) :BaseResponse(){
    data class Data(
     val list:ArrayList<DataList>
     )
    data class DataList(
        val newsId: Int,//新闻id
        val createTime: Long,//发布时间
        val newsTitle: String,//新闻标题
        val newsType: String//新闻类型
    )
}