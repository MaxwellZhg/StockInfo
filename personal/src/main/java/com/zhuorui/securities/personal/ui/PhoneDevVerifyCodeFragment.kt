package com.zhuorui.securities.personal.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.qw.soul.permission.SoulPermission
import com.qw.soul.permission.bean.Permission
import com.qw.soul.permission.bean.Permissions
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener
import com.zhuorui.securities.base2app.ui.activity.AbsActivity
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.ui.presenter.PhoneDevVerifyCodePresenter
import com.zhuorui.securities.personal.ui.view.PhoneDevVerifyCodeView
import com.zhuorui.securities.personal.ui.viewmodel.PhoneDevVerifyCodeViewModel
import com.zhuorui.securities.personal.databinding.FragmentPhoneDevVerifyCodeBinding
import com.zhuorui.securities.personal.util.PhoneHideUtils
import com.zhuorui.securities.personal.widget.PasswordView
import kotlinx.android.synthetic.main.fragment_phone_dev_verify_code.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/12
 * Desc:
 */
class PhoneDevVerifyCodeFragment :AbsSwipeBackNetFragment<FragmentPhoneDevVerifyCodeBinding,PhoneDevVerifyCodeViewModel,PhoneDevVerifyCodeView,PhoneDevVerifyCodePresenter>(),PhoneDevVerifyCodeView,PasswordView.PasswordListener,View.OnClickListener,
    CheckRequestPermissionsListener {
    private var phone: String? = null
    private var phoneArea: String? = null
    override val layout: Int
        get() = R.layout.fragment_phone_dev_verify_code
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: PhoneDevVerifyCodePresenter
        get() = PhoneDevVerifyCodePresenter(requireContext())
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
        phoneArea = arguments?.getSerializable("phoneArea") as String?
        et_phone_code.setPasswordListener(this)
        tv_no_reciver_code.setOnClickListener(this)
        tv_phone_timer_tips.text=ResUtil.getStringFormat(R.string.phone_dev_timer_phonenum_tips,PhoneHideUtils.hidePhoneNum(phone))
        presenter?.startTimeCountDown()
    }
    override fun passwordChange(changeText: String?) {

    }

    override fun passwordComplete() {
    }

    override fun keyEnterPress(password: String?, isComplete: Boolean) {
       if(isComplete){
           presenter?.requestUserLoginCode(phone,password,phoneArea)
       }
    }
    override fun onClick(p0: View?) {
      when(p0?.id){
          R.id.tv_no_reciver_code->{
            presenter?.showTipsDailog()
          }
      }
    }
    override fun gotoPhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(
                    Manifest.permission.CALL_PHONE
                ), this
            )
        }else{
            presenter?.luanchCall()
        }
    }
    override fun onAllPermissionOk(allPermissions: Array<out Permission>?) {
        presenter?.luanchCall()
    }

    override fun onPermissionDenied(refusedPermissions: Array<out Permission>?) {

    }
    override fun gotomain() {
        val toFragment=(activity as AbsActivity).supportFragmentManager.fragments[0]as AbsFragment<*, *, *, *>
        popTo(toFragment::class.java,false)
    }
}