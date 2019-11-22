package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.socket.vo.IndexPonitHandicapData

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/17
 * Desc:
 */
interface HkStockDetailView :AbsView{
    fun addInfoToAdapter(list: List<Int>)
    fun setHsiIndexData(list: List<IndexPonitHandicapData?>)
}