package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.FragmentRepairedTradePassBinding
import com.zhuorui.securities.personal.ui.presenter.RepairedTradePassPresenter
import com.zhuorui.securities.personal.ui.view.RepairedTradePassView
import com.zhuorui.securities.personal.ui.viewmodel.RepairedTradePassViewModel
import kotlinx.android.synthetic.main.fragment_repaired_login_psw.*
import kotlinx.android.synthetic.main.fragment_repaired_trade_pass.*
import kotlinx.android.synthetic.main.fragment_repaired_trade_pass.tv_btn_complete

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/11
 * Desc:
 */
class RepairedTradePassFragment :AbsSwipeBackNetFragment<FragmentRepairedTradePassBinding,RepairedTradePassViewModel,RepairedTradePassView,RepairedTradePassPresenter>(),RepairedTradePassView,View.OnClickListener,TextWatcher{

    override val layout: Int
        get() = R.layout.fragment_repaired_trade_pass
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: RepairedTradePassPresenter
        get() = RepairedTradePassPresenter()
    override val createViewModel: RepairedTradePassViewModel?
        get() =  ViewModelProviders.of(this).get(RepairedTradePassViewModel::class.java)
    override val getView: RepairedTradePassView
        get() = this
    companion object {
        fun newInstance(): RepairedTradePassFragment {
            return RepairedTradePassFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        tv_forget_psw.setOnClickListener(this)
        et_trade_psw.addTextChangedListener(this)
        tv_btn_complete.setOnClickListener(this)
    }
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tv_forget_psw->{
                start(ForgetTradePassFragment.newInstance())
            }
            R.id.tv_btn_complete->{
                start(ChangeTradePassFragment.newInstance())
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if(TextUtils.isEmpty(et_trade_psw.text.toString())){
                    ToastUtil.instance.toast(R.string.input_old_trade_pass_tips)
                }else {
                    tv_btn_complete.isEnabled=true
                }
            }
        } else {
            tv_btn_complete.isEnabled=false
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

}