package com.zhuorui.securities.personal.ui.presenter

import android.content.Context
import android.view.View
import com.zhuorui.commonwidget.common.CountryCodeConfig
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.net.IPersonalNet
import com.zhuorui.securities.personal.net.request.SendLoginCodeRequest
import com.zhuorui.securities.personal.net.request.VerifForgetCodeRequest
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.ui.dailog.ErrorTimesDialog
import com.zhuorui.securities.personal.ui.view.ForgetPswView
import com.zhuorui.securities.personal.ui.viewmodel.ForgetPswViewModel
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
        ErrorTimesDialog(context,1,"")
    }
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(context)
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
                    viewModel?.str?.set(recLen.toString()+"s")
                    if (recLen < 0) {
                        timer!!.cancel()
                        task = null
                        timer = null
                        viewModel?.str?.set(ResUtil.getString(R.string.send_verification_code))
                        viewModel?.getcodeState?.set(0)
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
        dialogshow(1)
        val request = SendLoginCodeRequest(str, CountryCodeConfig.read().defaultCode, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.sendForgetPwdCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendForgetCodeResponse(response: SendLoginCodeResponse) {
          dialogshow(0)
          startTimeCountDown()
    }

    override fun onErrorResponse(response: ErrorResponse) {
        if (response.request is SendLoginCodeRequest) {
            dialogshow(0)
            if(response.code == "030002"){
                showErrorDailog()
                return
            }
            super.onErrorResponse(response)
        }else if(response.request is VerifForgetCodeRequest){
            dialogshow(0)
            super.onErrorResponse(response)
        }
    }

    fun requestVerifyForgetCode(str: kotlin.String,code:kotlin.String){
        dialogshow(1)
        val request = VerifForgetCodeRequest(str, code, CountryCodeConfig.read().defaultCode,transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.verifyForgetCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onVerifyForgetCodeResponse(response: SendLoginCodeResponse) {
        if(response.request is VerifForgetCodeRequest) {
            dialogshow(0)
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

    fun dialogshow(type:Int){
        when(type){
            1->{
                progressDialog.setCancelable(false)
                progressDialog.show()
            }
            else->{
                if(progressDialog!=null) {
                    progressDialog.setCancelable(true)
                    progressDialog.dismiss()
                }
            }
        }
    }

    fun getGetCodeColor(state: Int) {
        viewModel?.getcodeState?.set(state)
    }

}