package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.model.GlobalStockInfo

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/29
 * Desc:
 */
interface GlobalStockView :AbsView{
     fun addIntoData(list: List<GlobalStockInfo>)
}