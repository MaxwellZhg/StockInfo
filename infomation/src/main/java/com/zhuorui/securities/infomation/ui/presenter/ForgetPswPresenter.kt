package com.zhuorui.securities.infomation.ui.presenter

import android.content.Context
import android.view.View
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.infomation.R
import com.zhuorui.securities.infomation.net.InfomationNet
import com.zhuorui.securities.infomation.net.request.SendLoginCodeRequest
import com.zhuorui.securities.infomation.net.request.UserLoginCodeRequest
import com.zhuorui.securities.infomation.net.request.VerifForgetCodeRequest
import com.zhuorui.securities.infomation.net.response.SendLoginCodeResponse
import com.zhuorui.securities.infomation.net.response.UserLoginCodeResponse
import com.zhuorui.securities.infomation.ui.dailog.ErrorTimesDialog
import com.zhuorui.securities.infomation.ui.view.ForgetPswView
import com.zhuorui.securities.infomation.ui.viewmodel.ForgetPswViewModel
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
    private val errorDialog by lazy {
        ErrorTimesDialog(context,1)
    }
    override fun init() {
        super.init()
        view?.init()
    }
    @Throws(InterruptedException::class)
    fun startTask() {
        if (task == null) {
            timer = Timer()
            task = object : TimerTask() {
                override fun run() {
                    recLen--
                    viewModel?.str?.set(ResUtil.getStringFormat(R.string.credit_time, recLen))
                    if (recLen < 0) {
                        timer!!.cancel()
                        task = null
                        timer = null
                        viewModel?.str?.set(ResUtil.getString(R.string.send_verification_code))
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
        val request = SendLoginCodeRequest(str, "0086", transactions.createTransaction())
        Cache[InfomationNet::class.java]?.sendForgetPwdCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendForgetCodeResponse(response: SendLoginCodeResponse) {

    }

    fun requestVerifyForgetCode(str: kotlin.String,code:kotlin.String){
        val request = VerifForgetCodeRequest(str, code, transactions.createTransaction())
        Cache[InfomationNet::class.java]?.verifyForgetCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onVerifyForgetCodeResponse(response: SendLoginCodeResponse) {
        if(response.request is VerifForgetCodeRequest) {
            view?.restpsw()
        }
    }

    fun showErrorDailog() {
        errorDialog.show()
        errorDialog.setOnclickListener( View.OnClickListener {
            when(it.id){
                R.id.rl_complete_verify->{
                    errorDialog.dismiss()
                }
            }
        })
    }
}