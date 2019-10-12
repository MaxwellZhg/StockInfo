package com.zhuorui.securities.personal.ui.presenter

import android.content.Context
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
import com.zhuorui.securities.personal.net.request.ModifyOldPhoneRequest
import com.zhuorui.securities.personal.net.request.SendLoginCodeRequest
import com.zhuorui.securities.personal.net.request.SendOldRepalceCodeRequest
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.ui.view.ChangePhoneNumView
import com.zhuorui.securities.personal.ui.viewmodel.ChangePhoneNumViewModel
import java.util.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/10
 * Desc:
 */
class ChangePhoneNumPresenter(context: Context) :AbsNetPresenter<ChangePhoneNumView,ChangePhoneNumViewModel>(){
    internal var timer: Timer? = null
    private var recLen = 60//跳过倒计时提示5秒
    internal var task: TimerTask? = null
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(context)
    }
    override fun init() {
        super.init()
    }
    fun requestSendOldRepaiedCode(str: String) {
        dialogshow(1)
        val request = SendLoginCodeRequest(str, "86", transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.sendOldPhoneRepaireCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendOldRepaiedCodeResponse(response: SendLoginCodeResponse) {
        if (!transactions.isMyTransaction(response)) return
        dialogshow(0)
        if(response.request is SendLoginCodeRequest){
            startTimeCountDown()
            view?.showGetCode(response.data)
        }else if(response.request is ModifyOldPhoneRequest){
            view?.gotonext()
        }
    }

    fun requestModifyOldPhone(str: kotlin.String?,verificationCode:String) {
        dialogshow(1)
        val request = ModifyOldPhoneRequest(str, verificationCode,"86", transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.sendModifyOldPhone(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }

    override fun onErrorResponse(response: ErrorResponse) {
        dialogshow(0)
        if (response.request is ModifyOldPhoneRequest) {
            return
        }else if(response.request is SendLoginCodeRequest){
            return
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
                    viewModel?.str?.set(recLen.toString()+"s")
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

    private fun dialogshow(type:Int){
        when(type){
            1->{
                progressDialog.setCancelable(false)
                progressDialog.show()
            }
            else->{
                progressDialog.setCancelable(true)
                progressDialog.dismiss()

            }
        }
    }
}