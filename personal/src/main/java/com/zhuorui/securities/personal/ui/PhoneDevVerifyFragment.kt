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
import com.zhuorui.commonwidget.common.CountryCodeConfig
import com.zhuorui.commonwidget.dialog.DevComfirmDailog
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.FragmentPhoneDevVerifyBinding
import com.zhuorui.securities.personal.ui.presenter.PhoneDevVerifyPresenter
import com.zhuorui.securities.personal.ui.view.PhoneDevVerifyView
import com.zhuorui.securities.personal.ui.viewmodel.PhoneDevVerifyViewModel
import com.zhuorui.securities.personal.util.PhoneHideUtils
import kotlinx.android.synthetic.main.fragment_phone_dev_verify.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/12
 * Desc:发送手机号验证码
 * */
class PhoneDevVerifyFragment :AbsSwipeBackNetFragment<FragmentPhoneDevVerifyBinding,PhoneDevVerifyViewModel,PhoneDevVerifyView,PhoneDevVerifyPresenter>(),PhoneDevVerifyView,View.OnClickListener,DevComfirmDailog.CallBack,
    CheckRequestPermissionsListener {
    var permissions = arrayOf(Manifest.permission.CALL_PHONE)
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(requireContext())
    }

    private var phone: String  = ""
    override val layout: Int
        get() = R.layout.fragment_phone_dev_verify
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: PhoneDevVerifyPresenter
        get() =PhoneDevVerifyPresenter(requireContext())
    override val createViewModel: PhoneDevVerifyViewModel?
        get() =ViewModelProviders.of(this).get(PhoneDevVerifyViewModel::class.java)
    override val getView: PhoneDevVerifyView
        get() = this
    companion object {
        fun newInstance(str:String?): PhoneDevVerifyFragment {
            val fragment = PhoneDevVerifyFragment()
            if (str != null) {
                val bundle = Bundle()
                bundle.putString("phone", str)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        phone = arguments?.getString("phone")?:phone
        tv_phone_num.text=PhoneHideUtils.hidePhoneNum(phone)
        tv_btn_complete.setOnClickListener(this)
        unable_verify_phone.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
       when(p0?.id){
           R.id.tv_btn_complete->{
               phone?.let {
                       it -> presenter?.requestSendLoginCode(it)
               }

           }
           R.id.unable_verify_phone->{
               DevComfirmDailog.
                   createWidth255Dialog(requireContext(),true,true)
                   .setNoticeText(R.string.notice)
                   .setMsgText(R.string.dev_login_problem_tips)
                   .setCancelText(R.string.cancle)
                   .setConfirmText(R.string.phone_call)
                   .setCallBack(this).show()
           }
       }
    }

   fun gotoPhone() {
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
    override fun goNext() {
        start(PhoneDevVerifyCodeFragment.newInstance(phone, CountryCodeConfig.read().defaultCode))
    }

    fun dialogshow(type:Int){
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
    override fun onCancel() {

    }

    override fun onConfirm() {
        gotoPhone()
    }

    override fun showProgressDailog(type: Int) {
        dialogshow(type)
    }



}