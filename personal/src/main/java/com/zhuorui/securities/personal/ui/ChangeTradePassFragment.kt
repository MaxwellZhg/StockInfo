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
import com.zhuorui.securities.personal.databinding.FragmentChangeTradePassBinding
import com.zhuorui.securities.personal.ui.presenter.ChangeTradePassPresenter
import com.zhuorui.securities.personal.ui.view.ChangeTradePassView
import com.zhuorui.securities.personal.ui.viewmodel.ChangeTradePassViewModel
import kotlinx.android.synthetic.main.fragment_change_new_phone_num.*
import kotlinx.android.synthetic.main.fragment_change_trade_pass.*
import kotlinx.android.synthetic.main.fragment_change_trade_pass.tv_btn_complete
import kotlinx.android.synthetic.main.fragment_repaired_login_psw.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/11
 * Desc:
 */
class ChangeTradePassFragment :AbsSwipeBackNetFragment<FragmentChangeTradePassBinding,ChangeTradePassViewModel,ChangeTradePassView,ChangeTradePassPresenter>(),ChangeTradePassView,TextWatcher,View.OnClickListener{
    private lateinit var strnewcapsw: String
    private lateinit var strencapsw: String
    override val layout: Int
        get() = R.layout.fragment_change_trade_pass
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: ChangeTradePassPresenter
        get() = ChangeTradePassPresenter(requireContext())
    override val createViewModel: ChangeTradePassViewModel?
        get() = ViewModelProviders.of(this).get(ChangeTradePassViewModel::class.java)
    override val getView: ChangeTradePassView
        get() = this
    companion object {
        fun newInstance(): ChangeTradePassFragment {
            return ChangeTradePassFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        et_ensure_new_capital_psw.addTextChangedListener(this)
        tv_btn_complete.setOnClickListener(this)
    }
    override fun gotomain() {
        popTo(SecurityFragment::class.java,false)
    }
    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if(TextUtils.isEmpty(et_ensure_new_capital_psw.text.toString())){
                    ToastUtil.instance.toast(R.string.please_input_ensure_capital_psw)
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
    override fun onClick(p0: View?) {
        strnewcapsw=et_new_capital_psw.text.toString().trim()
        strencapsw=et_ensure_new_capital_psw.text.toString().trim()
        when(p0?.id){
           R.id.tv_btn_complete->{
                presenter?.detailTips(strnewcapsw,strencapsw)
            }
        }
    }
}