package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.model.TopicStockModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/19 17:08
 *    desc   :
 */
interface TopicStockListView : AbsView {

    fun init()

    fun notifyDataSetChanged(list: List<TopicStockModel>?)

    fun notifyItemChanged(index: Int)

    fun hideRegisterNow()

    fun showRetry(visible: Boolean)
}