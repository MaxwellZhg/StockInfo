package com.zhuorui.securities.market.ui.kline

import android.view.View

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-11-11 11:27
 *    desc   :
 */
interface IKLineHighlightView {

    fun setData(obj:Any)

    fun getView(): View

}