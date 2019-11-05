package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.event.MarketDetailInfoEvent
import com.zhuorui.securities.market.net.response.MarketNewsListResponse

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:56
 *    desc   :
 */
interface MarketDetailInformationView:AbsView {
    fun addIntoInfoData(list:List<MarketNewsListResponse.DataList>)
    fun changeInfoTypeData(event: MarketDetailInfoEvent)
    fun noMoreData()
    fun loadFailData()
}