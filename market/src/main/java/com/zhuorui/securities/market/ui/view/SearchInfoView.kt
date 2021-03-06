package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.event.ChageSearchTabEvent
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.StockPageInfo
import com.zhuorui.securities.market.model.TestSeachDefaultData
import com.zhuorui.securities.market.ui.viewmodel.SearchInfoViewModel
import com.zhuorui.securities.market.ui.viewmodel.StockTabViewModel
import java.util.ArrayList

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/19
 * Desc:
 */
interface SearchInfoView :AbsView{
    fun changeTab(enum: ChageSearchTabEvent)
    fun notifyDataSetChanged(list: TestSeachDefaultData)
    fun notifyAdapter()
}