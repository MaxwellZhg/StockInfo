package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.net.response.StockConsInfoResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/22
 * Desc:
 */
interface MarketPointView :AbsView{
    fun addInfoToAdapter(list: List<StockConsInfoResponse.ListInfo>)
    fun addPointInfoAdapter(list: List<Int>)
}