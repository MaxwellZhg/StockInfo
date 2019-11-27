package com.zhuorui.securities.personal.ui

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.common.CommonCountryCodeFragment
import com.zhuorui.commonwidget.common.CommonEnum
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.LoginAndRegisterFragmentBinding
import com.zhuorui.securities.personal.event.JumpToOpenAccountEvent
import com.zhuorui.securities.personal.event.LoginStateChangeEvent
import com.zhuorui.securities.personal.ui.presenter.LoginRegisterPresenter
import com.zhuorui.securities.personal.ui.view.LoginRegisterView
import com.zhuorui.securities.personal.ui.viewmodel.LoginRegisterViewModel
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
class LoginRegisterFragment : AbsSwipeBackNetFragment<LoginAndRegisterFragmentBinding, LoginRegisterViewModel, LoginRegisterView, LoginRegisterPresenter>(), View.OnClickListener, TextWatcher,LoginRegisterView {

    private lateinit var strphone: String
    private lateinit var phonecode: String
    private var locale: Locale?=null
    private var type:Int = 0
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
    }

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
            R.id.tv_btn_login->{
              if (strphone == "") {
                    ToastUtil.instance.toast(R.string.phone_tips)
                    return
                }
                if (phonecode == "") {
                    ToastUtil.instance.toast(R.string.phone_code_tips)
                    return
                }
                presenter?.requestUserLoginCode(strphone,phonecode)
            }
            R.id.tv_phone_num_login->{
                startWithPop(LoginPswFragment())
            }
            R.id.rl_country_disct->{
                startForResult(CommonCountryCodeFragment.newInstance(CommonEnum.Code), ISupportFragment.RESULT_OK)
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if(TextUtils.isEmpty(et_phone_code.text.toString())){
                    ToastUtil.instance.toast(R.string.phone_code_tips)
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

    override fun gotopsw() {
        startWithPop(SettingPswFragment.newInstance(strphone,phonecode))
    }

    override fun gotomain() {
        RxBus.getDefault().post(LoginStateChangeEvent(true))
         pop()
    }

    companion object {
        fun newInstance(type:Int): LoginRegisterFragment {
            val fragment = LoginRegisterFragment()
            if (type!= null) {
                val bundle = Bundle()
                bundle.putSerializable("type", type)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        when(requestCode){
            ISupportFragment.RESULT_OK->{
                if(data?.get("str")!=null&&data?.get("code")!=null) {
                    var str = data?.get("str") as String
                    var code = data?.get("code") as String
                    LogUtils.e(str)
                    tv_login_contry.text = str
                    tv_areaphone_tips.text = code
                }
            }
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        type = arguments?.getSerializable("type") as Int
        if(type==2){
            presenter?.postChangeMytabInfo()
        }
    }

    inner class PhoneEtChange : TextWatcher{
        override fun afterTextChanged(p0: Editable?) {
          if(!TextUtils.isEmpty(p0.toString())){
              presenter?.getGetCodeColor(1)
          }else{
              presenter?.getGetCodeColor(0)
          }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    }
    override fun showSendCode(str:String) {
        et_phone_code.setText(str)
    }
}

