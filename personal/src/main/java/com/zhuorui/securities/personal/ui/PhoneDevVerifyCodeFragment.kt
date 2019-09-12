package com.zhuorui.securities.personal.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.ui.presenter.PhoneDevVerifyCodePresenter
import com.zhuorui.securities.personal.ui.view.PhoneDevVerifyCodeView
import com.zhuorui.securities.personal.ui.viewmodel.PhoneDevVerifyCodeViewModel
import com.zhuorui.securities.personal.databinding.FragmentPhoneDevVerifyCodeBinding
/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/12
 * Desc:
 */
class PhoneDevVerifyCodeFragment :AbsSwipeBackNetFragment<FragmentPhoneDevVerifyCodeBinding,PhoneDevVerifyCodeViewModel,PhoneDevVerifyCodeView,PhoneDevVerifyCodePresenter>(),PhoneDevVerifyCodeView{
    override val layout: Int
        get() = R.layout.fragment_phone_dev_verify_code
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: PhoneDevVerifyCodePresenter
        get() = PhoneDevVerifyCodePresenter()
    override val createViewModel: PhoneDevVerifyCodeViewModel?
        get() = ViewModelProviders.of(this).get(PhoneDevVerifyCodeViewModel::class.java)
    override val getView: PhoneDevVerifyCodeView
        get() = this
    companion object {
        fun newInstance(): PhoneDevVerifyCodeFragment {
            return PhoneDevVerifyCodeFragment()
        }
    }

}