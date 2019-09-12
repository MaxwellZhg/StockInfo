package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.ui.presenter.SecurityPresenter
import com.zhuorui.securities.personal.ui.view.SecurityView
import com.zhuorui.securities.personal.ui.viewmodel.SecurityViewModel
import com.zhuorui.securities.personal.databinding.FragmentSecurityBinding
import kotlinx.android.synthetic.main.fragment_security.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/9
 * Desc:
 */
class SecurityFragment :
    AbsSwipeBackFragment<FragmentSecurityBinding, SecurityViewModel, SecurityView, SecurityPresenter>(), SecurityView,View.OnClickListener {
    override val layout: Int
        get() = R.layout.fragment_security
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: SecurityPresenter
        get() = SecurityPresenter()
    override val createViewModel: SecurityViewModel?
        get() = ViewModelProviders.of(this).get(SecurityViewModel::class.java)
    override val getView: SecurityView
        get() = this

    companion object {
        fun newInstance(): SecurityFragment {
            return SecurityFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        //presenter?.getUserInfo()
        tv_change_phone.setOnClickListener(this)
        tv_repaired_login_pass.setOnClickListener(this)
        repaired_trade_pass.setOnClickListener(this)
    }
    override fun onClick(p0: View?) {
       when(p0?.id){
           R.id.tv_change_phone->{
               start(ChangePhoneNumFragment.newInstance())
           }
           R.id.tv_repaired_login_pass->{
               start(RepairedLoginPassFragment.newInstance())
           }
           R.id.repaired_trade_pass->{
               start(RepairedTradePassFragment.newInstance())
           }
       }
    }
}