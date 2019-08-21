package com.zhuorui.securities.infomation.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.infomation.net.InfomationNet
import com.zhuorui.securities.infomation.net.request.SendLoginCodeRequest
import com.zhuorui.securities.infomation.net.request.UserLoginPwdRequest
import com.zhuorui.securities.infomation.net.response.SendLoginCodeResponse
import com.zhuorui.securities.infomation.net.response.UserLoginCodeResponse
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
    fun requestLoginPwd(phone: kotlin.String,password: kotlin.String,phoneArea:kotlin.String) {
        val request = UserLoginPwdRequest(phone, password,"0086", transactions.createTransaction())
        Cache[InfomationNet::class.java]?.userLoginByPwd(request)
            ?.enqueue(Network.IHCallBack<UserLoginCodeResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onUserLoginPwdResponse(response: UserLoginCodeResponse) {
         view?.gotomain()
    }

}