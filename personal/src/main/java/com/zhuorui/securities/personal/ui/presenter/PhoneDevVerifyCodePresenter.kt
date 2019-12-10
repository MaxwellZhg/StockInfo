package com.zhuorui.securities.personal.ui.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.zhuorui.commonwidget.common.CountryCodeConfig
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.event.LoginStateChangeEvent
import com.zhuorui.securities.personal.net.IPersonalNet
import com.zhuorui.securities.personal.net.request.GetUserInfoDataRequest
import com.zhuorui.securities.personal.net.request.SendLoginCodeRequest
import com.zhuorui.securities.personal.net.request.UserLoginCodeRequest
import com.zhuorui.securities.personal.net.response.GetUserInfoResponse
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.net.response.UserLoginCodeResponse
import com.zhuorui.securities.personal.ui.view.PhoneDevVerifyCodeView
import com.zhuorui.securities.personal.ui.viewmodel.PhoneDevVerifyCodeViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/12
 * Desc:
 */
class PhoneDevVerifyCodePresenter(context:Context) :AbsNetPresenter<PhoneDevVerifyCodeView,PhoneDevVerifyCodeViewModel>() {
    private var recLen = 60//跳过倒计时提示5秒
    private var disposable: Disposable?=null



    fun startTask() {
        disposable = Observable.interval(0,1,  TimeUnit.SECONDS).take(61)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                recLen--
                viewModel?.str?.set(recLen.toString()+"s")
                if(recLen<0) {
                    viewModel?.str?.set(ResUtil.getString(R.string.send_verification_code))
                    view?.changeLoginSendCodeState(0)
                }
            }
    }






    fun luanchCall(){
        var intent: Intent =  Intent()
        intent.action = Intent.ACTION_CALL
        intent.data = Uri.parse("tel:" + "400-788-190")
        context?.startActivity(intent)
    }

    fun requestUserLoginCode(str:String?,vfcode:String?,phoneArea:String?) {
        view?.showProgressDailog(1)
        val request = UserLoginCodeRequest(str, vfcode,phoneArea, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.userLoginCode(request)
            ?.enqueue(Network.IHCallBack<UserLoginCodeResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onUserLoginCodeResponse(response: UserLoginCodeResponse) {
        if (response.request is UserLoginCodeRequest) {
            view?.showProgressDailog(0)
            if (LocalAccountConfig.getInstance().saveLogin(
                    response.data.userId,
                    response.data.phone,
                    response.data.token
                )
            ) {
               getUserInfoData()
            }
        }
    }


    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
        view?.showProgressDailog(0)
    }
    fun getUserInfoData(){
        val request = GetUserInfoDataRequest(transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.getUserInfoData(request)
            ?.enqueue(Network.IHCallBack<GetUserInfoResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onGetUserInfoDataResponse(response: GetUserInfoResponse) {
        if (!transactions.isMyTransaction(response)) return
        LocalAccountConfig.getInstance().setZrNo(response.data.zrNo)
        view?.gotomain()
        // 通知登录状态发生改变
        RxBus.getDefault().post(LoginStateChangeEvent(true))
    }

    fun requestSendLoginCode(str: kotlin.String) {
        val request = SendLoginCodeRequest(str, CountryCodeConfig.read().defaultCode, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.sendLoginCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendLoginCodeResponse(response: SendLoginCodeResponse) {
        if (!transactions.isMyTransaction(response)) return
        if (response.request is SendLoginCodeRequest) {
            view?.changeLoginSendCodeState(1)
            startTask()
        }
    }

    override fun destroy() {
        super.destroy()

    }


}