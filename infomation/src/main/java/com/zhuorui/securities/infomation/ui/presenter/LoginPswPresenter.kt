package com.zhuorui.securities.infomation.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.infomation.ui.view.LoginPswView
import com.zhuorui.securities.infomation.ui.viewmodel.LoginPswViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class LoginPswPresenter : AbsNetPresenter<LoginPswView, LoginPswViewModel>(){
    override fun init() {
        super.init()
        view?.init()
    }
}