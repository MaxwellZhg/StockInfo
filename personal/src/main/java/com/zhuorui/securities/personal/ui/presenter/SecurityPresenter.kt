package com.zhuorui.securities.personal.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
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

 /*   fun getUserInfo(){
        val request = UserInfoRequest(transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.getUserInfo(request)
            ?.enqueue(Network.IHCallBack<UserInfoResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onUserInfoResponse(response: UserInfoResponse) {

    }*/

}