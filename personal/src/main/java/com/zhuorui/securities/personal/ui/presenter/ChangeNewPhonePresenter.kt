package com.zhuorui.securities.personal.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.ui.view.ChangeNewPhoneView
import com.zhuorui.securities.personal.ui.view.ChangePhoneNumView
import com.zhuorui.securities.personal.ui.viewmodel.ChangeNewPhoneViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/10
 * Desc:
 */
class ChangeNewPhonePresenter :AbsNetPresenter<ChangeNewPhoneView,ChangeNewPhoneViewModel>(){
    override fun init() {
        super.init()
    }

    fun detailPhoneTips(phone:String,code:String):Boolean{
        if(phone==""){
            ResUtil.getString(R.string.phone_tips)?.let { ToastUtil.instance.toast(it) }
            return false
        }
        if(code==null){
            ResUtil.getString(R.string.phone_code_tips)?.let { ToastUtil.instance.toast(it) }
            return false
        }
        if(code.length<6){
            ResUtil.getString(R.string.verify_code_length)?.let { ToastUtil.instance.toast(it) }
            return false
        }
        return true
    }
}