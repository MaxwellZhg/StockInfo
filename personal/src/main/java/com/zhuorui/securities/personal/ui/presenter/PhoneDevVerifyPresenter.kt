package com.zhuorui.securities.personal.ui.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.zhuorui.commonwidget.common.CountryCodeConfig
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.net.IPersonalNet
import com.zhuorui.securities.personal.net.request.SendLoginCodeRequest
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.ui.view.PhoneDevVerifyView
import com.zhuorui.securities.personal.ui.viewmodel.PhoneDevVerifyViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/12
 * Desc:
 */
class PhoneDevVerifyPresenter(context: Context):AbsNetPresenter<PhoneDevVerifyView,PhoneDevVerifyViewModel>(){

    override fun init() {
        super.init()
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
        if(response.request is SendLoginCodeRequest){
            view?.showProgressDailog(0)
            view?.goNext()
        }
    }

    override fun onErrorResponse(response: ErrorResponse) {
        view?.showProgressDailog(0)
        //网络问题
        if(response.isNetworkBroken){
            ToastUtil.instance.toastCenter(R.string.verify_get_code_error)
            return
        }
        super.onErrorResponse(response)
    }

    fun luanchCall(){
        var intent: Intent =  Intent()
        intent.action = Intent.ACTION_CALL
        intent.data = Uri.parse("tel:" + "400-788-190")
        context?.startActivity(intent)
    }
}