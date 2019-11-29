package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.net.response.MarketNewsListResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:
 */
interface MarketPointConsInfoView :AbsView{
    fun addIntoInfoData(list:List<MarketNewsListResponse.DataList>)
    fun noMoreData()
    fun loadFailData()
}