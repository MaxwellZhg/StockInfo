package com.zhuorui.securities.infomation.ui.presenter
import com.zhuorui.securities.base2app.ui.fragment.AbsEventPresenter
import com.zhuorui.securities.infomation.config.LocalAccountConfig
import com.zhuorui.securities.infomation.ui.view.OpenAccountTabView
import com.zhuorui.securities.infomation.ui.viewmodel.OpenAccountTabViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class OpenAccountTabPresenter:AbsEventPresenter<OpenAccountTabView,OpenAccountTabViewModel>(){
    override fun init() {
        super.init()
        view?.init()
    }
    fun getLoginStatus() :Boolean{
        return  LocalAccountConfig.read().isLogin()
    }
}