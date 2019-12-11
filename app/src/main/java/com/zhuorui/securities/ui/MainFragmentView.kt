package com.zhuorui.securities.ui

import com.zhuorui.securities.base2app.ui.fragment.AbsView

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/16 16:19
 *    desc   :
 */
interface MainFragmentView : AbsView {

    // 引导开户对话框
    fun showOpenAccountDailog()

    // 是否正处于开户tab
    fun inOpenAccoutTab(): Boolean

    fun jumpToLogin()

}