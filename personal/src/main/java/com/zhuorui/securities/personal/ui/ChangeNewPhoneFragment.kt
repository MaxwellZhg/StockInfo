package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.common.CommonCountryCodeFragment
import com.zhuorui.commonwidget.common.CommonEnum
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.FragmentChangeNewPhoneNumBinding
import com.zhuorui.securities.personal.ui.presenter.ChangeNewPhonePresenter
import com.zhuorui.securities.personal.ui.view.ChangeNewPhoneView
import com.zhuorui.securities.personal.ui.viewmodel.ChangeNewPhoneViewModel
import kotlinx.android.synthetic.main.fragment_change_new_phone_num.*
import kotlinx.android.synthetic.main.login_and_register_fragment.*
import me.jessyan.autosize.utils.LogUtils
import me.yokeyword.fragmentation.ISupportFragment

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/10
 * Desc:修改新手机号
 * */
class ChangeNewPhoneFragment :AbsSwipeBackNetFragment<FragmentChangeNewPhoneNumBinding,ChangeNewPhoneViewModel,ChangeNewPhoneView,ChangeNewPhonePresenter>(),ChangeNewPhoneView,TextWatcher,View.OnClickListener{

    private lateinit var strnewphone: String
    private lateinit var strnewphonecode: String
    private var oldphone:String?= null
    private var code : String? =null
    override val layout: Int
        get() = R.layout.fragment_change_new_phone_num
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: ChangeNewPhonePresenter
        get() = ChangeNewPhonePresenter(requireContext())
    override val createViewModel: ChangeNewPhoneViewModel?
        get() = ViewModelProviders.of(this).get(ChangeNewPhoneViewModel::class.java)
    override val getView: ChangeNewPhoneView
        get() = this
    companion object {
        fun newInstance(oldphone:String?,code:String): ChangeNewPhoneFragment {
            val fragment = ChangeNewPhoneFragment()
            if (oldphone != ""&&code!="") {
                val bundle = Bundle()
                bundle.putSerializable("oldphone", oldphone)
                bundle.putSerializable("code",code)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        oldphone = arguments?.getSerializable("oldphone") as String
        code = arguments?.getSerializable("code") as String
        et_verify_code.addTextChangedListener(this)
        tv_btn_complete.setOnClickListener(this)
        tv_get_code.setOnClickListener(this)
        rl_select_disctry.setOnClickListener(this)
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
        //todo 修改手机号待验证
        strnewphone=et_new_phone.text.toString().trim()
        strnewphonecode=et_verify_code.text.toString().trim()
       when(p0?.id){
           R.id.tv_btn_complete->{
            presenter?.detailPhoneTips(strnewphone,strnewphonecode,oldphone).let {
               when(it){
                   true->  presenter?.requestModifyNewRepaiedCode(oldphone,code,strnewphone,strnewphonecode)
                   else -> {

                    }
               }
            }

           }
           R.id.tv_get_code->{
               presenter?.requestSendNewRepaiedCode(strnewphone)
           }
           R.id.rl_select_disctry->{
               startForResult(CommonCountryCodeFragment.newInstance(CommonEnum.Code), ISupportFragment.RESULT_OK)
           }
       }
    }

    override fun gotomain() {
        popTo(SecurityFragment::class.java,false)
    }
    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        when(requestCode){
            ISupportFragment.RESULT_OK->{
                var str=data?.get("str") as String
                var code=data?.get("code") as String
                LogUtils.e(str)
                tv_choosearea.text=str
                tv_choosearea_tips.text=code
            }
        }
    }
    override fun showgetCode(str: String) {
        et_verify_code.setText(str)
    }

}