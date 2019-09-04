package com.zhuorui.securities.openaccount.ui.presenter

import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import com.zhuorui.securities.base2app.ui.fragment.AbsPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.R2.array.gender
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.ui.view.OAPersonalInformationView
import com.zhuorui.securities.openaccount.ui.view.OAUploadDocumentsView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAPersonalInformationViewModel
import com.zhuorui.securities.openaccount.ui.viewmodel.OAUploadDocumentsViewModel
import com.zhuorui.securities.pickerview.option.OnOptionSelectedListener

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:19
 *    desc   :
 */
class OAPersonalInformationPresenter : AbsPresenter<OAPersonalInformationView, OAPersonalInformationViewModel>() {

    var occupationPickerData: MutableList<String>? = null//就业状态数据
    var occupationCode: MutableList<Int>? = null//就业状态code
    var taxTypePickerData: MutableList<String>? = null//
    var taxTypeCode: MutableList<Int>? = null//
    var mOccupation: String? = null
    var mTaxType: String? = null
    var mTaxState: String? = null


    override fun init() {
        super.init()
        occupationPickerData = ResUtil.getStringArray(R.array.occupation)?.asList()?.toMutableList()
        occupationCode = ResUtil.getIntArray(R.array.occupation_code)?.asList()?.toMutableList()
        taxTypePickerData = ResUtil.getStringArray(R.array.tax_type)?.asList()?.toMutableList()
        taxTypeCode = ResUtil.getIntArray(R.array.tax_type_code)?.asList()?.toMutableList()
    }

    fun setDefData() {
        mOccupation = occupationPickerData?.get(1)
        view?.setOccupation(mOccupation)
        mTaxType = taxTypePickerData?.get(0)
        view?.setTaxType(mTaxType)
        mTaxState = ResUtil.getString(R.string.str_china)
        view?.setTaxState(mTaxState)
        view?.setTaxNo(OpenInfoManager.getInstance()?.info?.cardNo)
    }

    /**
     * 就业状态选择监听
     *
     */
    fun getOccupationPickerListener(): OnOptionSelectedListener<String> {
        return OnOptionSelectedListener { data ->
            mOccupation = data?.get(0)
            view?.setOccupation(mOccupation)
        }
    }

    /**
     * 税务信息选择监听
     *
     */
    fun getTaxTypePickerListener(): OnOptionSelectedListener<String> {
        return OnOptionSelectedListener { data ->
            mTaxType = data?.get(0)
            view?.setTaxType(mTaxType)
        }
    }

    fun checkData(): Boolean {
        var msg: String? = ""
        if (TextUtils.isEmpty(view?.getEmail())) {
            msg = ResUtil.getString(R.string.str_email_address) + ResUtil.getString(R.string.str_not_empty)
        } else if (TextUtils.isEmpty(view?.getTaxNo())) {
            msg = ResUtil.getString(R.string.str_tax_number) + ResUtil.getString(R.string.str_not_empty)
        }
        if (TextUtils.isEmpty(msg)) {
            return true
        }
        view?.showToast(msg)
        return false
    }

    fun getDialogSpanned(): Spannable? {
        val email = view?.getEmail()
        var txt = "邮箱将用于接收结单等重要操作请再次确认是否输入正确\n"
        var spannable = SpannableString(txt + email)
        spannable.setSpan(
            ForegroundColorSpan(ResUtil.getColor(R.color.app_bule)!!),
            txt.length,
            spannable.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    fun sub() {
        var occupation = occupationPickerData?.indexOf(mOccupation)?.let { occupationCode?.get(it) }
        var taxType = taxTypePickerData?.indexOf(mTaxType)?.let { taxTypeCode?.get(it) }
        OpenInfoManager.getInstance()?.info?.occupation = occupation
        OpenInfoManager.getInstance()?.info?.taxType = taxType
        OpenInfoManager.getInstance()?.info?.taxState = mTaxState
        OpenInfoManager.getInstance()?.info?.mailbox = view?.getEmail()
        OpenInfoManager.getInstance()?.info?.taxNumber = view?.getTaxNo()
        view?.toNext()
    }


}