package com.zhuorui.securities.personal.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/11
 * Desc:
 */
interface ChangeTradePassView :AbsView{
  fun gotomain()
  fun showProgressDailog(type:Int)
}