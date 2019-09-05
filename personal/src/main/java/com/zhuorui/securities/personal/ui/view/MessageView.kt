package com.zhuorui.securities.personal.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.personal.ui.model.MessageModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/5 11:23
 *    desc   :
 */
interface MessageView : AbsView {

    fun init()

    fun notifyDataSetChanged(list: List<MessageModel>?)
}