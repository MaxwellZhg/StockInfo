package com.zhuorui.securities.infomation.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.ui.fragment.AbsPresenter
import com.zhuorui.securities.infomation.net.InfomationNet
import com.zhuorui.securities.infomation.net.request.SendLoginCodeRequest
import com.zhuorui.securities.infomation.net.request.UserLoginCodeRequest
import com.zhuorui.securities.infomation.net.request.UserLoginOutRequest
import com.zhuorui.securities.infomation.net.response.SendLoginCodeResponse
import com.zhuorui.securities.infomation.net.response.UserLoginCodeResponse
import com.zhuorui.securities.infomation.ui.SettingPswFragment
import com.zhuorui.securities.infomation.ui.view.LoginRegisterView
import com.zhuorui.securities.infomation.ui.viewmodel.LoginRegisterViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class LoginRegisterPresenter : AbsNetPresenter<LoginRegisterView, LoginRegisterViewModel>(){
    override fun init() {
        super.init()
        view?.init()
    }


    fun requestSendLoginCode(str: kotlin.String) {
        val request = SendLoginCodeRequest(str, "0086", transactions.createTransaction())
        Cache[InfomationNet::class.java]?.sendLoginCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }

    fun requestUserLoginCode(str: kotlin.String,vfcode:kotlin.String) {
        val request = UserLoginCodeRequest(str, vfcode,"0086", transactions.createTransaction())
        Cache[InfomationNet::class.java]?.userLoginCode(request)
            ?.enqueue(Network.IHCallBack<UserLoginCodeResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendLoginCodeResponse(response: SendLoginCodeResponse) {
        view?.countdown()
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onUserLoginCodeResponse(response: UserLoginCodeResponse) {
        view?.gotomain()
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onUserLoginOutResponse(response: SendLoginCodeResponse) {
        view?.gotomain()
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onErrorRes(response: ErrorResponse) {
        if (response.request is UserLoginCodeRequest) {
            if(response.code=="010003"){
                view?.gotopsw()
            }
            if(response.code=="010002"){
                view?.gotopsw()
            }
            if(response.code=="010001"){
                view?.gotopsw()
            }
        }
    }

    fun requestUserLoginOut() {
        val request = UserLoginOutRequest("",transactions.createTransaction())
        Cache[InfomationNet::class.java]?.userLoginOut(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }
    fun setState(value:Int){
        viewModel!!.state.set(value)
    }
}