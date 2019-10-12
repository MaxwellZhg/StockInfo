package com.zhuorui.securities.personal.ui.presenter

import android.content.Context
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.Md5Util
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.net.IPersonalNet
import com.zhuorui.securities.personal.net.request.ModifyCapitalPswRequest
import com.zhuorui.securities.personal.net.request.ModifyLoginPswRequest
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.ui.view.ChangeTradePassView
import com.zhuorui.securities.personal.ui.viewmodel.ChangeTradePassViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/11
 * Desc:
 */
class ChangeTradePassPresenter(context: Context) :AbsNetPresenter<ChangeTradePassView,ChangeTradePassViewModel>(){
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(context)
    }

    override fun init() {
        super.init()
    }

    fun modifyCapitalPsw(oldstr:String,newStr:String){
        dialogshow(1)
        val request = ModifyCapitalPswRequest(Md5Util.getMd5Str(oldstr), Md5Util.getMd5Str(newStr),transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.sendModifyCapitalPsw(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onmodifyCapitalPsw(response: SendLoginCodeResponse) {
        if (!transactions.isMyTransaction(response)) return
        dialogshow(0)
        view?.gotomain()

    }

    fun detailTips(newStr:String,enStr:String):Boolean{
        if(newStr==""){
            ResUtil.getString(R.string.input_new_ca_psw)?.let { ToastUtil.instance.toast(it) }
            return false
        }
        if(enStr==""){
            ResUtil.getString(R.string.input_en_ca_psw)?.let { ToastUtil.instance.toast(it) }
            return false
        }
        if(newStr!=enStr){
            ResUtil.getString(R.string.input_en_ca_psw_no_match)?.let { ToastUtil.instance.toast(it) }
           return false
          }
        return true
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