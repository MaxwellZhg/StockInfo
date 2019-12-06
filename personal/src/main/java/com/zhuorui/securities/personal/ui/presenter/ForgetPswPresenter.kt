package com.zhuorui.securities.personal.ui.presenter

import android.content.Context
import android.text.InputFilter
import android.text.TextUtils
import android.widget.EditText
import com.zhuorui.commonwidget.StateButton
import com.zhuorui.commonwidget.common.CountryCodeConfig
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.net.IPersonalNet
import com.zhuorui.securities.personal.net.request.SendLoginCodeRequest
import com.zhuorui.securities.personal.net.request.VerifForgetCodeRequest
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.ui.view.ForgetPswView
import com.zhuorui.securities.personal.ui.viewmodel.ForgetPswViewModel
import com.zhuorui.securities.personal.util.PatternUtils
import java.util.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/21
 * Desc:
 */
class ForgetPswPresenter(context: Context) : AbsNetPresenter<ForgetPswView,ForgetPswViewModel>(){
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
                        viewModel?.getcodeState?.set(1)
                        view?.changeLoginSendCodeState(0)
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

    fun requestSendForgetCode(str: kotlin.String) {
        view?.showProgressDailog(1)
        val request = SendLoginCodeRequest(str, CountryCodeConfig.read().defaultCode, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.sendForgetPwdCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendForgetCodeResponse(response: SendLoginCodeResponse) {
        if (!transactions.isMyTransaction(response)) return
        if(response.request is SendLoginCodeRequest){
            view?.showProgressDailog(0)
            view?.changeLoginSendCodeState(1)
            startTimeCountDown()
        }else if(response.request is VerifForgetCodeRequest){
            view?.showProgressDailog(0)
            view?.restpsw()
        }
    }

    override fun onErrorResponse(response: ErrorResponse) {
        view?.showProgressDailog(0)
        if (response.request is SendLoginCodeRequest) {
            if(response.code == "030002"){
               // 请求验证超次数
                view?.showErrorTimes("",1)
                return
            }else if(response.isNetworkBroken){
                //网络错误
                ToastUtil.instance.toastCenter(R.string.verify_get_code_error)
                return
            }
        }else if(response.request is VerifForgetCodeRequest){
             ToastUtil.instance.toastCenter(R.string.verify_code_error)
            return
        }
        super.onErrorResponse(response)
    }

    fun requestVerifyForgetCode(str: kotlin.String,code:kotlin.String){
        view?.showProgressDailog(1)
        val request = VerifForgetCodeRequest(str, code, CountryCodeConfig.read().defaultCode,transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.verifyForgetCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }



    fun getGetCodeColor(state: Int) {
        viewModel?.getcodeState?.set(state)
    }

    fun detailChangeCodeState(code:String, et_phone: EditText, et_code: EditText, btn_login: StateButton){
        if(code == "+86"){
            et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
        }else{
            et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
        }
        if(!TextUtils.isEmpty(et_phone.text.toString())&&!TextUtils.isEmpty(et_code.text.toString())){
            if(code == "+86"){
                val matcher = PatternUtils.patternZhPhone(et_phone.text.toString())
                if(matcher) {
                    getGetCodeColor(1)
                    view?.changeLoginSendCodeState(0)
                    btn_login.isEnabled = true
                }else{
                    getGetCodeColor(0)
                    view?.changeLoginSendCodeState(1)
                    btn_login.isEnabled = false
                }
            }else{
                val matcher = PatternUtils.patternOtherPhone(et_phone.text.toString())
                if(matcher) {
                    getGetCodeColor(1)
                    view?.changeLoginSendCodeState(0)
                    btn_login.isEnabled = true
                }else{
                    getGetCodeColor(0)
                    view?.changeLoginSendCodeState(1)
                    btn_login.isEnabled = false
                }
            }
        }else if(!TextUtils.isEmpty(et_phone.text.toString())&& TextUtils.isEmpty(et_code.text.toString())){
            if(code == "+86"){
                val matcher = PatternUtils.patternZhPhone(et_phone.text.toString())
                if(matcher) {
                    getGetCodeColor(1)
                    view?.changeLoginSendCodeState(0)
                    btn_login.isEnabled = false
                }else{
                    getGetCodeColor(0)
                    view?.changeLoginSendCodeState(1)
                    btn_login.isEnabled = false
                }
            }else{
                val matcher = PatternUtils.patternOtherPhone(et_phone.text.toString())
                if(matcher) {
                    getGetCodeColor(1)
                    view?.changeLoginSendCodeState(0)
                    btn_login.isEnabled = false
                }else{
                    getGetCodeColor(0)
                    view?.changeLoginSendCodeState(1)
                    btn_login.isEnabled = false
                }
            }
        }else if(TextUtils.isEmpty(et_phone.text.toString())&&!TextUtils.isEmpty(et_code.text.toString())){
            getGetCodeColor(0)
            view?.changeLoginSendCodeState(1)
            btn_login.isEnabled=false
        }else if(TextUtils.isEmpty(et_phone.text.toString())&& TextUtils.isEmpty(et_code.text.toString())){
            getGetCodeColor(0)
            view?.changeLoginSendCodeState(1)
            btn_login.isEnabled=false
        }
    }

}