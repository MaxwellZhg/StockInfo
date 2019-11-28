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
import kotlinx.android.synthetic.main.fragment_change_trade_pass.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/11
 * Desc:修改交易密码
 * */
class ChangeTradePassFragment :AbsSwipeBackNetFragment<FragmentChangeTradePassBinding,ChangeTradePassViewModel,ChangeTradePassView,ChangeTradePassPresenter>(),ChangeTradePassView,TextWatcher,View.OnClickListener{
    private lateinit var strnewcapsw: String
    private lateinit var strencapsw: String
    private var oldcapsw:String?=null
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
        fun newInstance(oldcapsw:String): ChangeTradePassFragment {
            val fragment = ChangeTradePassFragment()
            if (oldcapsw != "") {
                val bundle = Bundle()
                bundle.putSerializable("oldcapsw", oldcapsw)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        oldcapsw = arguments?.getSerializable("oldcapsw") as String?
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
        //todo 操作交易密码待验证
        strnewcapsw=et_new_capital_psw.text.toString().trim()
        strencapsw=et_ensure_new_capital_psw.text.toString().trim()
        when(p0?.id){
           R.id.tv_btn_complete->{
                presenter?.detailTips(strnewcapsw,strencapsw).let {
                    when(it){
                        true->{
                            oldcapsw?.let { it1 -> presenter?.modifyCapitalPsw(it1,strencapsw) }
                        }
                        else->{

                        }
                    }
                }
            }
        }
    }
}