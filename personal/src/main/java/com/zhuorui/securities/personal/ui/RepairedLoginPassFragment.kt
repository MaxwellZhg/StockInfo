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
import com.zhuorui.securities.personal.databinding.FragmentRepairedLoginPswBinding
import com.zhuorui.securities.personal.ui.presenter.RepairedLoginPassPresenter
import com.zhuorui.securities.personal.ui.view.RepairedLoginPassView
import com.zhuorui.securities.personal.ui.viewmodel.RepairedLoginPassViewModel
import com.zhuorui.securities.personal.util.PatternUtils
import kotlinx.android.synthetic.main.fragment_repaired_login_psw.*
import kotlinx.android.synthetic.main.fragment_repaired_login_psw.et_ensure_psw
import kotlinx.android.synthetic.main.login_psw_fragment.*
import kotlinx.android.synthetic.main.setting_psw_fragment.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/11
 * Desc:修改登录密码
 * */
class RepairedLoginPassFragment :AbsSwipeBackNetFragment<FragmentRepairedLoginPswBinding,RepairedLoginPassViewModel,RepairedLoginPassView,RepairedLoginPassPresenter>(),RepairedLoginPassView,View.OnClickListener,TextWatcher{
    private lateinit var stroldpsw: String
    private lateinit var strnewpsw: String
    private lateinit var strensurepsw: String
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(requireContext())
    }
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
        et_old_psw.addTextChangedListener(OldPasswordEtChange())
        et_new_psw.addTextChangedListener(NewPasswordEtChange())
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
                if(!TextUtils.isEmpty(p0.toString())&&!TextUtils.isEmpty(et_old_psw.text.toString())&&!TextUtils.isEmpty(et_new_psw.text.toString())){
                    tv_btn_complete.isEnabled = PatternUtils.patternLoginPassWord(p0.toString())
                }else{
                    tv_btn_complete.isEnabled =false
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

    inner class OldPasswordEtChange : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            if(!TextUtils.isEmpty(p0.toString())&&!TextUtils.isEmpty(et_new_psw.text.toString())&&!TextUtils.isEmpty(et_ensure_psw.text.toString())){
                tv_btn_complete.isEnabled = PatternUtils.patternLoginPassWord(p0.toString())
            }else{
                tv_btn_complete.isEnabled=false
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    }
    inner class NewPasswordEtChange : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            if(!TextUtils.isEmpty(p0.toString())&&!TextUtils.isEmpty(et_old_psw.text.toString())&&!TextUtils.isEmpty(et_ensure_psw.text.toString())){
                tv_btn_complete.isEnabled = PatternUtils.patternLoginPassWord(p0.toString())
            }else{
                tv_btn_complete.isEnabled =false
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    }
    override fun showProgressDailog(type: Int) {
        dialogshow(type)
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
}