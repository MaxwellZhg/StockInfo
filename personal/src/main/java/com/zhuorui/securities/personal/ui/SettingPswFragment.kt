package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.ConfirmToCancelDialog
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackEventFragment
import com.zhuorui.securities.base2app.util.Md5Util
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.SettingPswFragmentBinding
import com.zhuorui.securities.personal.ui.presenter.SettingPswPresenter
import com.zhuorui.securities.personal.ui.view.SettingPswView
import com.zhuorui.securities.personal.ui.viewmodel.SettingPswViewModel
import com.zhuorui.securities.personal.util.PatternUtils
import kotlinx.android.synthetic.main.login_psw_fragment.*
import kotlinx.android.synthetic.main.setting_psw_fragment.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/16
 * Desc:设置密码
 * */

class SettingPswFragment : AbsSwipeBackEventFragment<SettingPswFragmentBinding, SettingPswViewModel, SettingPswView, SettingPswPresenter>()
    ,SettingPswView,View.OnClickListener,TextWatcher,View.OnTouchListener{

    private var phone: String = ""
    private var code :String=""

    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(requireContext())
    }
    override val layout: Int
        get() = R.layout.setting_psw_fragment
    override val viewModelId: Int
        get() = BR.viewmodel
    override val createPresenter: SettingPswPresenter
        get() = SettingPswPresenter(requireContext())
    override val createViewModel: SettingPswViewModel?
        get() =  ViewModelProviders.of(this).get(SettingPswViewModel::class.java)
    override val getView: SettingPswView
        get() = this
    private lateinit var strloginpsw: String
    private lateinit var strensurepsw: String

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        phone = arguments?.getString("phone")?:phone
        code = arguments?.getString("code")?:code
        iv_back.setOnClickListener(this)
        tv_btn_settin_finish.setOnClickListener(this)
        cb_login_psw.setOnCheckedChangeListener{ _, isChecked->
            run {
                if (isChecked) {
                    et_login_psw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }else{
                    et_login_psw.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }
        }
        cb_ensure_psw.setOnCheckedChangeListener{ _, isChecked->
            run {
                if (isChecked) {
                    et_ensure_psw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }else{
                    et_ensure_psw.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }
        }
        et_login_psw.addTextChangedListener(PhoneEtChange())
        et_ensure_psw.addTextChangedListener(this)
        et_login_psw.onFocusChangeListener =EtNewPswWordChange()
        et_ensure_psw.onFocusChangeListener =EtEnsureWordChange()
        et_login_psw.isFocusable = true
        et_login_psw.isFocusableInTouchMode = true
        et_login_psw.requestFocus()
        rl_content.setOnTouchListener(this)
    }
    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }
    companion object {
        fun newInstance(phone: String?, code: String?): SettingPswFragment {
            val fragment = SettingPswFragment()
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
        strloginpsw = et_login_psw.text.toString().trim()
        strensurepsw = et_ensure_psw.text.toString().trim()
        when(p0?.id){
            R.id.iv_back->{
                pop()
            }
            R.id.tv_btn_settin_finish->{
                if (strloginpsw == "") {
                    ToastUtil.instance.toast(R.string.input_psw_tips)
                    return
                }
                if (strensurepsw == "") {
                    ToastUtil.instance.toast(R.string.input_psw_tips)
                    return
                }
                if (strloginpsw.length<6) {
                    ToastUtil.instance.toast(R.string.input_mix_count)
                    return
                }
                if (strensurepsw.length<6) {
                    ToastUtil.instance.toast(R.string.input_mix_count)
                    return
                }
                if (strloginpsw != strensurepsw) {
                    ToastUtil.instance.toast(R.string.compare_psw_again)
                    return
                }
                presenter?.requestUserLoginPwdCode(Md5Util.getMd5Str(strensurepsw),code!!,phone!!)

            }
        }
    }
   fun gotomain() {
        pop()
    }



    inner class PhoneEtChange : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            if(et_login_psw.text.toString()==""){
                presenter?.detailPhonePswTips(et_login_psw.text.toString())
            }
            if(et_ensure_psw.text.toString()!=""){
                presenter?.detailCompareWithPswTips(p0.toString(),et_ensure_psw.text.toString())
            }
            if(!TextUtils.isEmpty(p0.toString())&&!TextUtils.isEmpty(et_ensure_psw.text.toString())){
                tv_btn_settin_finish.isEnabled = PatternUtils.patternLoginPassWord(p0.toString())
                        &&PatternUtils.patternLoginPassWord(et_ensure_psw.text.toString())
                        &&p0.toString()==et_ensure_psw.text.toString()
            }else{
                tv_btn_settin_finish.isEnabled =false
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }


    }

    override fun afterTextChanged(p0: Editable?) {
        if(!TextUtils.isEmpty(p0.toString())&&!TextUtils.isEmpty(et_login_psw.text.toString())){
            tv_btn_settin_finish.isEnabled = PatternUtils.patternLoginPassWord(p0.toString())
                    &&PatternUtils.patternLoginPassWord(et_login_psw.text.toString())
                    &&p0.toString()==et_login_psw.text.toString()
        }else{
            tv_btn_settin_finish.isEnabled=false
        }

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

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

    override fun showProgressDailog(type: Int) {
        dialogshow(type)
    }

    override fun gotoMain() {
        gotomain()
    }

    inner class EtNewPswWordChange: View.OnFocusChangeListener{
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if(!hasFocus){
                presenter?.detailPhonePswTips(et_login_psw.text.toString())
            }else{
                presenter?.detailPhonePswTips("")
            }
        }

    }

    inner class EtEnsureWordChange: View.OnFocusChangeListener{
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if(!hasFocus) {
                presenter?.detailCompareWithPswTips(et_login_psw.text.toString(),et_ensure_psw.text.toString())
                if(et_login_psw.text.toString()!=""){
                    presenter?.detailPhonePswTips(et_login_psw.text.toString())
                }
            }else{
                presenter?.detailCompareWithPswTips(et_login_psw.text.toString(),"")
            }
        }

    }
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        rl_content.isFocusable = true
        rl_content.isFocusableInTouchMode = true
        rl_content.requestFocus()
        hideSoftInput()
        return false
    }

}
