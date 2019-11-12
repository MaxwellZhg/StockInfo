package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.model.TestNoticeData
import com.zhuorui.securities.market.net.response.MarketBaseInfoResponse

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:56
 *    desc   :
 */
interface MarketDetailNoticeView:AbsView {
    fun addIntoNoticeData(list:List<MarketBaseInfoResponse.Source>)
    fun noMoreData()
}