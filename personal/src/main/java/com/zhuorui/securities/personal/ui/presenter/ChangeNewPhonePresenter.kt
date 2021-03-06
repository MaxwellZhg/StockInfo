package com.zhuorui.securities.personal.ui.presenter

import android.content.Context
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
import com.zhuorui.securities.personal.net.request.ModifyNewPhoneCodeRequest
import com.zhuorui.securities.personal.net.request.SendLoginCodeRequest
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.ui.view.ChangeNewPhoneView
import com.zhuorui.securities.personal.ui.viewmodel.ChangeNewPhoneViewModel
import java.util.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/10
 * Desc:
 */
class ChangeNewPhonePresenter(context: Context) : AbsNetPresenter<ChangeNewPhoneView, ChangeNewPhoneViewModel>() {
    internal var timer: Timer? = null
    private var recLen = 60//跳过倒计时提示5秒
    internal var task: TimerTask? = null
    override fun init() {
        super.init()
    }

    fun detailPhoneTips(phone: String, code: String, oldphone: String?): Boolean {
        if (phone == "") {
            ResUtil.getString(R.string.phone_tips)?.let { ToastUtil.instance.toast(it) }
            return false
        }
        if (phone == oldphone) {
            ResUtil.getString(R.string.same_phone_num)?.let { ToastUtil.instance.toastCenter(it) }
            return false
        }
        if (code == null) {
            ResUtil.getString(R.string.phone_code_tips)?.let { ToastUtil.instance.toast(it) }
            return false
        }
        if (code.length < 6) {
            ResUtil.getString(R.string.verify_code_length)?.let { ToastUtil.instance.toast(it) }
            return false
        }
        return true
    }

    fun requestSendNewRepaiedCode(str: String) {
        view?.showProgressDailog(1)
        val request = SendLoginCodeRequest(str, CountryCodeConfig.read().defaultCode, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.sendNewRepairedCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendNewRepaiedCodeResponse(response: SendLoginCodeResponse) {
        if (!transactions.isMyTransaction(response)) return
        view?.showProgressDailog(0)
        if (response.request is SendLoginCodeRequest) {
            startTimeCountDown()
            view?.showgetCode(response.data)
        } else if (response.request is ModifyNewPhoneCodeRequest) {
            view?.gotomain()
        }
    }

    fun requestModifyNewRepaiedCode(
        str: String?,
        verificationCode: String?,
        newPhone: String,
        newVerificationCode: String
    ) {
        view?.showProgressDailog(1)
        val request = ModifyNewPhoneCodeRequest(
            str,
            verificationCode,
            newPhone,
            newVerificationCode,
            "86",
            "86",
            transactions.createTransaction()
        )
        Cache[IPersonalNet::class.java]?.sendModifyNewPhone(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }

    @Throws(InterruptedException::class)
    fun startTask() {
        if (task == null) {
            timer = Timer()
            task = object : TimerTask() {
                override fun run() {
                    recLen--
                    viewModel?.str?.set(recLen.toString() + "s")
                    if (recLen < 0) {
                        timer!!.cancel()
                        task = null
                        timer = null
                        viewModel?.str?.set(ResUtil.getString(R.string.get_verify_code))
                    }
                }
            }
        }
        timer!!.schedule(task, 1000, 1000)
    }

    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
        view?.showProgressDailog(0)
    }

    private fun startTimeCountDown() {
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

}