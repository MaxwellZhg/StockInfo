package com.zhuorui.securities.openaccount.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsPresenter
import com.zhuorui.securities.openaccount.ui.view.OAAuthenticationView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAAuthenticationViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:19
 *    desc   :
 */
class OAAuthenticationPresenter : AbsPresenter<OAAuthenticationView, OAAuthenticationViewModel>() {
    override fun init() {
        super.init()
        view?.init();
    }


}