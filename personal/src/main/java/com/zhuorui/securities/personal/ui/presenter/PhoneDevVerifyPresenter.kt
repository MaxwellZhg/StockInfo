package com.zhuorui.securities.personal.ui.presenter

import android.content.Context
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.ui.dailog.DevComfirmDailog
import com.zhuorui.securities.personal.ui.view.PhoneDevVerifyView
import com.zhuorui.securities.personal.ui.viewmodel.PhoneDevVerifyViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/12
 * Desc:
 */
class PhoneDevVerifyPresenter(context: Context):AbsNetPresenter<PhoneDevVerifyView,PhoneDevVerifyViewModel>(),DevComfirmDailog.CallBack{

    override fun init() {
        super.init()
    }
    /* 加载进度条 */
    private val phoneDevDailog by lazy {
       DevComfirmDailog.
            createWidth255Dialog(context,true,true)
            .setNoticeText(R.string.notice)
            .setMsgText(R.string.dev_login_problem_tips)
            .setCancelText(R.string.cancle)
            .setConfirmText(R.string.phone_call)
            .setCallBack(this)
    }
    override fun onCancel() {

    }

    override fun onConfirm() {
      view?.gotoPhone()
    }

    fun showTipsDailog(){
        phoneDevDailog.show()
    }
}