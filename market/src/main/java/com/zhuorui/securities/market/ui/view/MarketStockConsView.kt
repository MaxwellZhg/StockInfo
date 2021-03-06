package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.model.StockConsInfoModel
import com.zhuorui.securities.market.net.response.StockConsInfoResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:
 */
interface MarketStockConsView :AbsView{
    fun addInfoToAdapter(list: List<StockConsInfoModel>)
    fun showStateInfo(state:Int)
    fun showErrorState()
    fun notifyItemChanged(index:Int)
}