package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.databinding.FragmentForgetTradePswBinding
import com.zhuorui.securities.personal.ui.presenter.ForgetTradePassPresenter
import com.zhuorui.securities.personal.ui.view.ForgetTradePassView
import com.zhuorui.securities.personal.ui.viewmodel.ForgetTradePassViewModel
import com.zhuorui.securities.personal.util.PhoneHideUtils
import kotlinx.android.synthetic.main.fragment_forget_trade_psw.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/11
 * Desc:忘记交易密码
 * */
class ForgetTradePassFragment :AbsSwipeBackNetFragment<FragmentForgetTradePswBinding,ForgetTradePassViewModel,ForgetTradePassView,ForgetTradePassPresenter>(),ForgetTradePassView,TextWatcher,View.OnClickListener{
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(requireContext())
    }
    override val layout: Int
        get() = R.layout.fragment_forget_trade_psw
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: ForgetTradePassPresenter
        get() = ForgetTradePassPresenter(requireContext())
    override val createViewModel: ForgetTradePassViewModel?
        get() = ViewModelProviders.of(this).get(ForgetTradePassViewModel::class.java)
    override val getView: ForgetTradePassView
        get() = this
    companion object {
        fun newInstance(): ForgetTradePassFragment {
            return ForgetTradePassFragment()
        }
    }
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if(LocalAccountConfig.getInstance().getAccountInfo().phone!=null) {
            tv_phone_tips.text= PhoneHideUtils.hidePhoneNum(LocalAccountConfig.getInstance().getAccountInfo().phone)
        }
        et_license_code.addTextChangedListener(this)
        tv_btn_complete.setOnClickListener(this)
    }
    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if(TextUtils.isEmpty(et_license_code.text.toString())){
                    ToastUtil.instance.toast(R.string.input_account_license_num)
                }else {
                    tv_btn_complete.isEnabled=true
                }
            }
        } else {
            tv_btn_complete.isEnabled=false
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tv_btn_complete->{
                start(ChangeTradePassFragment.newInstance(""))
            }
        }
    }

    private fun dialogshow(type:Int){
        when(type){
            1->{
                progressDialog.setCancelable(false)
                progressDialog.show()
            }
            else->{
                progressDialog.setCancelable(true)
                progressDialog.dismiss()

            }
        }
    }
    override fun showProgressDailog(type: Int) {
        dialogshow(type)
    }

}