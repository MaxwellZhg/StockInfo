package com.zhuorui.securities.infomation.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.ui.fragment.AbsPresenter
import com.zhuorui.securities.infomation.net.InfomationNet
import com.zhuorui.securities.infomation.net.request.UserLoginCodeRequest
import com.zhuorui.securities.infomation.net.request.UserLoginrRegisterRequest
import com.zhuorui.securities.infomation.net.response.SendLoginCodeResponse
import com.zhuorui.securities.infomation.net.response.UserLoginCodeResponse
import com.zhuorui.securities.infomation.ui.config.LocalLoginResConfig
import com.zhuorui.securities.infomation.ui.view.SettingPswView
import com.zhuorui.securities.infomation.ui.viewmodel.SettingPswViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class SettingPswPresenter :AbsNetPresenter<SettingPswView,SettingPswViewModel>() {
    override fun init() {
        super.init()
        view?.init()
    }

    fun requestUserLoginPwdCode(pwd: kotlin.String, code: kotlin.String, phone: kotlin.String) {
        val request = UserLoginrRegisterRequest(pwd, code, phone, "0086", transactions.createTransaction())
        Cache[InfomationNet::class.java]?.userPwdCode(request)
            ?.enqueue(Network.IHCallBack<UserLoginCodeResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendLoginPwdResponse(response: UserLoginCodeResponse) {
        if (response.request is UserLoginrRegisterRequest) {
            if (response.code == "000000") {
                if (LocalLoginResConfig.read().add(response)) {
                    view?.showDialog()
                }
            }
        }
    }
}