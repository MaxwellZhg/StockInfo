package com.zhuorui.securities.openaccount.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaAuthenticationBinding
import com.zhuorui.securities.openaccount.ui.presenter.OAAuthenticationPresenter
import com.zhuorui.securities.openaccount.ui.view.OAAuthenticationView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAAuthenticationViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-23 14:09
 *    desc   :
 */
class OAAuthenticationFragment :
    AbsFragment<FragmentOaAuthenticationBinding, OAAuthenticationViewModel, OAAuthenticationView, OAAuthenticationPresenter>(),
    OAAuthenticationView {

    companion object {
        fun newInstance(): OAAuthenticationFragment {
            return OAAuthenticationFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_oa_authentication

    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: OAAuthenticationPresenter
        get() = OAAuthenticationPresenter()
    override val createViewModel: OAAuthenticationViewModel?
        get() = ViewModelProviders.of(this).get(OAAuthenticationViewModel::class.java)
    override val getView: OAAuthenticationView
        get() = this

    override fun init() {

    }

}