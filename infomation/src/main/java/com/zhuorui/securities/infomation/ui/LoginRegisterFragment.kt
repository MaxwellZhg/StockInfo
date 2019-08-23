package com.zhuorui.securities.infomation.ui

import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.infomation.BR

import com.zhuorui.securities.infomation.R
import com.zhuorui.securities.infomation.R2.id.iv_cancle
import com.zhuorui.securities.infomation.R2.id.tv_send_code
import com.zhuorui.securities.infomation.net.InfomationNet
import com.zhuorui.securities.infomation.net.request.SendLoginCodeRequest
import com.zhuorui.securities.infomation.net.request.UserLoginCodeRequest
import com.zhuorui.securities.infomation.net.response.SendLoginCodeResponse
import com.zhuorui.securities.infomation.net.response.UserLoginCodeResponse
import com.zhuorui.securities.infomation.ui.viewmodel.LoginRegisterViewModel
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_and_register_fragment.*
import kotlinx.android.synthetic.main.login_and_register_fragment.tv_btn_login
import kotlinx.android.synthetic.main.setting_psw_fragment.*
import me.jessyan.autosize.utils.LogUtils
import java.util.*
import kotlin.String as String
import com.zhuorui.securities.infomation.databinding.LoginAndRegisterFragmentBinding
import com.zhuorui.securities.infomation.ui.presenter.LoginRegisterPresenter
import com.zhuorui.securities.infomation.ui.view.LoginRegisterView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/15
 * Desc:手机号登录与注册
 */
class LoginRegisterFragment : AbsSwipeBackNetFragment<LoginAndRegisterFragmentBinding, LoginRegisterViewModel, LoginRegisterView, LoginRegisterPresenter>(), View.OnClickListener, TextWatcher,LoginRegisterView {
    private lateinit var strphone: String
    private lateinit var phonecode: String
    override val layout: Int
        get() = R.layout.login_and_register_fragment
    override val viewModelId: Int
        get() =  BR.viewmodel
    override val createPresenter: LoginRegisterPresenter
        get() = LoginRegisterPresenter(requireContext())
    override val createViewModel: LoginRegisterViewModel?
        get() = ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java)
    override val getView: LoginRegisterView
        get() = this
    override fun init() {
        tv_send_code.setOnClickListener(this)
        iv_cancle.setOnClickListener(this)
        tv_btn_login.setOnClickListener(this)
        et_phone_code.addTextChangedListener(this)
        tv_phone_num_login.setOnClickListener(this)
        rl_country_disct.setOnClickListener(this)
    }

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    override fun onClick(v: View?) {
        strphone = et_phone.text.toString().trim()
        phonecode = et_phone_code.text.toString().trim()
        when (v?.id) {
            R.id.tv_send_code -> {
                if (strphone == null || strphone == "") {
                    ToastUtil.instance.toast(R.string.phone_tips)
                    return
                }
                presenter?.startTimeCountDown()
                presenter?.requestSendLoginCode(strphone)
            }
            R.id.iv_cancle -> {
               pop()
            }
            R.id.tv_btn_login->{
              if (strphone == null || strphone == "") {
                    ToastUtil.instance.toast(R.string.phone_tips)
                    return
                }
                if (phonecode == null || phonecode == "") {
                    ToastUtil.instance.toast(R.string.phone_code_tips)
                    return
                }
                presenter?.requestUserLoginCode(strphone,phonecode)
            }
            R.id.tv_phone_num_login->{
                startWithPop(LoginPswFragment())
            }
            R.id.rl_country_disct->{
                start(CountryDisctFragment())
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if(TextUtils.isEmpty(et_phone.text.toString())){
                    ToastUtil.instance.toast(R.string.phone_code_tips)
                }else {
                    presenter?.setState(0)
                }
             }
        } else {
            presenter?.setState(1)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun gotopsw() {
        startWithPop(SettingPswFragment.newInstance(strphone,phonecode))
    }

    override fun gotomain() {
         pop()
    }

    companion object {
        fun newInstance(): LoginRegisterFragment {
            return LoginRegisterFragment()
        }
    }


}

