package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.ui.presenter.ChangePhoneNumPresenter
import com.zhuorui.securities.personal.ui.view.ChangePhoneNumView
import com.zhuorui.securities.personal.ui.viewmodel.ChangePhoneNumViewModel
import com.zhuorui.securities.personal.util.PhoneHideUtils
import kotlinx.android.synthetic.main.fragment_change_phone_num.*
import com.zhuorui.securities.personal.databinding.FragmentChangePhoneNumBinding
import kotlinx.android.synthetic.main.forget_psw_fragment.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/10
 * Desc:修改手机号
 * */
class ChangePhoneNumFragment :AbsSwipeBackFragment<FragmentChangePhoneNumBinding,ChangePhoneNumViewModel,ChangePhoneNumView,ChangePhoneNumPresenter>(),ChangePhoneNumView,View.OnClickListener,TextWatcher {

    override val layout: Int
        get() = R.layout.fragment_change_phone_num
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: ChangePhoneNumPresenter
        get() = ChangePhoneNumPresenter(requireContext())
    override val createViewModel: ChangePhoneNumViewModel?
        get() = ViewModelProviders.of(this).get(ChangePhoneNumViewModel::class.java)
    override val getView: ChangePhoneNumView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if(LocalAccountConfig.getInstance().getAccountInfo().phone!=null) {
            tv_hide_phone.text = ResUtil.getString(R.string.phone_tips_hide)+PhoneHideUtils.hidePhoneNum(LocalAccountConfig.getInstance().getAccountInfo().phone)

        }
        tv_get_code.setOnClickListener(this)
        et_verify_code.addTextChangedListener(this)
        tv_btn_next.setOnClickListener(this)
    }
    companion object {
        fun newInstance(): ChangePhoneNumFragment {
            return ChangePhoneNumFragment()
        }
    }
    override fun onClick(p0: View?) {
       when(p0?.id){
           R.id.tv_get_code->{
               LocalAccountConfig.getInstance().getAccountInfo().phone?.let { presenter?.requestSendOldRepaiedCode(it) }
           }
           R.id.tv_btn_next->{
               if(LocalAccountConfig.getInstance().getAccountInfo().phone!="") {
                   presenter?.requestModifyOldPhone(
                       LocalAccountConfig.getInstance().getAccountInfo().phone,
                       et_verify_code.text.toString()
                   )
               }else{
                   ResUtil.getString(R.string.login_ahead)?.let { ToastUtil.instance.toast(it) }
               }
           }
       }
    }

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if(TextUtils.isEmpty(et_verify_code.text.toString())){
                    ToastUtil.instance.toast(R.string.phone_code_tips)
                }else {
                    tv_btn_next.isEnabled=true
                }
            }
        } else {
            tv_btn_next.isEnabled=false
        }
    }

    override fun gotonext() {
     start(ChangeNewPhoneFragment.newInstance(LocalAccountConfig.getInstance().getAccountInfo().phone,et_verify_code.text.toString()))
    }
    override fun showGetCode(str: String) {
        et_verify_code.setText(str)
    }
}