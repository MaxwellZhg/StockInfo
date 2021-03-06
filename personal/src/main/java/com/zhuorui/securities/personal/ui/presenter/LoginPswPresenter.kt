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
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.event.LoginStateChangeEvent
import com.zhuorui.securities.personal.net.IPersonalNet
import com.zhuorui.securities.personal.net.request.GetUserInfoDataRequest
import com.zhuorui.securities.personal.net.request.UserLoginPwdRequest
import com.zhuorui.securities.personal.net.response.GetUserInfoResponse
import com.zhuorui.securities.personal.net.response.UserLoginCodeResponse
import com.zhuorui.securities.personal.ui.view.LoginPswView
import com.zhuorui.securities.personal.ui.viewmodel.LoginPswViewModel
import com.zhuorui.securities.personal.util.PatternUtils
import java.util.regex.Pattern

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class LoginPswPresenter() : AbsNetPresenter<LoginPswView, LoginPswViewModel>(){
    private var transaction: String? = null

    fun requestLoginPwd(phone:String,password:String,phoneArea:String) {
       view?.showProgressDailog(1)
        val request = UserLoginPwdRequest(phone, password,CountryCodeConfig.read().defaultCode, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.userLoginByPwd(request)
            ?.enqueue(Network.IHCallBack<UserLoginCodeResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onUserLoginPwdResponse(response: UserLoginCodeResponse) {
        view?.showProgressDailog(0)
        if (response.request is UserLoginPwdRequest) {
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
        if (response.request is UserLoginPwdRequest) {
            view?.showProgressDailog(0)
            if(response.code=="010005"){
                //错误次数
                response.msg?.let { view?.showErrorTimesDailog(it) }
                return
            }
            if(response.code=="010007"){
                view?.showVerify()
                return
            }
            super.onErrorResponse(response)
        }
    }

    private fun showPswError() {

    }

    fun detailTips(str:String,pass:String):Boolean{
        val pattern = "^(?![\\d]+\$)(?![a-zA-Z]+\$)(?![^\\da-zA-Z]+\$).{6,20}\$"
        //用正则式匹配文本获取匹配器
        val matcher = Pattern.compile(pattern).matcher(pass)
        if (str == "") {
            view?.showTipsInfo(1)
            return false
        }
        if (pass == "") {
            view?.showTipsInfo(2)
            return false
        }
        if(!matcher.find()){
            view?.showTipsInfo(3)
            return  false
        }

        return true

    }

    fun getUserInfoData(){
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

    fun detailChangeCodeState(code:String, et_phone: EditText, et_code: EditText, btn_login: StateButton){
        if(code == "+86"){
            et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
        }else{
            et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
        }
        if(!TextUtils.isEmpty(et_phone.text.toString())&&!TextUtils.isEmpty(et_code.text.toString())){
            if(code == "+86"){
                btn_login.isEnabled = PatternUtils.patternZhPhone(et_phone.text.toString())&&PatternUtils.patternLoginPassWord(et_code.text.toString())
            }else{
                btn_login.isEnabled =PatternUtils.patternZhPhone(et_phone.text.toString())&&PatternUtils.patternLoginPassWord(et_code.text.toString())
            }
        }else{
            btn_login.isEnabled=false
        }
    }



    fun setTransaction(transaction: String?) {
        this.transaction = transaction
    }


}