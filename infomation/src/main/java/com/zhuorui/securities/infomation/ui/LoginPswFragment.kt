package com.zhuorui.securities.infomation.ui

import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.infomation.BR
import com.zhuorui.securities.infomation.R
import com.zhuorui.securities.infomation.ui.presenter.LoginPswPresenter
import com.zhuorui.securities.infomation.ui.view.LoginPswView
import com.zhuorui.securities.infomation.ui.viewmodel.LoginPswViewModel
import com.zhuorui.securities.infomation.databinding.LoginPswFragmentBinding
import com.zhuorui.securities.infomation.ui.viewmodel.OpenAccountTabViewModel
import kotlinx.android.synthetic.main.login_psw_fragment.*

/*
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/19
 * Desc:
 * */


class LoginPswFragment :AbsSwipeBackNetFragment<LoginPswFragmentBinding, LoginPswViewModel,LoginPswView, LoginPswPresenter>(),LoginPswView,View.OnClickListener{
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
    }
    override fun onClick(p0: View?) {
       when(p0?.id){
           R.id.tv_code_login_register->{
               pop()
           }
       }
    }


}
