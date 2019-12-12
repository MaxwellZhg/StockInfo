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
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.net.IPersonalNet
import com.zhuorui.securities.personal.net.request.RestLoginPswRequest
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.ui.view.RestPswView
import com.zhuorui.securities.personal.ui.viewmodel.RestPswViewModel
import com.zhuorui.securities.personal.util.PatternUtils
import java.util.regex.Pattern

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/21
 * Desc:
 */
class RestPswPresenter(context: Context):AbsNetPresenter<RestPswView, RestPswViewModel>() {
    override fun init() {
        super.init()
        view?.init()
    }

    fun requestRestLoginPsw(phone: String?,newpsw:String,code: String?) {
        view?.showProgressDailog(1)
        val request = RestLoginPswRequest(phone, newpsw,code, CountryCodeConfig.read().defaultCode,transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.restLoginPsw(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onRestLoginPswResponse(response: SendLoginCodeResponse) {
        if (!transactions.isMyTransaction(response)) return
        if (response.request is RestLoginPswRequest) {
            view?.showProgressDailog(0)
              view?.gotopswlogin()
        }
    }


    override fun onErrorResponse(response: ErrorResponse) {
        if (response.request is RestLoginPswRequest) {
            view?.showProgressDailog(0)
            return
        }
        super.onErrorResponse(response)
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