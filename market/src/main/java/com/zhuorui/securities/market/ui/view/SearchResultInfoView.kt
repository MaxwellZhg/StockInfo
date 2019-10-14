package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.model.SearchDeafaultData
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.TestSeachDefaultData

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

  fun addInfoToAdapter(list: List<Int>?,totalPage: Int)

  fun addStockToAdapter(list: List<SearchStockInfo>?,totalPage:Int)

  fun addAllToAdapter(list: List<SearchDeafaultData>?)

  fun notifyAdapter()

  fun showEmpty()
  fun hideEmpty()

  fun showloadMoreFail()

}