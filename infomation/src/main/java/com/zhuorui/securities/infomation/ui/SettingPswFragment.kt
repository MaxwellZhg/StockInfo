package com.zhuorui.securities.infomation.ui

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackEventFragment
import com.zhuorui.securities.infomation.R
import kotlinx.android.synthetic.main.setting_psw_fragment.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/16
 * Desc:
 */
class SettingPswFragment : AbsSwipeBackEventFragment(),View.OnClickListener{

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    override val layout: Int
        get() = R.layout.setting_psw_fragment

    override fun init() {
        iv_back.setOnClickListener(this)
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
    override fun onClick(p0: View?) {
       when(p0?.id){
           R.id.iv_back->{
               pop()
           }
       }
    }
}