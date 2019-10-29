package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/29
 * Desc:
 */
interface ChinaHkStockDetailView :AbsView{
    fun addIntoAllHkStockName(list: List<Int>)
    fun addIntoAllHkContainer(list: List<Int>)
}