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
import java.util.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/12
 * Desc:
 */
class PhoneDevVerifyCodePresenter(context:Context) :AbsNetPresenter<PhoneDevVerifyCodeView,PhoneDevVerifyCodeViewModel>() {
    internal var timer: Timer? = null
    private var recLen = 60//跳过倒计时提示5秒
    internal var task: TimerTask? = null
    override fun init() {
        super.init()
    }

    @Throws(InterruptedException::class)
    fun startTask() {
        if (task == null) {
            timer = Timer()
            task = object : TimerTask() {
                override fun run() {
                    recLen--
                    viewModel?.str?.set(recLen.toString()+"s")
                    if (recLen < 0) {
                        timer!!.cancel()
                        task = null
                        timer = null
                        viewModel?.str?.set(ResUtil.getString(R.string.send_verification_code))
                        viewModel?.getCodeClickState?.set(0)
                    }
                }
            }
        }
        timer!!.schedule(task, 1000, 1000)
    }

    fun startTimeCountDown() {
        if (recLen == 60) {
            try {
                startTask()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        } else if (recLen < 0) {
            recLen = 60
            try {
                startTask()
            } catch (e: InterruptedException) {
                e.printStackTrace()
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
        view?.showProgressDailog(1)
        val request = SendLoginCodeRequest(str, CountryCodeConfig.read().defaultCode, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.sendLoginCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendLoginCodeResponse(response: SendLoginCodeResponse) {
        if (!transactions.isMyTransaction(response)) return
        if (response.request is SendLoginCodeRequest) {
            view?.showProgressDailog(0)
            setGetCodeClickState(1)
            startTimeCountDown()
        }
    }

    fun setGetCodeClickState(state:Int){
        viewModel?.getCodeClickState?.set(state)
    }
}