package com.zhuorui.securities.personal.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.ui.presenter.PhoneDevVerifyCodePresenter
import com.zhuorui.securities.personal.ui.view.PhoneDevVerifyCodeView
import com.zhuorui.securities.personal.ui.viewmodel.PhoneDevVerifyCodeViewModel
import com.zhuorui.securities.personal.databinding.FragmentPhoneDevVerifyCodeBinding
import com.zhuorui.securities.personal.widget.PasswordView
import kotlinx.android.synthetic.main.fragment_phone_dev_verify_code.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/12
 * Desc:
 */
class PhoneDevVerifyCodeFragment :AbsSwipeBackNetFragment<FragmentPhoneDevVerifyCodeBinding,PhoneDevVerifyCodeViewModel,PhoneDevVerifyCodeView,PhoneDevVerifyCodePresenter>(),PhoneDevVerifyCodeView,PasswordView.PasswordListener{
    private var phone: String? = null
    private var phoneArea: String? = null
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
        fun newInstance(phone:String?,phoneArea:String): PhoneDevVerifyCodeFragment {
            val fragment = PhoneDevVerifyCodeFragment()
            if (phone!= null&&phoneArea!=null) {
                val bundle = Bundle()
                bundle.putSerializable("phone", phone)
                bundle.putSerializable("phoneArea", phoneArea)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        phone = arguments?.getSerializable("phone") as String?
        phoneArea = arguments?.getSerializable("phoenArea") as String?
        et_phone_code.setPasswordListener(this)
    }
    override fun passwordChange(changeText: String?) {

    }

    override fun passwordComplete() {

    }

    override fun keyEnterPress(password: String?, isComplete: Boolean) {

    }

}