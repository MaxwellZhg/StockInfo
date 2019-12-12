package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.Md5Util
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.RestPswFragmentBinding
import com.zhuorui.securities.personal.ui.presenter.RestPswPresenter
import com.zhuorui.securities.personal.ui.view.RestPswView
import com.zhuorui.securities.personal.ui.viewmodel.RestPswViewModel
import com.zhuorui.securities.personal.util.PatternUtils
import kotlinx.android.synthetic.main.rest_psw_fragment.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/21
 * Desc:重置密码
 * */

class RestPswFragment : AbsSwipeBackNetFragment<RestPswFragmentBinding, RestPswViewModel, RestPswView, RestPswPresenter>(),
    RestPswView,View.OnClickListener,TextWatcher,View.OnTouchListener{
    private var phone: String = ""
    private var code :String=""
    private lateinit var strnewpsw: String
    private lateinit var strensurepsw: String
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(requireContext())
    }
    override val layout: Int
        get() = R.layout.rest_psw_fragment
    override val viewModelId: Int
        get() = BR.viewmodel
    override val createPresenter: RestPswPresenter
        get() = RestPswPresenter(requireContext())
    override val createViewModel: RestPswViewModel?
        get() = RestPswViewModel()
    override val getView: RestPswView
        get() = this
    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }
    override fun init() {
        phone = arguments?.getString("phone")?:phone
        code = arguments?.getString("code") ?:code
        iv_back.setOnClickListener(this)
        cb_new_psw.setOnCheckedChangeListener{buttonView, isChecked->
            run {
                if (isChecked) {
                    et_new_psw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }else{
                    et_new_psw.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }
        }
        cb_rest_ensure_psw.setOnCheckedChangeListener{ _, isChecked->
            run {
                if (isChecked) {
                    et_rest_ensure_psw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }else{
                    et_rest_ensure_psw.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }
        }
        tv_btn_rest.setOnClickListener(this)
        et_new_psw.addTextChangedListener(PhoneEtChange())
        et_rest_ensure_psw.addTextChangedListener(this)
        tv_phone_code_login.setOnClickListener(this)
        et_new_psw.isFocusable = true
        et_new_psw.isFocusableInTouchMode = true
        et_new_psw.requestFocus()
        et_new_psw.onFocusChangeListener = EtNewPswWordChange()
        et_rest_ensure_psw.onFocusChangeListener=EtEnsureWordChange()
        rl_rest_content.setOnTouchListener(this)
    }
    companion object {
        fun newInstance(phone: String?, code: String?): RestPswFragment {
            val fragment = RestPswFragment()
            if (phone != null && code != null) {
                val bundle = Bundle()
                bundle.putString("phone", phone)
                bundle.putString("code", code)
                fragment.arguments = bundle
            }
            return fragment
        }
    }
    override fun onClick(p0: View?) {
        strnewpsw=et_new_psw.text.toString().trim()
        strensurepsw=et_rest_ensure_psw.text.toString().trim()
       when(p0?.id){
           R.id.iv_back->{
               pop()
           }
           R.id.tv_btn_rest->{
               if (strnewpsw == "") {
                   ToastUtil.instance.toast(R.string.input_psw_tips)
                   return
               }
               if (strensurepsw == "") {
                   ToastUtil.instance.toast(R.string.input_psw_tips)
                   return
               }
               if (strnewpsw.length<6) {
                   ToastUtil.instance.toast(R.string.input_mix_count)
                   return
               }
               if (strensurepsw.length<6) {
                   ToastUtil.instance.toast(R.string.input_mix_count)
                   return
               }
               if (strnewpsw != strensurepsw) {
                   ToastUtil.instance.toast(R.string.compare_psw_again)
                   return
               }
               presenter?.requestRestLoginPsw(phone, Md5Util.getMd5Str(strensurepsw), code)


           }
           R.id.tv_phone_code_login->{
               startWithPop(LoginRegisterFragment.newInstance(1))
           }
       }
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if(!TextUtils.isEmpty(p0.toString())&&!TextUtils.isEmpty(et_new_psw.text.toString())){
                    tv_btn_rest.isEnabled = PatternUtils.patternLoginPassWord(p0.toString())
                            &&PatternUtils.patternLoginPassWord(et_new_psw.text.toString())
                            &&p0.toString()==et_new_psw.text.toString()
                }else{
                    tv_btn_rest.isEnabled=false
                }
            }
        } else {
            tv_btn_rest.isEnabled=false
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
    override fun gotopswlogin() {
        startWithPop(LoginRegisterFragment.newInstance(1))
    }

    inner class PhoneEtChange : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            if(et_new_psw.text.toString()==""){
                presenter?.detailPhonePswTips(et_new_psw.text.toString())
            }
            if(et_rest_ensure_psw.text.toString()!=""){
                presenter?.detailCompareWithPswTips(p0.toString(),et_rest_ensure_psw.text.toString())
            }
            if(!TextUtils.isEmpty(p0.toString())&&!TextUtils.isEmpty(et_rest_ensure_psw.text.toString())){
                tv_btn_rest.isEnabled = PatternUtils.patternLoginPassWord(p0.toString())
                        &&PatternUtils.patternLoginPassWord(et_rest_ensure_psw.text.toString())
                        &&p0.toString()==et_rest_ensure_psw.text.toString()
            }else{
                tv_btn_rest.isEnabled=false
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    }

    fun dialogshow(type:Int){
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

    override fun showProgressDailog(type: Int) {
        dialogshow(type)
    }

    inner class EtNewPswWordChange: View.OnFocusChangeListener{
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if(!hasFocus){
                presenter?.detailPhonePswTips(et_new_psw.text.toString())
            }else{
                presenter?.detailPhonePswTips("")
            }
        }

    }

    inner class EtEnsureWordChange: View.OnFocusChangeListener{
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if(!hasFocus) {
                presenter?.detailCompareWithPswTips(et_new_psw.text.toString(),et_rest_ensure_psw.text.toString())
                if(et_new_psw.text.toString()!=""){
                    presenter?.detailPhonePswTips(et_new_psw.text.toString())
                }
            }else{
                presenter?.detailCompareWithPswTips(et_new_psw.text.toString(),"")
            }
        }

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        rl_rest_content.isFocusable = true
        rl_rest_content.isFocusableInTouchMode = true
        rl_rest_content.requestFocus()
        hideSoftInput()
        return false
    }




}
