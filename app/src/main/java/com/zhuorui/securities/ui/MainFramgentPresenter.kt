package com.zhuorui.securities.ui

import com.zhuorui.securities.TokenOverdueEvent
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsEventPresenter

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/16 16:07
 *    desc   :
 */
class MainFramgentPresenter : AbsEventPresenter<MainFragmentView, MainFragmentViewModel>() {

    override fun init() {
        view?.init()
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onTokenOverdueEvent(event: TokenOverdueEvent) {
        view?.jumpToLogin()
    }
}