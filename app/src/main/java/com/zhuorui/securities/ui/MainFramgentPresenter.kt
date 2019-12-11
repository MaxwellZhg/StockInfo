package com.zhuorui.securities.ui

import com.zhuorui.securities.TokenOverdueEvent
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsEventPresenter
import com.zhuorui.securities.personal.event.LoginStateChangeEvent

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/16 16:07
 *    desc   :
 */
class MainFramgentPresenter : AbsEventPresenter<MainFragmentView, MainFragmentViewModel>() {

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onTokenOverdueEvent(event: TokenOverdueEvent) {
        view?.jumpToLogin()
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onLoginStateChangeEvent(event: LoginStateChangeEvent) {
        if (event.isLogin && event.register) {
            // 不处于主页开户tab
            if (!view?.inOpenAccoutTab()!!) {
                // 弹出引导开户提示框
                view?.showOpenAccountDailog()
            }
        }
    }
}