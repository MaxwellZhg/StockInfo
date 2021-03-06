package com.zhuorui.securities.personal.ui.presenter

import android.content.Context
import com.zhuorui.commonwidget.common.CountryCodeConfig
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
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
import com.zhuorui.securities.personal.net.request.GetUserInfoDataRequest
import com.zhuorui.securities.personal.net.request.UserLoginRegisterRequest
import com.zhuorui.securities.personal.net.response.GetUserInfoResponse
import com.zhuorui.securities.personal.net.response.UserLoginCodeResponse
import com.zhuorui.securities.personal.ui.view.SettingPswView
import com.zhuorui.securities.personal.ui.viewmodel.SettingPswViewModel
import com.zhuorui.securities.personal.util.PatternUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class SettingPswPresenter : AbsNetPresenter<SettingPswView, SettingPswViewModel>() {


    fun requestUserLoginPwdCode(pwd: String, code: String, phone: String) {
        view?.showProgressDailog(1)
        val request = UserLoginRegisterRequest(pwd, code, phone, CountryCodeConfig.read().defaultCode, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.userPwdCode(request)
            ?.enqueue(Network.IHCallBack<UserLoginCodeResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendLoginPwdResponse(response: UserLoginCodeResponse) {
        if (response.request is UserLoginRegisterRequest) {
            if (LocalAccountConfig.getInstance().saveLogin(
                    response.data.userId,
                    response.data.phone,
                    response.data.token
                )
            ) {
                view?.showProgressDailog(0)
                getUserInfoData()
          /*      view?.showDialog()
                // 通知登录状态发生改变
                RxBus.getDefault().post(LoginStateChangeEvent(true))*/
            }
        }
    }

    override fun onErrorResponse(response: ErrorResponse) {
        if (response.request is UserLoginRegisterRequest) {
            view?.showProgressDailog(0)
            return
        }
        super.onErrorResponse(response)
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
        view?.gotoMain()
       // 弹框选择暂时关闭
       // view?.showSwicthGotoDailog()
        // 通知登录状态发生改变
        RxBus.getDefault().post(LoginStateChangeEvent(true,true))
    }

   //处理第一个输入框密码提示
    fun detailPhonePswTips(str:String){
       if(str!="") {
           if (str.length < 6) {
               viewModel?.strnews?.set(ResUtil.getString(R.string.input_new_pws_mix))
           } else {
               if (PatternUtils.patternLoginPassWord(str)) {
                   viewModel?.strnews?.set("")
               } else {
                   viewModel?.strnews?.set(ResUtil.getString(R.string.new_psw_no_match))
               }
           }
       }else{
           viewModel?.strnews?.set("")
       }
    }

    //处理第二输入框是否一致
    fun detailCompareWithPswTips(str:String,strensure:String){
        if(strensure!="") {
            if (str == strensure) {
                viewModel?.strensure?.set("")
            } else {
                viewModel?.strensure?.set(ResUtil.getString(R.string.compare_no_match))
            }
        }else{
            viewModel?.strensure?.set("")
        }
    }


}