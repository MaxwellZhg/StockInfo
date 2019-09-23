package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
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
    fun initViewPager(fragments: ArrayList<SearchInfoViewModel.StockPageInfo>,str:String)
}