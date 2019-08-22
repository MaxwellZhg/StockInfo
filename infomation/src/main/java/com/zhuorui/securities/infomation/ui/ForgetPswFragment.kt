package com.zhuorui.securities.infomation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.infomation.BR
import com.zhuorui.securities.infomation.R
import com.zhuorui.securities.infomation.ui.presenter.ForgetPswPresenter
import com.zhuorui.securities.infomation.ui.view.ForgetPswView
import com.zhuorui.securities.infomation.ui.viewmodel.ForgetPswViewModel
import com.zhuorui.securities.infomation.databinding.ForgetPswFragmentBinding
import kotlinx.android.synthetic.main.forget_psw_fragment.*
import kotlinx.android.synthetic.main.forget_psw_fragment.et_phone
import kotlinx.android.synthetic.main.forget_psw_fragment.et_phone_code
import kotlinx.android.synthetic.main.forget_psw_fragment.rl_country_disct
import kotlinx.android.synthetic.main.forget_psw_fragment.tv_send_code
import kotlinx.android.synthetic.main.login_and_register_fragment.*
import kotlinx.android.synthetic.main.login_and_register_fragment.tv_btn_login
import kotlinx.android.synthetic.main.login_psw_fragment.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/21
 * Desc:
 */
class ForgetPswFragment :AbsSwipeBackNetFragment<ForgetPswFragmentBinding,ForgetPswViewModel, ForgetPswView, ForgetPswPresenter>(),ForgetPswView,View.OnClickListener,TextWatcher{
    private lateinit var strphone: String
    private lateinit var phonecode: String
    override val layout: Int
        get() = R.layout.forget_psw_fragment
    override val viewModelId: Int
        get() = BR.viewmodel
    override val createPresenter: ForgetPswPresenter
        get() = ForgetPswPresenter()
    override val createViewModel: ForgetPswViewModel?
        get() = ForgetPswViewModel()
    override val getView: ForgetPswView
        get() = this
    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }
    override fun init() {
        iv_back.setOnClickListener(this)
        tv_send_code.setOnClickListener(this)
        rl_country_disct.setOnClickListener(this)
        tv_btn_commit.setOnClickListener(this)
        tv_phone_code_login.setOnClickListener(this)
        et_phone_code.addTextChangedListener(this)
    }
    override fun onClick(p0: View?) {
        strphone = et_phone.text.toString().trim()
        phonecode = et_phone_code.text.toString().trim()
       when(p0?.id){
           R.id.rl_country_disct->{
               start(CountryDisctFragment())
           }
           R.id.tv_send_code->{
               if (strphone == null || strphone == "") {
                   ToastUtil.instance.toast(R.string.phone_tips)
                   return
               }
               presenter?.startTimeCountDown()
               presenter?.requestSendForgetCode(strphone)
           }
           R.id.tv_btn_commit->{
               if (strphone == null || strphone == "") {
                   ToastUtil.instance.toast(R.string.phone_tips)
                   return
               }
               if (phonecode == null || phonecode == "") {
                   ToastUtil.instance.toast(R.string.phone_code_tips)
                   return
               }
               start(RestPswFragment.newInstance(strphone,phonecode))
              // presenter?.requestVerifyForgetCode(strphone,phonecode)
           }
           R.id.tv_phone_code_login->{
               start(LoginRegisterFragment.newInstance())
           }
           R.id.iv_back->{
               pop()
           }
       }
    }
    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if(TextUtils.isEmpty(et_phone_code.text.toString())){
                    ToastUtil.instance.toast(R.string.phone_code_tips)
                }else {
                    tv_btn_commit.isEnabled=true
                }
            }
        } else {
            tv_btn_commit.isEnabled=false
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
    override fun restpsw() {
       start(RestPswFragment())
    }


}