package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.common.CommonCountryCodeFragment
import com.zhuorui.commonwidget.common.CommonEnum
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.ForgetPswFragmentBinding
import com.zhuorui.securities.personal.ui.dailog.ErrorTimesDialog
import com.zhuorui.securities.personal.ui.presenter.ForgetPswPresenter
import com.zhuorui.securities.personal.ui.view.ForgetPswView
import com.zhuorui.securities.personal.ui.viewmodel.ForgetPswViewModel
import com.zhuorui.securities.personal.util.PatternUtils
import kotlinx.android.synthetic.main.forget_psw_fragment.*
import kotlinx.android.synthetic.main.forget_psw_fragment.et_phone
import kotlinx.android.synthetic.main.forget_psw_fragment.et_phone_code
import kotlinx.android.synthetic.main.forget_psw_fragment.rl_country_disct
import kotlinx.android.synthetic.main.forget_psw_fragment.tv_areaphone_tips
import kotlinx.android.synthetic.main.forget_psw_fragment.tv_send_code
import kotlinx.android.synthetic.main.login_and_register_fragment.*
import me.jessyan.autosize.utils.LogUtils
import me.yokeyword.fragmentation.ISupportFragment

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/21
 * Desc:忘记密码
 * */
class ForgetPswFragment :AbsSwipeBackNetFragment<ForgetPswFragmentBinding,ForgetPswViewModel, ForgetPswView, ForgetPswPresenter>(),ForgetPswView,View.OnClickListener,TextWatcher{
    private lateinit var strphone: String
    private lateinit var phonecode: String
    private val errorDialog by lazy {
        ErrorTimesDialog(requireContext(),1,"")
    }
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(requireContext())
    }
    override val layout: Int
        get() = R.layout.forget_psw_fragment
    override val viewModelId: Int
        get() = BR.viewmodel
    override val createPresenter: ForgetPswPresenter
        get() = ForgetPswPresenter(requireContext())
    override val createViewModel: ForgetPswViewModel?
        get() = ViewModelProviders.of(this).get(ForgetPswViewModel::class.java)
    override val getView: ForgetPswView
        get() = this
    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        iv_back.setOnClickListener(this)
        tv_send_code.setOnClickListener(this)
        rl_country_disct.setOnClickListener(this)
        tv_btn_commit.setOnClickListener(this)
        tv_phone_code_login.setOnClickListener(this)
        et_phone.addTextChangedListener(PhoneEtChange())
        et_phone_code.addTextChangedListener(this)
    }
    override fun onClick(p0: View?) {
        strphone = et_phone.text.toString().trim()
        phonecode = et_phone_code.text.toString().trim()
       when(p0?.id){
           R.id.rl_country_disct->{
               startForResult(CommonCountryCodeFragment.newInstance(CommonEnum.Code), ISupportFragment.RESULT_OK)
           }
           R.id.tv_send_code->{
               if (strphone == "") {
                   ToastUtil.instance.toast(R.string.phone_tips)
                   return
               }
               presenter?.requestSendForgetCode(strphone)
           }
           R.id.tv_btn_commit->{
               if (strphone == "") {
                   ToastUtil.instance.toast(R.string.phone_tips)
                   return
               }
               if (phonecode == "") {
                   ToastUtil.instance.toast(R.string.phone_code_tips)
                   return
               }
              //
               presenter?.requestVerifyForgetCode(strphone,phonecode)
           }
           R.id.tv_phone_code_login->{
               startWithPop(LoginRegisterFragment.newInstance(1))
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
                }else if(!TextUtils.isEmpty(et_phone_code.text.toString())&&TextUtils.isEmpty(et_phone.text.toString())){
                    tv_btn_commit.isEnabled=false
                }else if(!TextUtils.isEmpty(et_phone_code.text.toString())&&!TextUtils.isEmpty(et_phone.text.toString())){
                    if(tv_areaphone_tips.text  == "+86"){
                        tv_btn_commit.isEnabled = PatternUtils.patternZhPhone(et_phone.text.toString())&&PatternUtils.patternPhoneCode(et_phone_code.text.toString())
                    }else{
                        tv_btn_commit.isEnabled == PatternUtils.patternOtherPhone(et_phone.text.toString())&& PatternUtils.patternPhoneCode(et_phone_code.text.toString())
                    }
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
        startWithPop(RestPswFragment.newInstance(strphone,phonecode))
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        when(requestCode){
            ISupportFragment.RESULT_OK->{
                if(data?.get("str")!=null&&data?.get("code")!=null) {
                    var str = data?.get("str") as String
                    var code = data?.get("code") as String
                    LogUtils.e(str)
                    tv_contry.text = str
                    tv_areaphone_tips.text = code
                    presenter?.detailChangeCodeState(code,et_phone,et_phone_code,tv_btn_commit)
                }
            }
        }
    }

    inner class PhoneEtChange : TextWatcher{
        override fun afterTextChanged(p0: Editable?) {
            if(tv_areaphone_tips.text == "+86"){
                et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
            }else{
                et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
            }
            if(!TextUtils.isEmpty(p0.toString())&&!TextUtils.isEmpty(et_phone_code.text.toString())){
                if(tv_areaphone_tips.text == "+86"){
                    val matcher = PatternUtils.patternZhPhone(p0.toString())
                    if(matcher) {
                        presenter?.getGetCodeColor(1)
                        changeLoginSendCodeState(0)
                        tv_btn_commit.isEnabled = true
                    }else{
                        presenter?.getGetCodeColor(0)
                        changeLoginSendCodeState(1)
                        tv_btn_commit.isEnabled = false
                    }
                }else{
                    val matcher = PatternUtils.patternOtherPhone(p0.toString())
                    if(matcher) {
                        presenter?.getGetCodeColor(1)
                        changeLoginSendCodeState(0)
                        tv_btn_commit.isEnabled = true
                    }else{
                        presenter?.getGetCodeColor(0)
                        changeLoginSendCodeState(1)
                        tv_btn_commit.isEnabled = false
                    }
                }
            }else if(!TextUtils.isEmpty(p0.toString())&&TextUtils.isEmpty(et_phone_code.text.toString())){
                if(tv_areaphone_tips.text == "+86"){
                    val matcher = PatternUtils.patternZhPhone(p0.toString())
                    if(matcher) {
                        presenter?.getGetCodeColor(1)
                        changeLoginSendCodeState(0)
                        tv_btn_commit.isEnabled = false
                    }else{
                        presenter?.getGetCodeColor(0)
                        changeLoginSendCodeState(1)
                        tv_btn_commit.isEnabled = false
                    }
                }else{
                    val matcher = PatternUtils.patternOtherPhone(p0.toString())
                    if(matcher) {
                        presenter?.getGetCodeColor(1)
                        changeLoginSendCodeState(0)
                        tv_btn_commit.isEnabled = false
                    }else{
                        presenter?.getGetCodeColor(0)
                        changeLoginSendCodeState(1)
                        tv_btn_commit.isEnabled = false
                    }
                }
            }else if(TextUtils.isEmpty(p0.toString())&&!TextUtils.isEmpty(et_phone_code.text.toString())){
                presenter?.getGetCodeColor(0)
                changeLoginSendCodeState(1)
                tv_btn_commit.isEnabled=false
            }else if(TextUtils.isEmpty(p0.toString())&&TextUtils.isEmpty(et_phone_code.text.toString())){
                presenter?.getGetCodeColor(0)
                changeLoginSendCodeState(1)
                tv_btn_commit.isEnabled=false
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    }

    fun showErrorDailog() {
        errorDialog.show()
        errorDialog.setOnclickListener( View.OnClickListener {
            when(it.id){
                R.id.rl_complete_verify->{
                    errorDialog.dismiss()
                }
            }
        })
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
    override fun showProgressDailog(type: Int) {
        dialogshow(type)
    }
    override fun showErrorTimes(str: String, type: Int) {
        showErrorDailog()
    }
    override fun changeLoginSendCodeState(type: Int) {
        tv_send_code.isClickable = type != 1
    }

}