package com.zhuorui.securities.personal.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.FragmentPhoneDevVerifyBinding
import com.zhuorui.securities.personal.ui.presenter.PhoneDevVerifyPresenter
import com.zhuorui.securities.personal.ui.view.PhoneDevVerifyView
import com.zhuorui.securities.personal.ui.viewmodel.PhoneDevVerifyViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/12
 * Desc:
 */
class PhoneDevVerifyFragment :AbsSwipeBackNetFragment<FragmentPhoneDevVerifyBinding,PhoneDevVerifyViewModel,PhoneDevVerifyView,PhoneDevVerifyPresenter>(),PhoneDevVerifyView{
    override val layout: Int
        get() = R.layout.fragment_phone_dev_verify
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: PhoneDevVerifyPresenter
        get() =PhoneDevVerifyPresenter()
    override val createViewModel: PhoneDevVerifyViewModel?
        get() =ViewModelProviders.of(this).get(PhoneDevVerifyViewModel::class.java)
    override val getView: PhoneDevVerifyView
        get() = this
    companion object {
        fun newInstance(): PhoneDevVerifyFragment {
            return PhoneDevVerifyFragment()
        }
    }

}