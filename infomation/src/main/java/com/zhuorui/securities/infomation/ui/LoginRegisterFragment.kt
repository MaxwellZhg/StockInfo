package com.zhuorui.securities.infomation.ui

import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil

import com.zhuorui.securities.infomation.R
import com.zhuorui.securities.infomation.net.InfomationNet
import com.zhuorui.securities.infomation.net.request.SendLoginCodeRequest
import com.zhuorui.securities.infomation.net.request.UserLoginCodeRequest
import com.zhuorui.securities.infomation.net.response.SendLoginCodeResponse
import com.zhuorui.securities.infomation.net.response.UserLoginCodeResponse
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_and_register_fragment.*
import kotlinx.android.synthetic.main.login_and_register_fragment.tv_btn_login
import kotlinx.android.synthetic.main.setting_psw_fragment.*
import me.jessyan.autosize.utils.LogUtils
import java.util.*
import kotlin.String as String

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/15
 * Desc:手机号登录与注册
 */
class LoginRegisterFragment : AbsSwipeBackNetFragment(), View.OnClickListener, TextWatcher {
    private lateinit var strphone: String
    private lateinit var phonecode: String
    internal var timer: Timer? = null
    private var recLen = 60//跳过倒计时提示5秒
    internal var task: TimerTask? = null
    override val layout: Int
        get() = R.layout.login_and_register_fragment

    override fun init() {
        tv_send_code.setOnClickListener(this)
        iv_cancle.setOnClickListener(this)
        tv_btn_login.setOnClickListener(this)
        et_phone_code.addTextChangedListener(this)
    }

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    override fun onClick(v: View?) {
        strphone = et_phone.text.toString().trim()
        phonecode = et_phone_code.text.toString().trim()
        when (v?.id) {
            R.id.tv_send_code -> {
                if (strphone == null || strphone == "") {
                    ToastUtil.instance.toast(R.string.phone_tips)
                    return
                }
                startTimeCountDown(R.string.minutiues)
                requestSendLoginCode(strphone)
            }
            R.id.iv_cancle -> {
               pop()
            }
            R.id.tv_btn_login->{
              if (strphone == null || strphone == "") {
                    ToastUtil.instance.toast(R.string.phone_tips)
                    return
                }
                if (phonecode == null || phonecode == "") {
                    ToastUtil.instance.toast(R.string.phone_code_tips)
                    return
                }
             requestUserLoginCode(strphone,phonecode)
                startWithPop(SettingPswFragment())
            }
        }
    }

    private fun requestSendLoginCode(str: kotlin.String) {
        val request = SendLoginCodeRequest(str, "0086", transactions.createTransaction())
        Cache[InfomationNet::class.java]?.sendLoginCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }

    private fun requestUserLoginCode(str: kotlin.String,vfcode:kotlin.String) {
        val request = UserLoginCodeRequest(str, vfcode,"0086", transactions.createTransaction())
        Cache[InfomationNet::class.java]?.userLoginCode(request)
            ?.enqueue(Network.IHCallBack<UserLoginCodeResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendLoginCodeResponse(response: SendLoginCodeResponse) {
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onUserLoginCodeResponse(response: UserLoginCodeResponse) {

    }

    override fun onErrorResponse(response: ErrorResponse) {
        if (response.request is UserLoginCodeRequest) {
            if(response.code=="010001"){
                 startWithPop(SettingPswFragment())
              }
        }
        super.onErrorResponse(response)
    }
    @Throws(InterruptedException::class)
    fun startTask() {
        if (task == null) {
            timer = Timer()
            task = object : TimerTask() {
                override fun run() {
                    recLen--
                    Observable.create(ObservableOnSubscribe<Int> {
                        it.onNext(recLen)
                    }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            tv_send_code.text = recLen.toString() + "s"
                            if (recLen < 0) {
                                timer!!.cancel()
                                task = null
                                timer = null
                                tv_send_code.text = ResUtil.getString(R.string.send_verification_code)
                            }
                        }
                }
            }
        }
        timer!!.schedule(task, 1000, 1000)
    }

    private fun startTimeCountDown(@StringRes str: Int) {
        if (recLen == 60) {
            tv_send_code.text = ResUtil.getString(str)
            try {
                startTask()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        } else if (recLen < 0) {
            recLen = 60
            tv_send_code.text = ResUtil.getString(str)
            try {
                startTask()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if(TextUtils.isEmpty(et_phone.text.toString())){
                    ToastUtil.instance.toast(R.string.phone_tips)
                }else {
                    tv_btn_login.background = ResUtil.getDrawable(R.drawable.login_btn_checked)
                }
             }
        } else {
            tv_btn_login.background = ResUtil.getDrawable(R.drawable.login_btn_unchecked)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

}

