package com.zhuorui.securities.infomation.ui

import android.os.Bundle
import android.text.InputFilter
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackEventFragment
import com.zhuorui.securities.base2app.util.Md5Util
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.infomation.BR
import com.zhuorui.securities.infomation.R
import com.zhuorui.securities.infomation.ui.dailog.InfoDialog
import com.zhuorui.securities.infomation.ui.presenter.SettingPswPresenter
import com.zhuorui.securities.infomation.ui.view.SettingPswView
import com.zhuorui.securities.infomation.ui.viewmodel.SettingPswViewModel
import kotlinx.android.synthetic.main.item_dailog_info.*
import kotlinx.android.synthetic.main.login_and_register_fragment.*
import kotlinx.android.synthetic.main.setting_psw_fragment.*
import com.zhuorui.securities.infomation.databinding.SettingPswFragmentBinding
import com.zhuorui.securities.infomation.ui.config.LocalLoginResConfig
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/16
 * Desc:
 */

class SettingPswFragment : AbsSwipeBackEventFragment<SettingPswFragmentBinding, SettingPswViewModel, SettingPswView, SettingPswPresenter>(),SettingPswView,View.OnClickListener{
    private var phone: String? = null
    private var code :String?=null
    override val layout: Int
        get() = R.layout.setting_psw_fragment
    override val viewModelId: Int
        get() = BR.viewmodel
    override val createPresenter: SettingPswPresenter
        get() = SettingPswPresenter()
    override val createViewModel: SettingPswViewModel?
        get() =  ViewModelProviders.of(this).get(SettingPswViewModel::class.java)
    override val getView: SettingPswView
        get() = this
    private val infodialog:InfoDialog by lazy {

        InfoDialog(this.requireContext())
    }

    private lateinit var strloginpsw: String
    private lateinit var strensurepsw: String

    override fun init() {
        phone = arguments?.getSerializable("phone") as String?
        code = arguments?.getSerializable("code") as String?
        iv_back.setOnClickListener(this)
        tv_btn_finish.setOnClickListener(this)
        cb_login_psw.setOnCheckedChangeListener{buttonView, isChecked->
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
    }
    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }
    companion object {
        fun newInstance(phone: String?, code: String?): SettingPswFragment {
            val fragment = SettingPswFragment()
            if (phone != null && code != null) {
                val bundle = Bundle()
                bundle.putSerializable("phone", phone)
                bundle.putSerializable("code", code)
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
            R.id.tv_btn_finish->{
                if (strloginpsw == null || strloginpsw == "") {
                    ToastUtil.instance.toast(R.string.input_psw_tips)
                    return
                }
                if (strensurepsw == null || strensurepsw == "") {
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
    private fun showDailog() {
       infodialog.show()
       infodialog.setOnclickListener( View.OnClickListener {
           when(it.id){
               R.id.rl_gotomain->{
                   infodialog.dismiss()
               }
               R.id.rl_completeinfo->{
                   infodialog.dismiss()
                   start(LoginPswFragment())
               }
           }
       })
   }

    override fun showDialog() {
       showDailog()
    }

}
