package com.zhuorui.securities.personal.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/21
 * Desc:
 */
interface ForgetPswView :AbsView{
    fun restpsw()
    fun showProgressDailog(type: Int)
    fun showErrorTimes(str: String, type: Int)
    fun changeLoginSendCodeState(type: Int)
}