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
import com.zhuorui.securities.personal.net.request.ModifyLoginPswRequest
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.ui.view.RepairedLoginPassView
import com.zhuorui.securities.personal.ui.viewmodel.RepairedLoginPassViewModel
import java.util.regex.Pattern

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/11
 * Desc:
 */
class RepairedLoginPassPresenter(context: Context) :AbsNetPresenter<RepairedLoginPassView,RepairedLoginPassViewModel>(){
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(context)
    }

    override fun init() {
        super.init()
    }

    fun detailPass(oldStr:String,newStr:String,enStr:String):Boolean{
        if(oldStr==""){
            ResUtil.getString(R.string.no_null_old_pass)?.let { ToastUtil.instance.toastCenter(it) }
            return false
        }else{
            if (oldStr.length<6){
            ResUtil.getString(R.string.no_null_old_pass)?.let { ToastUtil.instance.toastCenter(it) }
                return false
            }else{
                val pattern = "^(?![\\d]+\$)(?![a-zA-Z]+\$)(?![^\\da-zA-Z]+\$).{6,20}\$"
                //用正则式匹配文本获取匹配器
                val matcher = Pattern.compile(pattern).matcher(oldStr)
                if (!matcher.find()) {
                    ResUtil.getString(R.string.no_null_old_pass)?.let {
                        ToastUtil.instance.toastCenter(it)
                    }
                    return false
                }
            }
        }
        if (newStr == "") {

          ResUtil.getString(R.string.input_new_pws_mix)?.let {  ToastUtil.instance.toastCenter(it) }
            return false
        }else{
            if (newStr.length<6){
              ResUtil.getString(R.string.input_new_pws_mix)?.let {
                  ToastUtil.instance.toastCenter(it)
              }
                if(enStr != ""){
                    if(newStr==enStr){
                      ResUtil.getString(R.string.input_new_pws_mix)?.let {
                          ToastUtil.instance.toastCenter(it)
                      }
                        return false
                    }else{
                        ResUtil.getString(R.string.compare_no_match)?.let {
                            ToastUtil.instance.toastCenter(it)
                        }
                        return false
                    }
                }
            }else {
                val pattern = "^(?![\\d]+\$)(?![a-zA-Z]+\$)(?![^\\da-zA-Z]+\$).{6,20}\$"
                //用正则式匹配文本获取匹配器
                val matcher = Pattern.compile(pattern).matcher(newStr)
                if (!matcher.find()) {
                    ResUtil.getString(R.string.new_psw_no_match)?.let {
                        ToastUtil.instance.toastCenter(it)
                    }
                    if(enStr != ""){
                        if(newStr==enStr){

                      ResUtil.getString(R.string.new_psw_no_match)?.let {
                          ToastUtil.instance.toastCenter(it)
                      }
                            return false
                        }else{
                            ToastUtil.instance.toastCenter(ResUtil.getString(R.string.compare_no_match).toString())
                            return false
                        }
                    }
                }
            }
        }
        if (enStr == "") {
            ToastUtil.instance.toastCenter(ResUtil.getString(R.string.compare_no_match).toString())
            return false
        }else{
            if(newStr==enStr){
                val pattern = "^(?![\\d]+\$)(?![a-zA-Z]+\$)(?![^\\da-zA-Z]+\$).{6,20}\$"
                //用正则式匹配文本获取匹配器
                val matcher = Pattern.compile(pattern).matcher(newStr)
                if (!matcher.find()) {
                    ToastUtil.instance.toastCenter(ResUtil.getString(R.string.new_psw_no_match).toString())

                    return false
                }else {
                    return true
                }
            }else{

               ToastUtil.instance.toastCenter(ResUtil.getString(R.string.compare_no_match).toString())

                return  false
            }
            return false
        }
    }

    fun modifyNewLoginPsw(oldstr:String,newStr:String){
        dialogshow(1)
        val request = ModifyLoginPswRequest(Md5Util.getMd5Str(oldstr),Md5Util.getMd5Str(newStr),transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.sendModifyNewLoginPsw(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onmodifyNewLoginPsw(response: SendLoginCodeResponse) {
            dialogshow(0)
            view?.gotomain()

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