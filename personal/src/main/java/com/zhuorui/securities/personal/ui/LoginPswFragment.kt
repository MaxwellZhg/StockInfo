package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.common.CommonCountryCodeFragment
import com.zhuorui.commonwidget.common.CommonEnum
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.Md5Util
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.databinding.LoginPswFragmentBinding
import com.zhuorui.securities.personal.ui.dailog.ErrorTimesDialog
import com.zhuorui.securities.personal.ui.presenter.LoginPswPresenter
import com.zhuorui.securities.personal.ui.view.LoginPswView
import com.zhuorui.securities.personal.ui.viewmodel.LoginPswViewModel
import com.zhuorui.securities.personal.util.PatternUtils
import kotlinx.android.synthetic.main.login_psw_fragment.*
import me.jessyan.autosize.utils.LogUtils
import me.yokeyword.fragmentation.ISupportFragment

/*
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/19
 * Desc:密码登录
  * * */


class LoginPswFragment :AbsSwipeBackNetFragment<LoginPswFragmentBinding, LoginPswViewModel,LoginPswView, LoginPswPresenter>(),LoginPswView,View.OnClickListener,TextWatcher{
    private lateinit var strphone: String
    private lateinit var password: String
    private var isChecked:Boolean =true
    private var transaction:String?=null
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(requireContext())
    }
    private var errorDialog: ErrorTimesDialog?=null
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

    companion object{
        fun newInstance( transaction: String?): LoginPswFragment {
            val fragment = LoginPswFragment()
            val bundle = Bundle()
            bundle.putString("transaction", transaction)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onClick(p0: View?) {
        strphone = et_phone.text.toString().trim()
        password = et_password.text.toString().trim()
        when(p0?.id){
            R.id.tv_code_login_register->{
                startWithPop(LoginRegisterFragment.newInstance(1))
            }
            R.id.ll_country_disct->{
                startForResult(CommonCountryCodeFragment.newInstance(CommonEnum.Code), ISupportFragment.RESULT_OK)
            }
            R.id.iv_cancle->{
                pop()
            }
            R.id.tv_btn_login->{
                //fe008700f25cb28940ca8ed91b23b354
                if(presenter?.detailTips(strphone,password)!!) {
                    presenter?.requestLoginPwd(strphone, Md5Util.getMd5Str(password), "0086")
                }
            }
            R.id.tv_forget_psw->{
                startWithPop(ForgetPswFragment())
            }
            R.id.cb_psw_login->{
                if (isChecked) {
                    et_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    cb_psw_login.setImageResource(R.mipmap.show_psw)
                }else{
                    et_password.transformationMethod = PasswordTransformationMethod.getInstance()
                    cb_psw_login.setImageResource(R.mipmap.hide_psw)
                }
                isChecked=!isChecked
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
                }else if(!TextUtils.isEmpty(et_password.text.toString())&&TextUtils.isEmpty(et_phone.text.toString())){
                    tv_btn_login.isEnabled=false
                }else if(!TextUtils.isEmpty(et_password.text.toString())&&!TextUtils.isEmpty(et_phone.text.toString())){
                    if(tv_areaphone_tips.text  == "+86"){
                        tv_btn_login.isEnabled = PatternUtils.patternLoginPassWord(et_password.text.toString())&&PatternUtils.patternZhPhone(et_phone.text.toString())
                    }else{
                        tv_btn_login.isEnabled == PatternUtils.patternLoginPassWord(et_password.text.toString())&&PatternUtils.patternOtherPhone(et_phone.text.toString())
                    }
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

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        when(requestCode){
            ISupportFragment.RESULT_OK->{
                var str=data?.get("str") as String
                var code=data?.get("code") as String
                LogUtils.e(str)
                tv_contry.text=str
                tv_areaphone_tips.text=code
                presenter?.detailChangeCodeState(code,et_phone,et_password,tv_btn_login)
            }
        }
    }

    override fun showTipsInfo(type: Int) {
        when(type){
            1->{
                ToastUtil.instance.toast(R.string.phone_tips)
            }
            2->{
                ToastUtil.instance.toast(R.string.input_psw_tips)
            }
            3->{
                ToastUtil.instance.toast(R.string.new_psw_no_match)
            }
        }
    }
    override fun showVerify() {
        start(PhoneDevVerifyFragment.newInstance(strphone))
    }


    inner class PhoneEtChange : TextWatcher{
        override fun afterTextChanged(p0: Editable?) {
            if(tv_areaphone_tips.text == "+86"){
                et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
            }else{
                et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
            }
            if(!TextUtils.isEmpty(p0.toString())&&!TextUtils.isEmpty(et_password.text.toString())){
                if(tv_areaphone_tips.text == "+86"){
                    tv_btn_login.isEnabled = PatternUtils.patternZhPhone(p0.toString())&&PatternUtils.patternLoginPassWord(et_password.text.toString())
                }else{
                    tv_btn_login.isEnabled = PatternUtils.patternOtherPhone(p0.toString())&&PatternUtils.patternLoginPassWord(et_password.text.toString())
                }
            }else{
                tv_btn_login.isEnabled=false
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        transaction = arguments?.getString("transaction")
        presenter?.setTransaction(transaction)
        cb_psw_login.setOnClickListener(this)
        tv_code_login_register.setOnClickListener(this)
        ll_country_disct.setOnClickListener(this)
        iv_cancle.setOnClickListener(this)
        tv_btn_login.setOnClickListener(this)
        et_phone.addTextChangedListener(PhoneEtChange())
        et_password.addTextChangedListener(this)
        tv_forget_psw.setOnClickListener(this)
        et_phone.isFocusable = true
        et_phone.isFocusableInTouchMode = true
        et_phone.requestFocus()
        if(LocalAccountConfig.getInstance().getAccountInfo().phone!=null){
            et_phone.setText(LocalAccountConfig.getInstance().getAccountInfo().phone)
            LocalAccountConfig.getInstance().getAccountInfo().phone?.length?.let { et_phone.setSelection(it) }
        }
    }

    fun dialogshow(type:Int){
        when(type){
            1->{
                progressDialog.setCancelable(false)
                progressDialog.show()
            }
            else->{
                if(progressDialog!=null) {
                    progressDialog.setCancelable(true)
                    progressDialog.dismiss()
                }
            }
        }
    }
    fun showErrorDailog(str:String?) {
        errorDialog=ErrorTimesDialog(requireContext(),2,str)
        errorDialog?.show()
        errorDialog?.setOnclickListener( View.OnClickListener {
            when(it.id){
                R.id.rl_complete_psw->{
                    errorDialog?.dismiss()
                }
            }
        })
    }

    override fun showProgressDailog(type: Int) {
       dialogshow(type)
    }
    override fun showErrorTimesDailog(str: String) {
       showErrorDailog(str)
    }


}
