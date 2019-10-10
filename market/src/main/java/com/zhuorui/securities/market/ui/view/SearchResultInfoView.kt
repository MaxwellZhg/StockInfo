package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/20
 * Desc:
 */
interface SearchResultInfoView :AbsView{
  fun init()
  fun initonlazy()
  fun detailInfo(str:String)
  fun detailStock(str:String)
  fun detailStockInfo(str:String)
}