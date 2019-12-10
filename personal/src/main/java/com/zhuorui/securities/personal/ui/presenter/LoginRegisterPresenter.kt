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
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.event.LoginStateChangeEvent
import com.zhuorui.securities.personal.event.MyTabInfoEvent
import com.zhuorui.securities.personal.net.IPersonalNet
import com.zhuorui.securities.personal.net.request.GetUserInfoDataRequest
import com.zhuorui.securities.personal.net.request.SendLoginCodeRequest
import com.zhuorui.securities.personal.net.request.UserLoginCodeRequest
import com.zhuorui.securities.personal.net.response.GetUserInfoResponse
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.net.response.UserLoginCodeResponse
import com.zhuorui.securities.personal.ui.view.LoginRegisterView
import com.zhuorui.securities.personal.ui.viewmodel.LoginRegisterViewModel
import com.zhuorui.securities.personal.util.PatternUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class LoginRegisterPresenter(context: Context) : AbsNetPresenter<LoginRegisterView, LoginRegisterViewModel>() {
    private var recLen = 60//跳过倒计时提示5秒
    private var disposable:Disposable?=null
    private var transaction: String? = null

    fun setTransaction(transaction: String?) {
        this.transaction = transaction
    }

    fun requestSendLoginCode(str: kotlin.String) {
        val request = SendLoginCodeRequest(str, CountryCodeConfig.read().defaultCode, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.sendLoginCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }

    fun requestUserLoginCode(str: kotlin.String, vfcode: kotlin.String) {
        view?.showProgressDailog(1)
        val request =
            UserLoginCodeRequest(str, vfcode, CountryCodeConfig.read().defaultCode, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.userLoginCode(request)
            ?.enqueue(Network.IHCallBack<UserLoginCodeResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendLoginCodeResponse(response: SendLoginCodeResponse) {
        if (!transactions.isMyTransaction(response)) return
        if (response.request is SendLoginCodeRequest) {
            view?.changeLoginSendCodeState(1)
            startTask()
        }
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
        view?.showProgressDailog(0)
        if (response.request is UserLoginCodeRequest) {
            if (response.code == "010003") {
                //设置密码
                view?.gotopsw()
                return
            } else if (response.code == "030002") {
                 // 请求验证超10次数
                 view?.showErrorTimes("",1)
                return
            } else if(response.code == "0100013"){
                //验证次数弹框
                response.msg?.let { view?.showErrorTimes(it,2) }
            }
        } else if (response.request is SendLoginCodeRequest) {
            //网络问题
            if (response.isNetworkBroken) {
                ToastUtil.instance.toastCenter(R.string.verify_get_code_error)
                return
            }
        }
        super.onErrorResponse(response)
    }

    fun startTask() {
            disposable = Observable.interval(0,1,  TimeUnit.SECONDS).take(61)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    recLen--
                    viewModel?.str?.set(recLen.toString()+"s")
                    if(recLen<0) {
                        viewModel?.str?.set(ResUtil.getString(R.string.send_verification_code))
                        viewModel?.getcodeState?.set(1)
                        view?.changeLoginSendCodeState(0)
                    }
                }
        }






    fun postChangeMytabInfo() {
        RxBus.getDefault().post(MyTabInfoEvent())
    }

    fun getGetCodeColor(state: Int) {
        viewModel?.getcodeState?.set(state)
    }

    fun getUserInfoData() {
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
        RxBus.getDefault().post(LoginStateChangeEvent(true, transaction))
    }

    fun detailChangeCodeState(code: String, et_phone: EditText, et_code: EditText, btn_login: StateButton) {
        if (code == "+86") {
            et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
        } else {
            et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
        }
        if (!TextUtils.isEmpty(et_phone.text.toString()) && !TextUtils.isEmpty(et_code.text.toString())) {
            if (code == "+86") {
                val matcher = PatternUtils.patternZhPhone(et_phone.text.toString())
                if (matcher) {
                    getGetCodeColor(1)
                    view?.changeLoginSendCodeState(0)
                    btn_login.isEnabled = true
                } else {
                    getGetCodeColor(0)
                    view?.changeLoginSendCodeState(1)
                    btn_login.isEnabled = false
                }
            } else {
                val matcher = PatternUtils.patternOtherPhone(et_phone.text.toString())
                if (matcher) {
                    getGetCodeColor(1)
                    view?.changeLoginSendCodeState(0)
                    btn_login.isEnabled = true
                } else {
                    getGetCodeColor(0)
                    view?.changeLoginSendCodeState(1)
                    btn_login.isEnabled = false
                }
            }
        } else if (!TextUtils.isEmpty(et_phone.text.toString()) && TextUtils.isEmpty(et_code.text.toString())) {
            if (code == "+86") {
                val matcher = PatternUtils.patternZhPhone(et_phone.text.toString())
                if (matcher) {
                    getGetCodeColor(1)
                    view?.changeLoginSendCodeState(0)
                    btn_login.isEnabled = false
                } else {
                    getGetCodeColor(0)
                    view?.changeLoginSendCodeState(1)
                    btn_login.isEnabled = false
                }
            } else {
                val matcher = PatternUtils.patternOtherPhone(et_phone.text.toString())
                if (matcher) {
                    getGetCodeColor(1)
                    view?.changeLoginSendCodeState(0)
                    btn_login.isEnabled = false
                } else {
                    getGetCodeColor(0)
                    view?.changeLoginSendCodeState(1)
                    btn_login.isEnabled = false
                }
            }
        } else if (TextUtils.isEmpty(et_phone.text.toString()) && !TextUtils.isEmpty(et_code.text.toString())) {
            getGetCodeColor(0)
            view?.changeLoginSendCodeState(1)
            btn_login.isEnabled = false
        } else if (TextUtils.isEmpty(et_phone.text.toString()) && TextUtils.isEmpty(et_code.text.toString())) {
            getGetCodeColor(0)
            view?.changeLoginSendCodeState(1)
            btn_login.isEnabled = false
        }
    }

    override fun destroy() {
        super.destroy()
        disposable?.dispose()
    }

}