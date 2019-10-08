package com.zhuorui.securities.personal.ui.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.zhuorui.commonwidget.dialog.DevComfirmDailog
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.Cache
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
import com.zhuorui.securities.personal.net.request.UserLoginCodeRequest
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
class PhoneDevVerifyCodePresenter(context:Context) :AbsNetPresenter<PhoneDevVerifyCodeView,PhoneDevVerifyCodeViewModel>(),
    DevComfirmDailog.CallBack{
    internal var timer: Timer? = null
    private var recLen = 60//跳过倒计时提示5秒
    internal var task: TimerTask? = null
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(context)
    }
    /* 加载对话框 */
    private val phoneDevDailog by lazy {
        DevComfirmDailog.
            createWidth255Dialog(context,true,true)
            .setNoticeText(R.string.notice)
            .setMsgText(R.string.dev_login_problem_tips)
            .setCancelText(R.string.cancle)
            .setConfirmText(R.string.phone_call)
            .setCallBack(this)
    }

    override fun init() {
        super.init()
    }
    fun showTipsDailog(){
        phoneDevDailog.show()
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
                        viewModel?.str?.set("0s")
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

    override fun onCancel() {

    }

    override fun onConfirm() {
      view?.gotoPhone()
    }
    fun luanchCall(){
        var intent: Intent =  Intent()
        intent.action = Intent.ACTION_CALL
        intent.data = Uri.parse("tel:" + "400-788-190")
        context?.startActivity(intent)
    }

    fun requestUserLoginCode(str:String?,vfcode:String?,phoneArea:String?) {
        dialogshow(1)
        val request = UserLoginCodeRequest(str, vfcode,phoneArea, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.userLoginCode(request)
            ?.enqueue(Network.IHCallBack<UserLoginCodeResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onUserLoginCodeResponse(response: UserLoginCodeResponse) {
        if (response.request is UserLoginCodeRequest) {
            dialogshow(0)
            if (LocalAccountConfig.read().saveLogin(
                    response.data.userId,
                    response.data.phone,
                    response.data.token
                )
            ) {
                view?.gotomain()
                // 通知登录状态发生改变
                RxBus.getDefault().post(LoginStateChangeEvent(true))
            }
        }
    }

    fun dialogshow(type:Int){
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