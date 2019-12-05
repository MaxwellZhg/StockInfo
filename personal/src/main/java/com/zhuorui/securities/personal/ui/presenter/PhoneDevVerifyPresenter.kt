package com.zhuorui.securities.personal.ui.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.zhuorui.commonwidget.common.CountryCodeConfig
import com.zhuorui.commonwidget.dialog.DevComfirmDailog
import com.zhuorui.commonwidget.dialog.ProgressDialog
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
class PhoneDevVerifyPresenter(context: Context):AbsNetPresenter<PhoneDevVerifyView,PhoneDevVerifyViewModel>(),DevComfirmDailog.CallBack{
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(context)
    }
    override fun init() {
        super.init()
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
    override fun onCancel() {

    }

    override fun onConfirm() {
      view?.gotoPhone()
    }

    fun showTipsDailog(){
        phoneDevDailog.show()
    }
    fun requestSendLoginCode(str: kotlin.String) {
        dialogshow(1)
        val request = SendLoginCodeRequest(str, CountryCodeConfig.read().defaultCode, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.sendLoginCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendLoginCodeResponse(response: SendLoginCodeResponse) {
        if (!transactions.isMyTransaction(response)) return
        if(response.request is SendLoginCodeRequest){
            dialogshow(0)
            view?.goNext()
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

    override fun onErrorResponse(response: ErrorResponse) {
        dialogshow(0)
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