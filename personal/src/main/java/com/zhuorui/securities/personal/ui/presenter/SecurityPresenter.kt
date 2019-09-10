package com.zhuorui.securities.personal.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.personal.net.IPersonalNet
import com.zhuorui.securities.personal.net.request.UserInfoRequest
import com.zhuorui.securities.personal.net.request.UserLoginOutRequest
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.net.response.UserInfoResponse
import com.zhuorui.securities.personal.ui.view.SecurityView
import com.zhuorui.securities.personal.ui.viewmodel.SecurityViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/9
 * Desc:
 */
class SecurityPresenter : AbsNetPresenter<SecurityView,SecurityViewModel>(){
    override fun init() {
        super.init()
    }

    fun getUserInfo(){
        val request = UserInfoRequest(transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.getUserInfo(request)
            ?.enqueue(Network.IHCallBack<UserInfoResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onUserInfoResponse(response: UserInfoResponse) {

    }

}