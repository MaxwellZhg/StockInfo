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
import com.zhuorui.securities.personal.databinding.FragmentRepairedLoginPswBinding
import com.zhuorui.securities.personal.ui.presenter.RepairedLoginPassPresenter
import com.zhuorui.securities.personal.ui.view.RepairedLoginPassView
import com.zhuorui.securities.personal.ui.viewmodel.RepairedLoginPassViewModel
import kotlinx.android.synthetic.main.fragment_repaired_login_psw.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/11
 * Desc:
 */
class RepairedLoginPassFragment :AbsSwipeBackNetFragment<FragmentRepairedLoginPswBinding,RepairedLoginPassViewModel,RepairedLoginPassView,RepairedLoginPassPresenter>(),RepairedLoginPassView,View.OnClickListener,TextWatcher{

    private lateinit var stroldpsw: String
    private lateinit var strnewpsw: String
    private lateinit var strensurepsw: String

    override val layout: Int
        get() = R.layout.fragment_repaired_login_psw
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: RepairedLoginPassPresenter
        get() = RepairedLoginPassPresenter(requireContext())
    override val createViewModel: RepairedLoginPassViewModel?
        get() = ViewModelProviders.of(this).get(RepairedLoginPassViewModel::class.java)
    override val getView: RepairedLoginPassView
        get() = this
    companion object {
        fun newInstance(): RepairedLoginPassFragment {
            return RepairedLoginPassFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        tv_btn_complete.setOnClickListener(this)
        et_ensure_psw.addTextChangedListener(this)
    }
    override fun onClick(p0: View?) {
        stroldpsw=et_old_psw.text.toString().trim()
        strnewpsw=et_new_psw.text.toString().trim()
        strensurepsw=et_ensure_psw.text.toString().trim()
      when(p0?.id){
          R.id.tv_btn_complete->{
            presenter?.detailPass(stroldpsw,strnewpsw,strensurepsw).let {
                when(it){
                    true->{
                        presenter?.modifyNewLoginPsw(stroldpsw,strensurepsw)
                    }
                    else->{

                    }
                }
            }
          }
        }
    }
    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if(TextUtils.isEmpty(et_ensure_psw.text.toString())){
                    ToastUtil.instance.toast(R.string.please_ensure_password)
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
    override fun gotomain() {
        popTo(SecurityFragment::class.java,false)
    }
}