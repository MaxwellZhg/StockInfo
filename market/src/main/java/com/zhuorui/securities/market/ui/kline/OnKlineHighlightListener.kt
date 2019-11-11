package com.zhuorui.securities.market.ui.kline


/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-11-11 11:32
 *    desc   :
 */
interface OnKlineHighlightListener {
    fun onShowHighlightView(v: IKLineHighlightView)
    fun onHideHighlightView()
    fun onUpHighlightData(obj: Any)
}