package com.zhuorui.securities.personal.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.qw.soul.permission.SoulPermission
import com.qw.soul.permission.bean.Permission
import com.qw.soul.permission.bean.Permissions
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener
import com.zhuorui.commonwidget.dialog.DevComfirmDailog
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.ui.activity.AbsActivity
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.ui.presenter.PhoneDevVerifyCodePresenter
import com.zhuorui.securities.personal.ui.view.PhoneDevVerifyCodeView
import com.zhuorui.securities.personal.ui.viewmodel.PhoneDevVerifyCodeViewModel
import com.zhuorui.securities.personal.databinding.FragmentPhoneDevVerifyCodeBinding
import com.zhuorui.securities.personal.util.PatternUtils
import com.zhuorui.securities.personal.util.PhoneHideUtils

import kotlinx.android.synthetic.main.fragment_phone_dev_verify_code.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/12
 * Desc:手机号设备验证码 */
class PhoneDevVerifyCodeFragment :
    AbsSwipeBackNetFragment<FragmentPhoneDevVerifyCodeBinding, PhoneDevVerifyCodeViewModel, PhoneDevVerifyCodeView, PhoneDevVerifyCodePresenter>(),
    PhoneDevVerifyCodeView, View.OnClickListener, TextWatcher, AbsActivity.OnDispatchTouchEventListener,
    CheckRequestPermissionsListener,DevComfirmDailog.CallBack {
    /* 加载进度条 */
    private val progressDialog by lazy {
        ProgressDialog(requireContext())
    }
    /* 加载对话框 */
    private val phoneDevDailog by lazy {
        DevComfirmDailog.
            createWidth255Dialog(requireContext(),true,true)
            .setNoticeText(R.string.notice)
            .setMsgText(R.string.dev_login_problem_tips)
            .setCancelText(R.string.cancle)
            .setConfirmText(R.string.phone_call)
            .setCallBack(this)
    }

    private var phone: String = ""
    private var phoneArea: String = ""
    private var code: String? = null

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
        fun newInstance(phone: String?, phoneArea: String): PhoneDevVerifyCodeFragment {
            val fragment = PhoneDevVerifyCodeFragment()
            if (phone != null && phoneArea != null) {
                val bundle = Bundle()
                bundle.putString("phone", phone)
                bundle.putString("phoneArea", phoneArea)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        phone = arguments?.getString("phone")?:phone
        phoneArea = arguments?.getString("phoneArea") ?:phoneArea
        et_phone_code.addTextChangedListener(this)
        tv_no_reciver_code.setOnClickListener(this)
        tv_timer_tips.setOnClickListener(this)
        tv_phone_timer_tips.text =
            ResUtil.getString(R.string.phone_dev_timer_phonenum_tips) + PhoneHideUtils.hidePhoneNum(phone)
        presenter?.startTimeCountDown()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_no_reciver_code -> {
               phoneDevDailog.show()
            }
            R.id.tv_timer_tips->{
                phone?.let { presenter?.requestSendLoginCode(it) }
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
        } else {
            presenter?.luanchCall()
        }
    }

    override fun onAllPermissionOk(allPermissions: Array<out Permission>?) {
        presenter?.luanchCall()
    }

    override fun onPermissionDenied(refusedPermissions: Array<out Permission>?) {

    }

    override fun gotomain() {
        val toFragment = (activity as AbsActivity).supportFragmentManager.fragments[0] as AbsFragment<*, *, *, *>
        popTo(toFragment::class.java, false)
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0?.length == 6) {
            if(PatternUtils.patternPhoneCode(p0.toString())) {
                presenter?.requestUserLoginCode(phone, p0.toString(), phoneArea)
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTouch(event: MotionEvent?) {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            hideSoftInput()
        }
    }

    override fun onDetach() {
        super.onDetach()
        (activity as AbsActivity).removeDispatchTouchEventListener(this)
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

    override fun showProgressDailog(type: Int) {
        dialogshow(type)
    }

    override fun onCancel() {

    }

    override fun onConfirm() {
        gotoPhone()
    }



}