package com.zhuorui.securities.infomation.ui

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.Md5Util
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.infomation.BR
import com.zhuorui.securities.infomation.R
import com.zhuorui.securities.infomation.ui.presenter.LoginPswPresenter
import com.zhuorui.securities.infomation.ui.view.LoginPswView
import com.zhuorui.securities.infomation.ui.viewmodel.LoginPswViewModel
import com.zhuorui.securities.infomation.databinding.LoginPswFragmentBinding
import com.zhuorui.securities.infomation.ui.viewmodel.OpenAccountTabViewModel
import kotlinx.android.synthetic.main.login_and_register_fragment.*
import kotlinx.android.synthetic.main.login_psw_fragment.*
import kotlinx.android.synthetic.main.login_psw_fragment.et_phone
import kotlinx.android.synthetic.main.login_psw_fragment.iv_cancle
import kotlinx.android.synthetic.main.login_psw_fragment.tv_btn_login

/*
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/19
 * Desc:
 * */


class LoginPswFragment :AbsSwipeBackNetFragment<LoginPswFragmentBinding, LoginPswViewModel,LoginPswView, LoginPswPresenter>(),LoginPswView,View.OnClickListener,TextWatcher{
    private lateinit var strphone: String
    private lateinit var password: String
    override val layout: Int
        get() = R.layout.login_psw_fragment
    override val viewModelId: Int
        get() = BR.viewmodel
    override val createPresenter: LoginPswPresenter
        get() = LoginPswPresenter()
    override val createViewModel: LoginPswViewModel?
        get() = ViewModelProviders.of(this).get(LoginPswViewModel::class.java)
    override val getView: LoginPswView
        get() = this
    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }
    override fun init() {
        tv_code_login_register.setOnClickListener(this)
        ll_country_disct.setOnClickListener(this)
        iv_cancle.setOnClickListener(this)
        tv_btn_login.setOnClickListener(this)
        et_password.addTextChangedListener(this)
        tv_forget_psw.setOnClickListener(this)
    }
    override fun onClick(p0: View?) {
        strphone = et_phone.text.toString().trim()
        password = et_password.text.toString().trim()
       when(p0?.id){
           R.id.tv_code_login_register->{
               startWithPop(LoginRegisterFragment.newInstance())
           }
           R.id.ll_country_disct->{
               start(CountryDisctFragment())
           }
           R.id.iv_cancle->{
               pop()
           }
           R.id.tv_btn_login->{
               if (strphone == null || strphone == "") {
                   ToastUtil.instance.toast(R.string.phone_tips)
                   return
               }
               if (password == null || password == "") {
                   ToastUtil.instance.toast(R.string.input_psw_tips)
                   return
               }
               presenter?.requestLoginPwd(strphone,Md5Util.getMd5Str(password),"0086")
           }
           R.id.tv_forget_psw->{
               start(ForgetPswFragment())
           }
       }
    }
    override fun gotomain() {
         pop()
    }
    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if(TextUtils.isEmpty(et_password.text.toString())){
                    ToastUtil.instance.toast(R.string.input_psw_tips)
                }else {
                    tv_btn_login.isEnabled=true
                }
            }
        } else {
            tv_btn_login.isEnabled=false
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }


}
