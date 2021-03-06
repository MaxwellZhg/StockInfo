package com.zhuorui.securities.personal.ui

import android.os.Build
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
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.databinding.LoginAndRegisterFragmentBinding
import com.zhuorui.securities.personal.ui.dailog.ErrorTimesDialog
import com.zhuorui.securities.personal.ui.presenter.LoginRegisterPresenter
import com.zhuorui.securities.personal.ui.view.LoginRegisterView
import com.zhuorui.securities.personal.ui.viewmodel.LoginRegisterViewModel
import com.zhuorui.securities.personal.util.PatternUtils
import kotlinx.android.synthetic.main.login_and_register_fragment.*
import me.jessyan.autosize.utils.LogUtils
import me.yokeyword.fragmentation.ISupportFragment
import java.util.*


/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/15
 * Desc:手机号登录与注册
 */
class LoginRegisterFragment :
    AbsSwipeBackNetFragment<LoginAndRegisterFragmentBinding, LoginRegisterViewModel, LoginRegisterView, LoginRegisterPresenter>(),
    View.OnClickListener, TextWatcher, LoginRegisterView {

    private lateinit var strphone: String
    private lateinit var phonecode: String
    var filterLength = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
    private var transaction:String?=null
    //用正则式匹配文本获取匹配器
    // val matcher = Pattern.compile(pattern).matcher(oldStr)
    private var locale: Locale? = null
    private var type: Int = -1
    override val layout: Int
        get() = R.layout.login_and_register_fragment
    override val viewModelId: Int
        get() = BR.viewmodel
    override val createPresenter: LoginRegisterPresenter
        get() = LoginRegisterPresenter(requireContext())
    override val createViewModel: LoginRegisterViewModel?
        get() = ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java)
    override val getView: LoginRegisterView
        get() = this

    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(requireContext())
    }
    private var errorDialog :ErrorTimesDialog?=null

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    override fun onClick(v: View?) {
        strphone = et_phone.text.toString().trim()
        phonecode = et_phone_code.text.toString().trim()
        when (v?.id) {
            R.id.tv_send_code -> {
                if (strphone == "") {
                    ToastUtil.instance.toast(R.string.phone_tips)
                    return
                }
                presenter?.requestSendLoginCode(strphone)
            }
            R.id.iv_cancle -> {
                pop()
            }
            R.id.tv_btn_login -> {
                if (strphone == "") {
                    ToastUtil.instance.toast(R.string.phone_tips)
                    return
                }
                if (phonecode == "") {
                    ToastUtil.instance.toast(R.string.phone_code_tips)
                    return
                }
                presenter?.requestUserLoginCode(strphone, phonecode)
            }
            R.id.tv_phone_num_login -> {
                startWithPop(LoginPswFragment.newInstance(transaction))
            }
            R.id.rl_country_disct -> {
                startForResult(CommonCountryCodeFragment.newInstance(CommonEnum.Code), ISupportFragment.RESULT_OK)
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if (TextUtils.isEmpty(et_phone_code.text.toString())) {
                    ToastUtil.instance.toast(R.string.phone_code_tips)
                } else if (!TextUtils.isEmpty(et_phone_code.text.toString()) && TextUtils.isEmpty(et_phone.text.toString())) {
                    tv_btn_login.isEnabled = false
                } else if (!TextUtils.isEmpty(et_phone_code.text.toString()) && !TextUtils.isEmpty(et_phone.text.toString())) {
                    if(tv_areaphone_tips.text  == "+86"){
                        tv_btn_login.isEnabled = PatternUtils.patternZhPhone(et_phone.text.toString())&&PatternUtils.patternPhoneCode(et_phone_code.text.toString())
                    }else{
                        tv_btn_login.isEnabled == PatternUtils.patternOtherPhone(et_phone.text.toString())&& PatternUtils.patternPhoneCode(et_phone_code.text.toString())
                    }
                }
            }
        } else {
            tv_btn_login.isEnabled = false
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun gotopsw() {
        startWithPop(SettingPswFragment.newInstance(strphone, phonecode))
    }

    override fun gotomain() {
        pop()
    }

    companion object {
        fun newInstance(type: Int): LoginRegisterFragment {
            val fragment = LoginRegisterFragment()
            val bundle = Bundle()
            bundle.putInt("type", type)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(type: Int, transaction: String?): LoginRegisterFragment {
            val fragment = LoginRegisterFragment()
            val bundle = Bundle()
            bundle.putInt("type", type)
            bundle.putString("transaction", transaction)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        when (requestCode) {
            ISupportFragment.RESULT_OK -> {
                if (data?.get("str") != null && data?.get("code") != null) {
                    var str = data?.getString("str")
                    var code = data?.getString("code")
                    LogUtils.e(str)
                    tv_login_contry.text = str
                    tv_areaphone_tips.text = code
                    code?.let { presenter?.detailChangeCodeState(it,et_phone,et_phone_code,tv_btn_login) }
                }
            }
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        type = arguments?.getInt("type") ?: type
        transaction = arguments?.getString("transaction")
        presenter?.setTransaction(transaction)
        presenter?.setLifecycleOwner(this)
        if (type == 2) {
            presenter?.postChangeMytabInfo()
        }
        locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales.get(0)
        } else {
            resources.configuration.locale
        }

        tv_send_code.setOnClickListener(this)
        iv_cancle.setOnClickListener(this)
        tv_btn_login.setOnClickListener(this)
        et_phone.addTextChangedListener(PhoneEtChange())
        et_phone_code.addTextChangedListener(this)
        tv_phone_num_login.setOnClickListener(this)
        rl_country_disct.setOnClickListener(this)

        et_phone.isFocusable = true
        et_phone.isFocusableInTouchMode = true
        et_phone.requestFocus()
        if(LocalAccountConfig.getInstance().getAccountInfo().phone!=null){
            et_phone.setText(LocalAccountConfig.getInstance().getAccountInfo().phone)
            LocalAccountConfig.getInstance().getAccountInfo().phone?.length?.let { et_phone.setSelection(it) }
        }
    }

    inner class PhoneEtChange : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            if (tv_areaphone_tips.text == "+86") {
                et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
            } else {
                et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
            }
            if (!TextUtils.isEmpty(p0.toString()) && !TextUtils.isEmpty(et_phone_code.text.toString())) {
                if (tv_areaphone_tips.text == "+86") {
                    val matcher = PatternUtils.patternZhPhone(p0.toString())
                    if (matcher) {
                         presenter?.getGetCodeColor(1)
                         presenter?.setSendCodeClickable(true)
                        tv_btn_login.isEnabled = true
                    } else {
                        presenter?.getGetCodeColor(0)
                        presenter?.setSendCodeClickable(false)
                        tv_btn_login.isEnabled = false
                    }
                } else {
                    val matcher = PatternUtils.patternOtherPhone(p0.toString())
                    if (matcher) {
                        presenter?.getGetCodeColor(1)
                        presenter?.setSendCodeClickable(true)
                        tv_btn_login.isEnabled = true
                    } else {
                        presenter?.getGetCodeColor(0)
                        presenter?.setSendCodeClickable(false)
                        tv_btn_login.isEnabled = false
                    }
                }
            } else if (!TextUtils.isEmpty(p0.toString()) && TextUtils.isEmpty(et_phone_code.text.toString())) {
                if (tv_areaphone_tips.text == "+86") {
                    val matcher = PatternUtils.patternZhPhone(p0.toString())
                    if (matcher) {
                        presenter?.getGetCodeColor(1)
                        presenter?.setSendCodeClickable(true)
                        tv_btn_login.isEnabled = false
                    } else {
                        presenter?.getGetCodeColor(0)
                        presenter?.setSendCodeClickable(false)
                        tv_btn_login.isEnabled = false
                    }
                } else {
                    val matcher = PatternUtils.patternOtherPhone(p0.toString())
                    if (matcher) {
                        presenter?.getGetCodeColor(1)
                        presenter?.setSendCodeClickable(true)
                        tv_btn_login.isEnabled = false
                    } else {
                        presenter?.getGetCodeColor(0)
                        presenter?.setSendCodeClickable(false)
                        tv_btn_login.isEnabled = false
                    }
                }
            } else if (TextUtils.isEmpty(p0.toString()) && !TextUtils.isEmpty(et_phone_code.text.toString())) {
                presenter?.getGetCodeColor(0)
                presenter?.setSendCodeClickable(false)
                tv_btn_login.isEnabled = false
            } else if (TextUtils.isEmpty(p0.toString()) && TextUtils.isEmpty(et_phone_code.text.toString())) {
                presenter?.getGetCodeColor(0)
                presenter?.setSendCodeClickable(false)
                tv_btn_login.isEnabled = false
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

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


    override fun showProgressDailog(type: Int) {
          dialogshow(type)
    }

    override fun showErrorTimes(str: String, type: Int) {
       showErrorTimesDailog(str,type)
    }

    fun showErrorTimesDailog(str:String?,type:Int) {
        errorDialog= context?.let { ErrorTimesDialog(it,type,str) }
        errorDialog?.show()
        errorDialog?.setOnclickListener( View.OnClickListener {
            when(it.id){
                R.id.rl_complete_psw->{
                    errorDialog?.dismiss()
                }
            }
        })
    }

    override fun notifySendCodeClickable(isClick: Boolean) {
        tv_send_code.isClickable =isClick
    }

}

