package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/29
 * Desc:
 */
interface GlobalStockView :AbsView{
     fun addIntoCoustomData(list: List<Int>)
     fun addIntoUsaData(list: List<Int>)
     fun addIntoEnuData(list: List<Int>)
     fun addIntoAsiaData(list: List<Int>)
}