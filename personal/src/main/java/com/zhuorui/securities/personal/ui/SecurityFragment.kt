package com.zhuorui.securities.personal.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.ui.presenter.SecurityPresenter
import com.zhuorui.securities.personal.ui.view.SecurityView
import com.zhuorui.securities.personal.ui.viewmodel.SecurityViewModel
import com.zhuorui.securities.personal.databinding.FragmentSecurityBinding

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/9
 * Desc:
 */
class SecurityFragment :
    AbsSwipeBackFragment<FragmentSecurityBinding, SecurityViewModel, SecurityView, SecurityPresenter>(), SecurityView {
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
        presenter?.getUserInfo()
    }
}