package com.zhuorui.securities.personal.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.ui.fragment.AbsPresenter
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.net.IPersonalNet
import com.zhuorui.securities.personal.net.request.UserLoginOutRequest
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.ui.view.MyTabVierw
import com.zhuorui.securities.personal.ui.viewmodel.MyTabVierwModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/27 14:13
 *    desc   :
 */
class MyTabPresenter : AbsNetPresenter<MyTabVierw, MyTabVierwModel>() {

    override fun init() {
        super.init()
    }

    fun getLoginStatus(): Boolean {
        return LocalAccountConfig.read().isLogin()
    }
    fun requestUserLoginOut() {
        val request = UserLoginOutRequest(transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.userLoginOut(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onUserLoginOutResponse(response: SendLoginCodeResponse) {
        if (response.request is UserLoginOutRequest) {
            if (LocalAccountConfig.read().saveLogin(
                    "",
                    "",
                    ""
                )
            ) {
                view?.gotomain()
            }
        }
    }


}