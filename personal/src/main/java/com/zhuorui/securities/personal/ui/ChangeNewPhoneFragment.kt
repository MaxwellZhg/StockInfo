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
import com.zhuorui.securities.personal.databinding.FragmentChangeNewPhoneNumBinding
import com.zhuorui.securities.personal.ui.presenter.ChangeNewPhonePresenter
import com.zhuorui.securities.personal.ui.view.ChangeNewPhoneView
import com.zhuorui.securities.personal.ui.viewmodel.ChangeNewPhoneViewModel
import kotlinx.android.synthetic.main.fragment_change_new_phone_num.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/10
 * Desc:
 */
class ChangeNewPhoneFragment :AbsSwipeBackNetFragment<FragmentChangeNewPhoneNumBinding,ChangeNewPhoneViewModel,ChangeNewPhoneView,ChangeNewPhonePresenter>(),ChangeNewPhoneView,TextWatcher,View.OnClickListener{
    private lateinit var strnewphone: String
    private lateinit var strnewphonecode: String
    override val layout: Int
        get() = R.layout.fragment_change_new_phone_num
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: ChangeNewPhonePresenter
        get() = ChangeNewPhonePresenter()
    override val createViewModel: ChangeNewPhoneViewModel?
        get() = ViewModelProviders.of(this).get(ChangeNewPhoneViewModel::class.java)
    override val getView: ChangeNewPhoneView
        get() = this
    companion object {
        fun newInstance(): ChangeNewPhoneFragment {
            return ChangeNewPhoneFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        et_verify_code.addTextChangedListener(this)
        tv_btn_complete.setOnClickListener(this)
    }
    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if(TextUtils.isEmpty(et_verify_code.text.toString())){
                    ToastUtil.instance.toast(R.string.phone_code_tips)
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
        strnewphone=et_new_phone.text.toString().trim()
        strnewphonecode=et_verify_code.text.toString().trim()
       when(p0?.id){
           R.id.tv_btn_complete->{
             if(presenter?.detailPhoneTips(strnewphone,strnewphonecode)!!){

             }
           }
       }
    }

}