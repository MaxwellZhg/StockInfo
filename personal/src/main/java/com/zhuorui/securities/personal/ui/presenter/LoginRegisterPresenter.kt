package com.zhuorui.securities.personal.ui.presenter

import android.content.Context
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import com.zhuorui.commonwidget.StateButton
import com.zhuorui.commonwidget.common.CountryCodeConfig
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseRequest
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.event.LoginStateChangeEvent
import com.zhuorui.securities.personal.event.MyTabInfoEvent
import com.zhuorui.securities.personal.net.IPersonalNet
import com.zhuorui.securities.personal.net.request.GetUserInfoDataRequest
import com.zhuorui.securities.personal.net.request.SendLoginCodeRequest
import com.zhuorui.securities.personal.net.request.UserLoginCodeRequest
import com.zhuorui.securities.personal.net.request.UserLoginOutRequest
import com.zhuorui.securities.personal.net.response.GetUserInfoResponse
import com.zhuorui.securities.personal.net.response.SendLoginCodeResponse
import com.zhuorui.securities.personal.net.response.UserLoginCodeResponse
import com.zhuorui.securities.personal.ui.dailog.ErrorTimesDialog
import com.zhuorui.securities.personal.ui.view.LoginRegisterView
import com.zhuorui.securities.personal.ui.viewmodel.LoginRegisterViewModel
import com.zhuorui.securities.personal.util.PatternUtils
import kotlinx.android.synthetic.main.login_and_register_fragment.*
import java.util.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class LoginRegisterPresenter(context: Context) : AbsNetPresenter<LoginRegisterView, LoginRegisterViewModel>() {
    internal var timer: Timer? = null
    private var recLen = 60//跳过倒计时提示5秒
    internal var task: TimerTask? = null
    private var transaction: String? = null
    private var errorDialog:ErrorTimesDialog?=null
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(context)
    }
    private val errorTimesDialog by lazy {
        ErrorTimesDialog(context, 1, "")
    }

    fun showErrorTimesDailog(str:String?) {
        errorDialog= context?.let { ErrorTimesDialog(it,2,str) }
        errorDialog?.show()
        errorDialog?.setOnclickListener( View.OnClickListener {
            when(it.id){
                R.id.rl_complete_psw->{
                    errorDialog?.dismiss()
                }
            }
        })
    }

    override fun init() {
        super.init()
        view?.init()
    }

    fun setTransaction(transaction: String?) {
        this.transaction = transaction
    }

    fun requestSendLoginCode(str: kotlin.String) {
        dialogshow(1)
        val request = SendLoginCodeRequest(str, CountryCodeConfig.read().defaultCode, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.sendLoginCode(request)
            ?.enqueue(Network.IHCallBack<SendLoginCodeResponse>(request))
    }

    fun requestUserLoginCode(str: kotlin.String, vfcode: kotlin.String) {
        dialogshow(1)
        val request =
            UserLoginCodeRequest(str, vfcode, CountryCodeConfig.read().defaultCode, transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.userLoginCode(request)
            ?.enqueue(Network.IHCallBack<UserLoginCodeResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSendLoginCodeResponse(response: SendLoginCodeResponse) {
        if (!transactions.isMyTransaction(response)) return
        if (response.request is SendLoginCodeRequest) {
            dialogshow(0)
            setGetCodeClickState(1)
            startTimeCountDown()
        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onUserLoginCodeResponse(response: UserLoginCodeResponse) {
        if (response.request is UserLoginCodeRequest) {
            dialogshow(0)
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
        dialogshow(0)
        if (response.request is UserLoginCodeRequest) {
            if (response.code == "010003") {
                //设置密码
                view?.gotopsw()
                return
            } else if (response.code == "030002") {
                 // 请求验证超次数
                showErrorDailog()
                return
            } else if(response.code == "0100013"){
                //验证次数弹框
                showErrorTimesDailog(response.msg)
            }
        } else if (response.request is SendLoginCodeRequest) {
            //网络问题
            if (response.isNetworkBroken) {
                ToastUtil.instance.toastCenter(R.string.verify_get_code_error)
                return
            }
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
                    viewModel?.str?.set(recLen.toString() + "s")
                    if (recLen < 0) {
                        timer!!.cancel()
                        task = null
                        timer = null
                        viewModel?.str?.set(ResUtil.getString(R.string.send_verification_code))
                        viewModel?.getcodeState?.set(1)
                        viewModel?.getCodeClickState?.set(0)
                    }
                }
            }
            timer?.schedule(task, 1000, 1000)
        }

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

    fun dialogshow(type: Int) {
        when (type) {
            1 -> {
                progressDialog.setCancelable(false)
                progressDialog.show()
            }
            else -> {
                progressDialog.setCancelable(true)
                progressDialog.dismiss()

            }
        }
    }

    fun showErrorDailog() {
        errorTimesDialog.show()
        errorTimesDialog.setOnclickListener(View.OnClickListener {
            when (it.id) {
                R.id.rl_complete_verify -> {
                    errorTimesDialog.dismiss()
                }
            }
        })
    }

    fun postChangeMytabInfo() {
        RxBus.getDefault().post(MyTabInfoEvent())
    }

    fun getGetCodeColor(state: Int) {
        viewModel?.getcodeState?.set(state)
    }

    fun getUserInfoData() {
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

    fun setGetCodeClickState(state: Int) {
        viewModel?.getCodeClickState?.set(state)
    }

    fun detailChangeCodeState(code: String, et_phone: EditText, et_code: EditText, btn_login: StateButton) {
        if (code == "+86") {
            et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
        } else {
            et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
        }
        if (!TextUtils.isEmpty(et_phone.text.toString()) && !TextUtils.isEmpty(et_code.text.toString())) {
            if (code == "+86") {
                val matcher = PatternUtils.patternZhPhone(et_phone.text.toString())
                if (matcher) {
                    getGetCodeColor(1)
                    setGetCodeClickState(0)
                    btn_login.isEnabled = true
                } else {
                    getGetCodeColor(0)
                    setGetCodeClickState(1)
                    btn_login.isEnabled = false
                }
            } else {
                val matcher = PatternUtils.patternOtherPhone(et_phone.text.toString())
                if (matcher) {
                    getGetCodeColor(1)
                    setGetCodeClickState(0)
                    btn_login.isEnabled = true
                } else {
                    getGetCodeColor(0)
                    setGetCodeClickState(1)
                    btn_login.isEnabled = false
                }
            }
        } else if (!TextUtils.isEmpty(et_phone.text.toString()) && TextUtils.isEmpty(et_code.text.toString())) {
            if (code == "+86") {
                val matcher = PatternUtils.patternZhPhone(et_phone.text.toString())
                if (matcher) {
                    getGetCodeColor(1)
                    setGetCodeClickState(0)
                    btn_login.isEnabled = false
                } else {
                    getGetCodeColor(0)
                    setGetCodeClickState(1)
                    btn_login.isEnabled = false
                }
            } else {
                val matcher = PatternUtils.patternOtherPhone(et_phone.text.toString())
                if (matcher) {
                    getGetCodeColor(1)
                    setGetCodeClickState(0)
                    btn_login.isEnabled = false
                } else {
                    getGetCodeColor(0)
                    setGetCodeClickState(1)
                    btn_login.isEnabled = false
                }
            }
        } else if (TextUtils.isEmpty(et_phone.text.toString()) && !TextUtils.isEmpty(et_code.text.toString())) {
            getGetCodeColor(0)
            setGetCodeClickState(1)
            btn_login.isEnabled = false
        } else if (TextUtils.isEmpty(et_phone.text.toString()) && TextUtils.isEmpty(et_code.text.toString())) {
            getGetCodeColor(0)
            setGetCodeClickState(1)
            btn_login.isEnabled = false
        }
    }

}